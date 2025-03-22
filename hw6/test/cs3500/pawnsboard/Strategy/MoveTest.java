package cs3500.pawnsboard.Strategy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Move class.
 */
public class MoveTest {

    @Test
    public void testPassMove() {
        Move m = Move.passMove();
        assertTrue(m.isPass);
        assertEquals(-1, m.handIndex);
        assertEquals(-1, m.row);
        assertEquals(-1, m.col);
    }

    @Test
    public void testPlaceMove() {
        Move m = Move.placeMove(2, 1, 3);
        assertFalse(m.isPass);
        assertEquals(2, m.handIndex);
        assertEquals(1, m.row);
        assertEquals(3, m.col);
    }
}
