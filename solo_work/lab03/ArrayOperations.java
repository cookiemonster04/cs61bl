public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        for (int i = pos; i < values.length-1; i++) {
            values[i] = values[i+1];
        }
        values[values.length-1] = 0;
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        for (int i = values.length-1; i > pos; i--) {
            values[i] = values[i-1];
        }
        values[pos] = newInt;
    }

    /** 
     * Returns a new array consisting of the elements of A followed by the
     *  the elements of B. 
     */
    public static int[] catenate(int[] A, int[] B) {
        // TODO: YOUR CODE HERE
        int[] arr = new int[A.length + B.length];
        for (int i = 0; i < A.length; i++) {
            arr[i] = A[i];
        }
        for (int i = 0; i < B.length; i++) {
            arr[i+A.length] = B[i];
        }
        return arr;
    }

    /** 
     * Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     */
    public static int[][] naturalRuns(int[] A) {
        int numRows = 1;
        for (int i = 1; i < A.length; i++) {
            if (A[i-1] >= A[i]) {
                numRows++;
            }
        }
        int[][] ret = new int[numRows][];
        int row = 0, rowStart = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] <= A[i-1]) { // assuming ascending = strictly increasing
                ret[row] = new int[i-rowStart];
                for (int j = rowStart; j < i; j++) {
                    ret[row][j-rowStart] = A[j];
                }
                row++;
                rowStart = i;
            }
        }
        ret[numRows-1] = new int[A.length-rowStart];
        for (int i = rowStart; i < A.length; i++) {
            ret[numRows-1][i-rowStart] = A[i];
        }
        return ret;
    }

    /*
    * Returns the subarray of A consisting of the LEN items starting
    * at index K.
    */
    public static int[] subarray(int[] A, int k, int len) {
        int[] result = new int[len];
        System.arraycopy(A, k, result, 0, len);
        return result;
    }

}