package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Tests for the CompositeStrategy.
 */
public class CompositeStrategyTest {

    // We'll create simple stubs for strategies that we can control.
    private static class PassStrategy implements Strategy {
        @Override
        public Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer) {
            return Move.passMove();
        }
    }

    private static class PlaceStrategy implements Strategy {
        private final int handIndex;
        private final int row;
        private final int col;

        public PlaceStrategy(int handIndex, int row, int col) {
            this.handIndex = handIndex;
            this.row = row;
            this.col = col;
        }

        @Override
        public Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer) {
            return Move.placeMove(handIndex, row, col);
        }
    }

    @Test
    public void testAllPass() {
        // If we have multiple pass strategies, result is pass
        Strategy comp = new CompositeStrategy(
                Collections.unmodifiableList(Arrays.asList(
                        new PassStrategy(),
                        new PassStrategy()
                ))
        );

        Move m = comp.chooseMove(null, PlayerColor.RED);
        assertTrue(m.isPass);
    }

    @Test
    public void testFirstStrategySucceeds() {
        // The first strategy in the list that returns placeMove => we pick that
        Strategy s1 = new PassStrategy(); // pass
        Strategy s2 = new PlaceStrategy(0, 2, 3); // place
        Strategy s3 = new PlaceStrategy(1, 0, 0); // place

        Strategy comp = new CompositeStrategy(Arrays.asList(s1, s2, s3));
        Move m = comp.chooseMove(null, PlayerColor.BLUE);

        assertFalse(m.isPass);
        assertEquals(0, m.handIndex);
        assertEquals(2, m.row);
        assertEquals(3, m.col);
    }

    @Test
    public void testFallbackToThird() {
        // If the first two pass, we pick the third
        Strategy s1 = new PassStrategy();
        Strategy s2 = new PassStrategy();
        Strategy s3 = new PlaceStrategy(2, 4, 4);

        Strategy comp = new CompositeStrategy(Arrays.asList(s1, s2, s3));
        Move m = comp.chooseMove(null, PlayerColor.RED);

        assertFalse(m.isPass);
        assertEquals(2, m.handIndex);
        assertEquals(4, m.row);
        assertEquals(4, m.col);
    }
}
