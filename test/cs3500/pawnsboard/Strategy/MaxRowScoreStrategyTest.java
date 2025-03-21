package cs3500.pawnsboard.Strategy;

import cs3500.pawnsboard.Strategy.Strategy;
import cs3500.pawnsboard.Strategy.MaxRowScoreStrategy;
import cs3500.pawnsboard.Strategy.Move;
import cs3500.pawnsboard.Model.Card;
import cs3500.pawnsboard.Model.CellType;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Model.ReadOnlyCell;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tests for the MaxRowScoreStrategy.
 */
public class MaxRowScoreStrategyTest {

    // Minimal read-only cell
    private static class SimpleCell extends ReadOnlyCell {
        public SimpleCell(CellType type, PlayerColor owner, int pawnCount, String cardName) {
            super(type, owner, pawnCount, cardName);
        }
    }

    // Minimal mock
    private static class MockRowScoreModel implements ReadOnlyPawnsBoardModel {
        private final int rows;
        private final int cols;
        private final PlayerColor current;
        private final List<Card> hand;
        private final ReadOnlyCell[][] cells;
        private final int[] redRowScores;
        private final int[] blueRowScores;

        public MockRowScoreModel(int rows, int cols, PlayerColor current,
                                 List<Card> hand, ReadOnlyCell[][] cells,
                                 int[] redRowScores, int[] blueRowScores) {
            this.rows = rows;
            this.cols = cols;
            this.current = current;
            this.hand = hand;
            this.cells = cells;
            this.redRowScores = redRowScores;
            this.blueRowScores = blueRowScores;
        }

        @Override
        public int getRows() { return rows; }
        @Override
        public int getCols() { return cols; }
        @Override
        public PlayerColor getCurrentPlayer() { return current; }
        @Override
        public boolean isGameOver() { return false; }
        @Override
        public PlayerColor getWinner() { return null; }
        @Override
        public ReadOnlyCell getCellState(int row, int col) {
            return cells[row][col];
        }
        @Override
        public List<Card> getCurrentPlayerHand() {
            return Collections.unmodifiableList(hand);
        }
        @Override
        public int getRowScore(int row, PlayerColor color) {
            return (color == PlayerColor.RED) ? redRowScores[row] : blueRowScores[row];
        }
        @Override
        public int getTotalScore(PlayerColor color) {
            return 0; // not used here
        }
    }

    @Test
    public void testNoRowsLosingOrTied() {
        // Suppose 2 rows, RED is already winning them all => strategy does not need to do anything => pass
        int[] redScores = {3, 4};
        int[] blueScores = {0, 1};

        // The model => 2x2 board, row0 col0 belongs to RED with 1 pawn
        ReadOnlyCell[][] cells = {
                { new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, ""), new SimpleCell(CellType.EMPTY, null, 0, "") },
                { new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, ""), new SimpleCell(CellType.EMPTY, null, 0, "") }
        };

        List<Card> hand = new ArrayList<>();
        hand.add(new Card("C1", 1, 2, null));

        MockRowScoreModel mock = new MockRowScoreModel(
                2, 2, PlayerColor.RED, hand, cells, redScores, blueScores
        );

        Strategy strat = new MaxRowScoreStrategy();
        Move m = strat.chooseMove(mock, PlayerColor.RED);
        assertTrue(m.isPass); // no row is losing or tied, so pass
    }

    @Test
    public void testTiedRowGetsFixed() {
        // Suppose row 0 => RED=2, BLUE=2 => tie
        // row 1 => RED=4, BLUE=3 => Red is winning
        int[] redScores = {2, 4};
        int[] blueScores = {2, 3};

        // We'll have a board 2x2, row0 col0 => RED pawns=1, so we can place a cost=1 card there
        ReadOnlyCell[][] cells = {
                { new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, ""), new SimpleCell(CellType.EMPTY, null, 0, "") },
                { new SimpleCell(CellType.EMPTY, null, 0, ""),            new SimpleCell(CellType.EMPTY, null, 0, "") }
        };

        List<Card> hand = new ArrayList<>();
        hand.add(new Card("Tiebreaker", 1, 2, null)); // cost=1, value=2 => helps us

        MockRowScoreModel mock = new MockRowScoreModel(
                2, 2, PlayerColor.RED, hand, cells, redScores, blueScores
        );

        Strategy strat = new MaxRowScoreStrategy();
        Move m = strat.chooseMove(mock, PlayerColor.RED);

        // We expect the strategy to place a card in row=0 => so that Red can exceed Blue
        assertFalse(m.isPass);
        assertEquals(0, m.handIndex);
        assertEquals(0, m.row);
        assertEquals(0, m.col);
    }

    @Test
    public void testMultipleRowsTied() {
        // 3 rows => row0 => tie, row1 => losing, row2 => winning
        int[] redScores = {2, 1, 4};
        int[] blueScores = {2, 2, 3};

        // We'll have a 3x2 board => row0 col0 belongs to RED with 1 pawn
        ReadOnlyCell[][] cells = {
                {
                        new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, ""),
                        new SimpleCell(CellType.EMPTY, null, 0, "")
                },
                {
                        new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, ""),
                        new SimpleCell(CellType.EMPTY, null, 0, "")
                },
                {
                        new SimpleCell(CellType.EMPTY, null, 0, ""),
                        new SimpleCell(CellType.EMPTY, null, 0, "")
                }
        };

        // One card: cost=1, value=2 => enough to help
        List<Card> hand = new ArrayList<>();
        hand.add(new Card("Helper", 1, 2, null));

        MockRowScoreModel mock = new MockRowScoreModel(
                3, 2, PlayerColor.RED, hand, cells, redScores, blueScores
        );
        Strategy strat = new MaxRowScoreStrategy();
        Move m = strat.chooseMove(mock, PlayerColor.RED);

        // The strategy checks row 0 => sees tie => tries to place => valid => done
        assertFalse(m.isPass);
        assertEquals(0, m.row);
        assertEquals(0, m.col);
    }
}
