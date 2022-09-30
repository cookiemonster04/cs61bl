import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleNameMapTest {
    @Test
    public void testSMT() {
        SimpleNameMap m = new SimpleNameMap();
        m.put("A", "1");
        assertTrue("put", m.get("A").equals("1"));
        m.put("Ab", "2");
        assertTrue("add ll", m.get("Ab").equals("2"));
        assertTrue(m.containsKey("Ab"));
        assertTrue(m.containsKey("A"));
    }
}
