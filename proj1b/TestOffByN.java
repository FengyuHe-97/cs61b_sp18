import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(5);

    // Your tests go here.

    @Test
    public void testEqualChars() {
        assertFalse("Fail equalchar.", offByN.equalChars('a', 'c'));
        assertFalse("Fail equalchar.", offByN.equalChars('a', 'A'));
        assertTrue(offByN.equalChars('a', 'f'));
        assertTrue(offByN.equalChars('f', 'a'));
    }
}
