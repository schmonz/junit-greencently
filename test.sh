#!/bin/sh

set -e

stamp() {
	date -r DEPRECATION-NOTICE.md '+%s' 2>/dev/null || echo 0
}

scenario_count=0
scenario() {
	tests="$1"
	color="$2"
	scenario_count=$(expr 1 + ${scenario_count})

	stringy=""
	if [ "${color}" = red ]; then
		stringy="non-empty"
	fi

	printf "%s" "scenario ${scenario_count}: tests '${tests}', color '${color}'"
	../../gradlew -PsetNonEmptyToFailTestOne="${stringy}" clean test --tests "${tests}" >/dev/null 2>&1 || true
}

die() {
	echo "$@"
	exit 55
}

assert() {
	assertion="$1"
	printf "%s" ", assertion '${assertion}': "
	if eval "${assertion}"; then
		echo OK
	else
		die FAILED
	fi
}

build_like_github_actions_would() {
	sequence=$(
		grep gradlew .github/workflows/main-build.yml \
		| sed \
			-e 's|.*\./gradlew ||' \
			-e 's|-Pversion=.*}} ||' \
			-e 's|^publishTo.*|publishToMavenLocal|' \
		| tr '\n' ' '
	)
	./gradlew ${sequence}
}

main() {
	build_like_github_actions_would

	cd example-projects/junit5-gradle

	scenario '*'			red
	assert '[ 0 -eq $(stamp) ]'

	scenario '*'			green;	new=$(stamp)
	assert '[ 0 -lt ${new} ]'

	scenario '*'			green;	newer=$(stamp)
	assert '[ ${new} -lt ${newer} ]'

	scenario 'ExampleTests.one'	green
	assert '[ 0 -eq $(stamp) ]'

	scenario '*'			green
	assert '[ 0 -lt $(stamp) ]'

	scenario '*'			red
	assert '[ 0 -eq $(stamp) ]'
}

main "$@"
exit $?
