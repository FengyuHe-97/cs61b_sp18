package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* TODO: Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        // Your code here.
        /* 由Hint.java可知，当hashcode超过256的三次方时会由于超过int的范围导致溢出为0,因此，
           要体现hashcode()的缺陷只需要让每一个哈希值都溢出为0.(randomComplexOomage
           方法虽然会溢出但是是在中间溢出，溢出后因为有 + 运算，又会立刻加上一个不为0的数，
           所以最后计算出的哈希值不为0) */
        int N = 10;
        for (int i = 0; i < N; i++) {
            ArrayList<Integer> params = new ArrayList<>();
            params.add(i + 1);
            for (int j = 0; j < 5; j++) {
                params.add(0);
            }
            ComplexOomage co = new ComplexOomage(params);
            deadlyList.add(co);
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
