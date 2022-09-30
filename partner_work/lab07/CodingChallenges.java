import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        int sum = (values.length+1)*(values.length)/2;
        for (int i = 0; i < values.length; i++) {
            sum -= values[i];
        }
        return sum;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < values.length; i++) {
            if (set.contains(n-values[i])) {
                return true;
            }
            set.add(values[i]);
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        int[] cnt = new int[256];
        for (int i = 0; i < s1.length(); i++) {
            cnt[s1.charAt(i)]++;
        }
        for (int i = 0; i < s2.length(); i++) {
            cnt[s2.charAt(i)]--;
        }
        for (int i = 0; i < 256; i++) {
            if (cnt[i] != 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        System.out.println(missingNumber(new int[]{0, 1, 2, 4}));
        System.out.println(sumTo(new int[]{0, 1, 2, 4}, 6));
        System.out.println(sumTo(new int[]{0, 1, 2, 4}, 7));
        System.out.println(isPermutation("asdfasdf", "fdsaasdf"));
        System.out.println(isPermutation("asdfasdf", "asdfasdf"));
        System.out.println(isPermutation("asdfasdf", "asdfaedf"));
    }
}