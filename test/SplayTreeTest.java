import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SplayTreeTest {
    Set<Integer> testedSet = new SplayTree<>();

    @Test
    void size() {
        testedSet.add(1);
        testedSet.add(2);
        testedSet.add(3);
        testedSet.add(10);
        testedSet.add(70);
        assertEquals(5, testedSet.size());
        assertFalse(testedSet.remove(5));
        assertEquals(5, testedSet.size());
        assertTrue(testedSet.remove(10));
        assertEquals(4, testedSet.size());
    }

    @Test
    void iterator() {
    }
}