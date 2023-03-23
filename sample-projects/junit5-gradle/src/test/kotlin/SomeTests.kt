import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SomeTests {
    @Test
    fun `where timestamp file would go`() {
        assertTrue(System.getProperty("user.dir").endsWith("/junit5-gradle"))
    }

    @Test
    fun `false is false`() {
        assertFalse(false)
    }
}
