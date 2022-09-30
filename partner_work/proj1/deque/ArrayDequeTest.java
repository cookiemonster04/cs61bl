package deque;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> ad = new ArrayDeque<>(), ad3  = new deque.ArrayDeque<>();
    public static Deque<String> ad2  = new ArrayDeque<>();

    @Test
    public void testAddFirst() {
        ad = new ArrayDeque<Integer>();
        ad2 = new ArrayDeque<String>();
        ad.addFirst(5);
        assertTrue("The list should be: 5", ad.get(0) == 5);
        ad.addFirst(7);
        assertTrue("The list should be: 7 5", ad.get(0) == 7 && ad.get(1) == 5);
        ad2.addFirst("Hello");
        assertTrue("The list should be: Hello", ad2.get(0).equals("Hello"));
        ad2.addFirst("World");
        assertTrue("The list should be: World Hello", ad2.get(0).equals("World") && ad2.get(1).equals("Hello"));
    }
    @Test
    public void testAddLast() {
        ad = new ArrayDeque<Integer>();
        ad2 = new ArrayDeque<String>();
        ad.addFirst(5);
        assertTrue("The list should be: 5", ad.get(0) == 5);
        ad.addFirst(7);
        assertTrue("The list should be: 7 5", ad.get(0) == 7 && ad.get(1) == 5);
        ad2.addFirst("Hello");
        assertTrue("The list should be: Hello", ad2.get(0).equals("Hello"));
        ad2.addFirst("World");
        assertTrue("The list should be: World Hello", ad2.get(0).equals("World") && ad2.get(1).equals("Hello"));
    }
    @Test
    public void testIsEmpty() {
        ad = new ArrayDeque<Integer>();
        ad2 = new ArrayDeque<String>();
        assertTrue("Integer list should be empty.", ad.isEmpty());
        assertTrue("String list should be empty.", ad2.isEmpty());
        ad.addLast(5);
        ad2.addFirst("aaa");
        assertFalse("Integer list contains: 5. Should not be empty.", ad.isEmpty());
        assertFalse("String list contains: aaa. Should not be empty.", ad2.isEmpty());
        ad.removeFirst();
        ad2.removeLast();
        assertTrue("Integer list is empty.", ad.isEmpty());
        assertTrue("String list is empty", ad2.isEmpty());
    }
    @Test
    public void testSize() {
        for (int i = 0; i < 100; i++) {
            ad = new ArrayDeque<Integer>();
            ad2 = new ArrayDeque<String>();
            int addops = (int) (Math.random() * 300) + 300, subops = (int) (Math.random() * 300), diff = addops - subops;
            for (int j = 0; j < addops; j++) {
                if (Math.random() < 0.5) {
                    ad.addFirst((int) (Math.random() * 100));
                    ad2.addFirst("aaa");
                } else {
                    ad.addLast((int) (Math.random() * 100));
                    ad2.addLast("bbb");
                }
            }
            for (int j = 0; j < subops; j++) {
                if (Math.random() < 0.5) {
                    ad.removeFirst();
                    ad2.removeFirst();
                } else {
                    ad.removeLast();
                    ad2.removeLast();
                }
            }
            assertEquals(diff, ad.size());
            assertEquals(diff, ad2.size());
        }
    }
    @Test
    public void printDequeTest() {
        ad = new ArrayDeque<Integer>();
        ad2 = new ArrayDeque<String>();
        ad.addFirst(2);
        ad.addFirst(5);
        ad.addFirst(5);
        ad.removeLast();
        ad.removeFirst();
        ad.addLast(7);
        ad.printDeque();
        System.out.println("Integer list content: 5 7");
        ad2.addLast("Hello");
        ad2.addFirst("World");
        ad2.addLast("aaa");
        ad2.addLast("aaa");
        ad2.removeFirst();
        ad2.removeLast();
        ad2.printDeque();
        System.out.println("String list content: Hello aaa");
        ad = new ArrayDeque<Integer>();
        ad.addLast(5);
        ad.addLast(5);
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.addLast(5);
        ad.removeFirst();
        ad.printDeque();
    }
    @Test
    public void removeFirstTest() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(7);
        assertEquals(7, (int) ad.removeFirst());
        assertEquals(3, (int) ad.removeFirst());
        assertEquals(5, (int) ad.removeFirst());
    }

    @Test
    public void removeLastTest(){
        ad = new ArrayDeque<Integer>();
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(7);
        assertEquals(5, (int) ad.removeLast());
        assertEquals(3, (int) ad.removeLast());
        assertEquals(7, (int) ad.removeLast());
    }

    @Test
    public void getTest(){
        ad = new ArrayDeque<Integer>();
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(7);
        assertEquals(7, (int) ad.get(0));
        assertEquals(7, (int) ad.get(0));
        assertEquals(3, (int) ad.get(1));
        assertEquals(5, (int) ad.get(2));
    }

    @Test
    public void equalsTest(){
        ad = new ArrayDeque<Integer>();
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(7);

        Deque<Integer> ad2 = new ArrayDeque<Integer>();
        ad2.addFirst(5);
        ad2.addFirst(3);
        ad2.addFirst(7);

        ad3 = new ArrayDeque<Integer>();
        ad3.addFirst(5);
        ad3.addFirst(3);
        ad3.addFirst(7);
        ad3.addFirst(7);
        
        assertTrue(ad.equals(ad2));
        assertFalse(ad.equals(ad3));
        assertFalse(ad2.equals(ad3));
    }
    @Test
    public void customTest() {
        Deque<Integer> ArrayDeque = new ArrayDeque<>();
        ArrayDeque.addLast(0);
        ArrayDeque.removeLast();
        ArrayDeque.addLast(2);
        ArrayDeque.removeFirst();
        ArrayDeque.addLast(4);
        ArrayDeque.removeLast();
        ArrayDeque.addFirst(6);
        ArrayDeque.addLast(7);
        ArrayDeque.addLast(8);
        ArrayDeque.addLast(9);
        ArrayDeque.addLast(10);
        ArrayDeque.removeFirst();
        ArrayDeque.addFirst(12);
        ArrayDeque.removeLast();
        ArrayDeque.removeLast();
    }
}
