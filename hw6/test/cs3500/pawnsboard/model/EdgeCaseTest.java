package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Edge cases.
 */
public class EdgeCaseTest {
  private PawnsBoardModel model;

  private final char[][] influenceArr = {
          {'X','X','I','X','X'},
          {'X','X','I','X','X'},
          {'I','I','C','I','I'},
          {'X','X','I','X','X'},
          {'X','X','I','X','X'}
  };

  @Before
  public void setup() {
    InfluenceGrid ig = new InfluenceGrid(influenceArr);
    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      deck.add(new Card("EdgeCard" + i, 1, 2, ig));
    }
    model = new PawnsBoardModel(3, 3, new ArrayList<>(deck),
            new ArrayList<>(deck), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidRow() {
    model.placeCard(0, -1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidColumn() {
    model.placeCard(0, 1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellStateOutOfBounds() {
    model.getCellState(0, -1);
  }

  @Test
  public void testDrawCardAfterGameOver() {
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    int handSizeBefore = model.getCurrentPlayerHand().size();
    model.drawCardIfPossible();
    assertEquals(handSizeBefore, model.getCurrentPlayerHand().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceCardAfterGameOver() {
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    model.placeCard(0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInvalidHandIndex() {
    model.placeCard(10, 0, 0);
  }

  @Test
  public void testInfluenceOffBoard() {
    model.placeCard(0, 0, 0);
    ReadOnlyCell influenced = model.getCellState(0, 1);
    assertEquals(CellType.PAWNS, influenced.type);
    assertEquals(PlayerColor.RED, influenced.owner);
    assertEquals(1, influenced.pawnCount);
  }
}
