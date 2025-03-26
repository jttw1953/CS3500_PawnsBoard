package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EdgeCaseTest {
  // A simple 5x5 influence grid (with center 'C')
  private final char[][] influenceArr = {
          {'X', 'X', 'I', 'X', 'X'},
          {'X', 'X', 'I', 'X', 'X'},
          {'I', 'I', 'C', 'I', 'I'},
          {'X', 'X', 'I', 'X', 'X'},
          {'X', 'X', 'I', 'X', 'X'}
  };
  private List<Card> deck;
  private PawnsBoardModel model;
  private InfluenceGrid ig;

  @Before
  public void setup() {
    ig = new InfluenceGrid(influenceArr);
    deck = new ArrayList<>();
    // Create a deck with cards that have cost 1 (so they can be placed on a cell with 1 pawn)
    for (int i = 0; i < 5; i++) {
      deck.add(new Card("EdgeCard" + i, 1, 2, ig));
    }
    // Create a small board: 3 rows x 3 cols.
    // Note: Board initialization places RED pawns in col 0 and BLUE pawns in col (cols-1).
    model = new PawnsBoardModel(3, 3, new ArrayList<>(deck), new ArrayList<>(deck), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidRow() {
    // Row index -1 is out-of-bounds.
    model.placeCard(0, -1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidColumn() {
    // Column index 5 (for a board with 3 columns) is out-of-bounds.
    model.placeCard(0, 1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellStateOutOfBounds() {
    // Negative column index should cause an exception.
    model.getCellState(0, -1);
  }

  @Test
  public void testDrawCardAfterGameOver() {
    // Force game over by two consecutive passes.
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    // Capture the current player's hand size.
    int handSizeBefore = model.getCurrentPlayerHand().size();
    // drawCardIfPossible should do nothing after game over.
    model.drawCardIfPossible();
    assertEquals(handSizeBefore, model.getCurrentPlayerHand().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceCardAfterGameOver() {
    // End the game.
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    // Attempting to place a card after the game is over should throw an exception.
    model.placeCard(0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidHandIndex() {
    // Attempt to place a card using an invalid hand index.
    // Since the hand size is small, index 10 is out-of-range.
    model.placeCard(10, 0, 0);
  }

  @Test
  public void testInfluenceOffBoard() {
    // Place a card in the upper-left corner (0,0).
    // Initial state: cell (0,0) has a RED pawn (so it's valid for RED).
    model.placeCard(0, 0, 0);
    // The card is placed at (0,0) so its influence grid (centered at [2,2]) will compute
    // board coordinates using (row-2, col-2) offsets. Some of these will be off-board.
    // Verify that an in-bound cell (for example, (0,1) computed from grid cell [2,3]) is updated.
    ReadOnlyCell influenced = model.getCellState(0, 1);
    assertEquals(CellType.PAWNS, influenced.type);
    assertEquals(PlayerColor.RED, influenced.owner);
    assertEquals(1, influenced.pawnCount);
  }
}
