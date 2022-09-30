package deque;

import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> lld = new LinkedListDeque<Integer>(), lld3 = new LinkedListDeque<Integer>();
    public static Deque<String> lld2 = new LinkedListDeque<String>();

    @Test
    public void testAddFirst() {
        lld = new LinkedListDeque<Integer>();
        lld2 = new LinkedListDeque<String>();
        lld.addFirst(5);
        assertTrue("The list should be: 5", lld.get(0).equals(5));
        lld.addFirst(7);
        assertTrue("The list should be: 7 5", lld.get(0).equals(7) && lld.get(1).equals(5));
        lld2.addFirst("Hello");
        assertTrue("The list should be: Hello", lld2.get(0).equals("Hello"));
        lld2.addFirst("World");
        assertTrue("The list should be: World Hello", lld2.get(0).equals("World") && lld2.get(1).equals("Hello"));
    }
    @Test
    public void testAddLast() {
        lld = new LinkedListDeque<Integer>();
        lld2 = new LinkedListDeque<String>();
        lld.addFirst(5);
        assertTrue("The list should be: 5", lld.get(0) == 5);
        lld.addFirst(7);
        assertTrue("The list should be: 7 5", lld.get(0) == 7 && lld.get(1) == 5);
        lld2.addFirst("Hello");
        assertTrue("The list should be: Hello", lld2.get(0).equals("Hello"));
        lld2.addFirst("World");
        assertTrue("The list should be: World Hello", lld2.get(0).equals("World") && lld2.get(1).equals("Hello"));
    }
    @Test
    public void testIsEmpty() {
        lld = new LinkedListDeque<Integer>();
        lld2 = new LinkedListDeque<String>();
        assertTrue("Integer list should be empty.", lld.isEmpty());
        assertTrue("String list should be empty.", lld2.isEmpty());
        lld.addLast(5);
        lld2.addFirst("aaa");
        assertFalse("Integer list contains: 5. Should not be empty.", lld.isEmpty());
        assertFalse("String list contains: aaa. Should not be empty.", lld2.isEmpty());
        lld.removeFirst();
        lld2.removeLast();
        assertTrue("Integer list is empty.", lld.isEmpty());
        assertTrue("String list is empty", lld2.isEmpty());
    }
    @Test
    public void testSize() {
        for (int i = 0; i < 100; i++) {
            lld = new LinkedListDeque<Integer>();
            lld2 = new LinkedListDeque<String>();
            int addops = (int)(Math.random() * 300)+300, subops = (int)(Math.random() * 300), diff = addops-subops;
            for (int j = 0; j < addops; j++) {
                if (Math.random() < 0.5) {
                    lld.addFirst((int)(Math.random()*100));
                    lld2.addFirst("aaa");
                }
                else {
                    lld.addLast((int)(Math.random()*100));
                    lld2.addLast("bbb");
                }
            }
            for (int j = 0; j < subops; j++) {
                if (Math.random() < 0.5) {
                    lld.removeFirst();
                    lld2.removeFirst();
                }
                else {
                    lld.removeLast();
                    lld2.removeLast();
                }
            }
            assertEquals(diff, lld.size());
            assertEquals(diff, lld2.size());
        }
    }

    @Test
    public void removeFirstTest() {
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(7);
        assertEquals(7, (int) lld.removeFirst());
        assertEquals(3, (int) lld.removeFirst());
        assertEquals(5, (int) lld.removeFirst());
    }

    @Test
    public void removeLastTest(){
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(7);
        assertEquals(5, (int) lld.removeLast());
        assertEquals(3, (int) lld.removeLast());
        assertEquals(7, (int) lld.removeLast());
    }

    @Test
    public void getTest(){
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(7);
        assertEquals(7, (int) lld.get(0));
        assertEquals(7, (int) lld.get(0));
        assertEquals(3, (int) lld.get(1));
        assertEquals(5, (int) lld.get(2));
    }

    @Test
    public void getRecursiveTest(){
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(7);
        assertEquals(7, (int) ((LinkedListDeque<Integer>) lld).getRecursive(0));
        assertEquals(7, (int) ((LinkedListDeque<Integer>) lld).getRecursive(0));
        assertEquals(3, (int) ((LinkedListDeque<Integer>) lld).getRecursive(1));
        assertEquals(5, (int) ((LinkedListDeque<Integer>) lld).getRecursive(2));
    }
    @Test
    public void equalsTest(){
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(7);

        Deque<Integer> lld2 = new LinkedListDeque<Integer>();
        lld2.addFirst(5);
        lld2.addFirst(3);
        lld2.addFirst(7);

        lld3 = new LinkedListDeque<Integer>();
        lld3.addFirst(5);
        lld3.addFirst(3);
        lld3.addFirst(7);
        lld3.addFirst(7);

        assertTrue(lld.equals(lld2));
        assertFalse(lld.equals(lld3));
        assertFalse(lld2.equals(lld3));
    }
}
