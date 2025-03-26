package cs3500.pawnsboard.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests various edge cases and invalid operations on PawnsBoardModel,
 * including out-of-bounds access, actions after game over,
 * and effects of placing cards near board edges.
 */
public class EdgeCaseTest {

  private PawnsBoardModel model;

  @Before
  public void setup() {
    // A simple 5x5 influence grid (with center 'C')
    char[][] influenceArr = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(influenceArr);
    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      deck.add(new Card("EdgeCard" + i, 1, 2, ig));
    }
    model = new PawnsBoardModel(3, 3, new ArrayList<>(deck), new ArrayList<>(deck), 1);
  }

  /**
   * Tests placing a card at an invalid (negative) row index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidRow() {
    model.placeCard(0, -1, 1);
  }

  /**
   * Tests placing a card at an invalid column index (beyond board bounds).
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidColumn() {
    model.placeCard(0, 1, 5);
  }

  /**
   * Tests accessing a cell state with an out-of-bounds column index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetCellStateOutOfBounds() {
    model.getCellState(0, -1);
  }

  /**
   * Tests that no new card is drawn after the game is over.
   */
  @Test
  public void testDrawCardAfterGameOver() {
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    int handSizeBefore = model.getCurrentPlayerHand().size();
    model.drawCardIfPossible();
    assertEquals(handSizeBefore, model.getCurrentPlayerHand().size());
  }

  /**
   * Tests that placing a card after the game has ended throws an exception.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlaceCardAfterGameOver() {
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    model.placeCard(0, 0, 0);
  }

  /**
   * Tests placing a card using an invalid hand index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidHandIndex() {
    model.placeCard(10, 0, 0);
  }

  /**
   * Tests that a card placed near the board's edge still applies influence
   * to in-bound cells and safely ignores out-of-bound targets.
   */
  @Test
  public void testInfluenceOffBoard() {
    model.placeCard(0, 0, 0);
    ReadOnlyCell influenced = model.getCellState(0, 1);
    assertEquals(CellType.PAWNS, influenced.type);
    assertEquals(PlayerColor.RED, influenced.owner);
    assertEquals(1, influenced.pawnCount);
  }
}

