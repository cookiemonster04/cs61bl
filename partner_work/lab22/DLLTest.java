import org.junit.Test;

public class DLLTest {

    @Test
    public void insSort(){
        DLList<Integer> l = new DLList<>();
        int[] arr = {4,7,9,2,0,3,8,1,9,3,5,6};
        for (int i = 0; i < arr.length; i++) {
            l.addLast(arr[i]);
        }
        l = l.insertionSort();
        System.out.println(l.toString());
    }
}
