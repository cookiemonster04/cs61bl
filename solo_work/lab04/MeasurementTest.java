import org.junit.Test;

import static org.junit.Assert.*;

public class MeasurementTest {
    @Test
    public void testDefaultConstructor() {
        Measurement defM = new Measurement();
        assertEquals(0, defM.getFeet());
        assertEquals(0, defM.getInches());
    }

    @Test
    public void testFeetConstructor() {
        Measurement defF = new Measurement(7);
        assertEquals(7, defF.getFeet());
        assertEquals(0, defF.getInches());
    }

    @Test
    public void testFeetInchConstructor() {
        Measurement defFI = new Measurement(5,5);
        assertEquals(5, defFI.getFeet());
        assertEquals(5, defFI.getInches());
    }

    @Test
    public void testgetFeet() {
        Measurement m = new Measurement(12,7);
        assertEquals(12, m.getFeet());
        Measurement m1 = new Measurement(0,0);
        assertEquals(0, m1.getFeet());
        Measurement m2 = new Measurement(7);
        assertEquals(7, m2.getFeet());
        Measurement m3 = new Measurement();
        assertEquals(0, m3.getFeet());
    }
    @Test
    public void testGetInches() {
        Measurement m = new Measurement(12,7);
        assertEquals(7, m.getInches());
        Measurement m1 = new Measurement(0,0);
        assertEquals(0, m1.getInches());
        Measurement m2 = new Measurement(7);
        assertEquals(0, m2.getInches());
        Measurement m3 = new Measurement();
        assertEquals(0, m3.getInches());
    }

    @Test
    public void testPlus() {
        Measurement m = new Measurement(), m1 = new Measurement(),
                m2 = new Measurement(3), m3 = new Measurement(4),
                m4 = new Measurement(5, 2), m5 = new Measurement(7, 11);
        Measurement res = m.plus(m1), res1 = m2.plus(m3), res2 = m4.plus(m5);
        Measurement res01 = m.plus(m2), res12 = m2.plus(m4), res02 = m.plus(m4);
        Measurement res10 = m2.plus(m), res21 = m4.plus(m2), res20 = m4.plus(m);
        assertEquals(0, res.getFeet()); assertEquals(0, res.getInches());
        assertEquals(7, res1.getFeet()); assertEquals(0, res1.getInches());
        assertEquals(13, res2.getFeet()); assertEquals(1, res2.getInches());
        assertEquals(3, res01.getFeet()); assertEquals(0, res01.getInches());
        assertEquals(8, res12.getFeet()); assertEquals(2, res12.getInches());
        assertEquals(5, res02.getFeet()); assertEquals(2, res02.getInches());
        assertEquals(3, res10.getFeet()); assertEquals(0, res10.getInches());
        assertEquals(8, res21.getFeet()); assertEquals(2, res21.getInches());
        assertEquals(5, res20.getFeet()); assertEquals(2, res20.getInches());
        Measurement m6 = new Measurement(2, 3);
        Measurement res6 = m6.plus(m4), res4 = m4.plus(m6);
        assertEquals(7, res6.getFeet()); assertEquals(5, res6.getInches());
        assertEquals(7, res4.getFeet()); assertEquals(5, res4.getInches());
    }
    @Test
    public void testMinus() {
        Measurement m = new Measurement(), m1 = new Measurement(),
                m2 = new Measurement(3), m3 = new Measurement(4),
                m4 = new Measurement(5, 2), m5 = new Measurement(7, 11);
        Measurement res = m.minus(m1), res1 = m3.minus(m2), res2 = m5.minus(m4);
        Measurement res10 = m2.minus(m), res21 = m4.minus(m2), res20 = m4.minus(m);
        assertEquals(0, res.getFeet()); assertEquals(0, res.getInches());
        assertEquals(1, res1.getFeet()); assertEquals(0, res1.getInches());
        assertEquals(2, res2.getFeet()); assertEquals(9, res2.getInches());
        assertEquals(3, res10.getFeet()); assertEquals(0, res10.getInches());
        assertEquals(2, res21.getFeet()); assertEquals(2, res21.getInches());
        assertEquals(5, res20.getFeet()); assertEquals(2, res20.getInches());
        Measurement m6 = new Measurement(2, 3);
        Measurement res4 = m4.minus(m6);
        assertEquals(2, res4.getFeet()); assertEquals(11, res4.getInches());
    }
    @Test
    public void testMultiple() {
        Measurement m = new Measurement(), m1 = new Measurement(5), m2 = new Measurement(3, 2);
        Measurement res00 = m.multiple(0), res01 = m.multiple(5);
        Measurement res10 = m1.multiple(0), res11 = m1.multiple(5);
        Measurement res20 = m2.multiple(0), res21 = m2.multiple(5), res22 = m2.multiple(6);
        assertEquals(0, res00.getFeet()); assertEquals(0, res00.getInches());
        assertEquals(0, res01.getFeet()); assertEquals(0, res01.getInches());
        assertEquals(0, res10.getFeet()); assertEquals(0, res10.getInches());
        assertEquals(0, res20.getFeet()); assertEquals(0, res20.getInches());
        assertEquals(25, res11.getFeet()); assertEquals(0, res11.getInches());
        assertEquals(15, res21.getFeet()); assertEquals(10, res21.getInches());
        assertEquals(19, res22.getFeet()); assertEquals(0, res22.getInches());
    }

    @Test
    public void testToString() {
        Measurement m = new Measurement(), m1 = new Measurement(5), m2 = new Measurement(10, 2);
        assertEquals("0'0\"", m.toString());
        assertEquals("5'0\"", m1.toString());
        assertEquals("10'2\"", m2.toString());
    }
}