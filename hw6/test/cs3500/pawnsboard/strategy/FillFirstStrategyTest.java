package cs3500.pawnsboard.strategy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.CellType;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyCell;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the FillFirstStrategy.
 */
public class FillFirstStrategyTest {

  @Test
  public void testNoValidMovesLeadsToPass() {
    // Mock a scenario where current player is RED, has 2 cards but no cell belongs to RED, 
    // or not enough pawns
    List<Card> hand = new ArrayList<>();
    hand.add(new Card("C1", 1, 2, null)); // cost=1, value=2
    hand.add(new Card("C2", 1, 3, null));

    // Board 2x2, all cells are empty or belong to BLUE
    ReadOnlyCell[][] cells = new ReadOnlyCell[2][2];
    cells[0][0] = new SimpleCell(CellType.PAWNS, PlayerColor.BLUE, 1, "");
    cells[0][1] = new SimpleCell(CellType.EMPTY, null, 0, "");
    cells[1][0] = new SimpleCell(CellType.PAWNS, PlayerColor.BLUE, 3, "");
    cells[1][1] = new SimpleCell(CellType.EMPTY, null, 0, "");

    MockModel mock = new MockModel(hand, 2, 2, PlayerColor.RED, cells);

    Strategy strat = new FillFirstStrategy();
    Move move = strat.chooseMove(mock, PlayerColor.RED);
    // Should be a pass
    assertTrue(move.isPass);
  }

  @Test
  public void testFindsFirstValidCell() {
    // We want to ensure it picks row-major order, the first cell it sees that's valid
    // Suppose we have 2 cards, both cost=1, and a 2x2 board
    // We'll mark (0,1) as RED pawns=1, (1,1) as RED pawns=1 too
    List<Card> hand = new ArrayList<>();
    hand.add(new Card("C1", 1, 2, null));
    hand.add(new Card("C2", 1, 3, null));

    ReadOnlyCell[][] cells = new ReadOnlyCell[2][2];
    cells[0][0] = new SimpleCell(CellType.EMPTY, null, 0, "");
    cells[0][1] = new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, "");
    cells[1][0] = new SimpleCell(CellType.PAWNS, PlayerColor.RED, 1, "");
    cells[1][1] = new SimpleCell(CellType.EMPTY, null, 0, "");

    MockModel mock = new MockModel(hand, 2, 2, PlayerColor.RED, cells);

    Strategy strat = new FillFirstStrategy();
    Move move = strat.chooseMove(mock, PlayerColor.RED);

    // Fill-first => it scans from row=0 col=0 => sees empty => 
    // not valid => row=0 col=1 => valid => picks that
    assertFalse(move.isPass);
    assertEquals(0, move.handIndex); // the first card in hand
    assertEquals(0, move.row);
    assertEquals(1, move.col);
  }

  // A trivial mock model that we can control for the test.
  private static class MockModel implements ReadOnlyPawnsBoardModel {
    // We'll store a hand, a board dimension, and some mock cells
    private final List<Card> hand;
    private final int rows;
    private final int cols;
    private final PlayerColor current;

    // We'll store cell states in a 2D array
    private final ReadOnlyCell[][] cells;

    MockModel(List<Card> hand, int rows, int cols, PlayerColor current, ReadOnlyCell[][] cells) {
      this.hand = hand;
      this.rows = rows;
      this.cols = cols;
      this.current = current;
      this.cells = cells;
    }

    @Override
    public int getRows() {
      return rows;
    }

    @Override
    public int getCols() {
      return cols;
    }

    @Override
    public PlayerColor getCurrentPlayer() {
      return current;
    }

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public PlayerColor getWinner() {
      return null;
    }

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
      return 0;
    }

    @Override
    public int getTotalScore(PlayerColor color) {
      return 0;
    }

    @Override
    public boolean canPlaceCard(int handIndex, int row, int col) {
      return false;
    }

    @Override
    public int getDeckSize(PlayerColor color) {
      return 0;
    }
  }

  // Another helper for read-only cell
  private static class SimpleCell extends ReadOnlyCell {
    public SimpleCell(CellType type, PlayerColor owner, int pawnCount, String cardName) {
      super(type, owner, pawnCount, cardName);
    }
  }
}
