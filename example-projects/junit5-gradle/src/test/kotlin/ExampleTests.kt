import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExampleTests {
    @Test
    fun one() {
        assertEquals("", System.getenv("SET_NON_EMPTY_TO_FAIL_TEST_ONE"));
    }

    @Test
    fun two() {
        assertFalse(false)
    }

    @Test
    fun three() {
        assertTrue(true)
    }
}
