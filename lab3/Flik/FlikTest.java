import static org.junit.Assert.*;

import org.junit.Test;
public class FlikTest {

    @Test
    public  void testFlik() {
        Integer A = 55;
        Integer B = 55;
        boolean res1 = Flik.isSameNumber(A, B);
        assertTrue("A and B are not same number", res1);

        Integer C = 666;
        Integer D = 666;
        boolean res2 = Flik.isSameNumber(C, D);
        assertTrue("C and D are not same number", res2);
    }


}
