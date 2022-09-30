import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    public static void countingSort(int[] arr) {
        int[] occurances = new int[10];
        for (int i = 0; i<arr.length; i++) {
            int item = arr[i];
            occurances[item]++;
        }
        //prefix sums
        int[] startingIdx = new int[10];
        for (int i = 1; i < occurances.length; i++) {
            startingIdx[i] = occurances[i-1] + startingIdx[i-1];
        }
// Method 1
//        int idx = 0;
//        for (int i = 0; i < arr.length; i++) {
//            while (occurances[idx] == 0) {
//                idx++;
//            }
//            arr[i] = idx;
//            occurances[idx]--;
//        }

// Method 2 (both methods work)
        for (int i=0; i<startingIdx.length; i++) {
            while (occurances[i] != 0) {
                arr[startingIdx[i]] = i;
                startingIdx[i]++;
                occurances[i]--;
            }
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    private static void countingSortOnDigit(int[] arr, int digit) {
//        ArrayList<Integer>[] bucket = new ArrayList[10];
//        for (int i = 0; i < 10; i++) bucket[i] = new ArrayList<>();
//        int div = (int)Math.pow(10, digit);
//        for (int i = 0; i < arr.length; i++) {
//            int d = arr[i] / div % 10;
//            bucket[d].add(arr[i]);
//        }
//        int idx = 0;
//        for (ArrayList<Integer> a : bucket) {
//            for (int i : a) {
//                arr[idx++] = i;
//            }
//        }
        int[] startingIdx = new int[10];
        int div = (int) Math.pow(10, digit);
        for (int i : arr) {
            startingIdx[i / div % 10]++;
        }
        for (int i = 8; i >= 0; i--) {
            startingIdx[i+1] = startingIdx[i];
        }
        startingIdx[0] = 0;
        for (int i = 1; i < 10; i++) {
            startingIdx[i] += startingIdx[i-1];
        }
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        for (int i : copy) {
            arr[startingIdx[i / div % 10]++] = i;
        }
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}