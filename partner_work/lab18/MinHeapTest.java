import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void test1() {
        MinHeap<Character> h = new MinHeap<>();
        h.insert('f');
        h.insert('h');
        h.insert('d');
        h.insert('b');
        h.insert('c');
        System.out.println(h.toString());
        h.removeMin();
        h.removeMin();
        System.out.println(h.toString());
//        h.insert('c');
//        h.insert('g');
//        h.insert('a');
//        h.insert('d');
//        h.insert('b');
//        h.removeMin();
//        h.insert('c');
//        h.insert('e');
//        h.insert('h');
//        h.insert('i');
        System.out.println("");
    }
}
