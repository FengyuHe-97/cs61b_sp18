package hw3.hash;

import java.util.HashMap;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        HashMap<Integer, Integer> hm = new HashMap<>();
        for (Oomage o: oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            if (hm.containsKey(bucketNum)) {
                int value = hm.get(bucketNum);
                hm.put(bucketNum, ++value); //出现一个属于这个包的，value + 1
                continue;
            }
            hm.put(bucketNum, 1);
        }
        for (int key: hm.keySet()) {
            if (hm.get(key) < (oomages.size() / 50) || hm.get(key) > (oomages.size() / 2.5)) {
                return false;
            }
        }
        return true;
    }
}
