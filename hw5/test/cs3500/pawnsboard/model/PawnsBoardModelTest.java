package cs3500.pawnsboard.model;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for PawnsBoardModel class.
 */
public class PawnsBoardModelTest {

  private PawnsBoardModel model;

  @Before
  public void setup() {
    char[][] influence = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(influence);

    List<Card> smallDeck = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      smallDeck.add(new Card("TestCard" + i, 1, 2, ig));
    }

    model = new PawnsBoardModel(3, 5, smallDeck, smallDeck, 2);
  }

  @Test
  public void testModelInitialization() {
    assertEquals(3, model.getRows());
    assertEquals(5, model.getCols());
    assertEquals(PlayerColor.RED, model.getCurrentPlayer());
  }

  @Test
  public void testInitialPawnSetup() {
    assertEquals(CellType.PAWNS, model.getCellState(0, 0).type);
    assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);

    assertEquals(CellType.PAWNS, model.getCellState(0, 4).type);
    assertEquals(PlayerColor.BLUE, model.getCellState(0, 4).owner);
  }

  @Test
  public void testDrawCardIfPossible() {
    int initialHandSize = model.getCurrentPlayerHand().size();
    model.drawCardIfPossible();
    assertEquals(initialHandSize + 1, model.getCurrentPlayerHand().size());

    model.pass();
    model.drawCardIfPossible();
    assertEquals(3, model.getCurrentPlayerHand().size());
  }

  @Test
  public void testPlaceCardValid() {
    assertEquals(CellType.PAWNS, model.getCellState(0, 0).type);
    assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);
    assertFalse(model.isGameOver());

    model.placeCard(0, 0, 0);

    assertEquals(CellType.CARD, model.getCellState(0, 0).type);
    assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);

    ReadOnlyCell influencedCell = model.getCellState(0, 1);
    assertEquals(CellType.PAWNS, influencedCell.type);
    assertEquals(PlayerColor.RED, influencedCell.owner);
    assertEquals(1, influencedCell.pawnCount);

    assertEquals(PlayerColor.BLUE, model.getCurrentPlayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardNotEnoughPawns() {
    model.placeCard(0, 0, 4);
  }

  @Test
  public void testRowScoreAndTotalScore() {
    for (int r = 0; r < 3; r++) {
      assertEquals(0, model.getRowScore(r, PlayerColor.RED));
      assertEquals(0, model.getRowScore(r, PlayerColor.BLUE));
    }
    assertEquals(0, model.getTotalScore(PlayerColor.RED));
    assertEquals(0, model.getTotalScore(PlayerColor.BLUE));

    model.placeCard(0, 0, 0);

    model.pass(); // BLUE pass => back to RED

    assertEquals(2, model.getRowScore(0, PlayerColor.RED));
    assertEquals(0, model.getRowScore(0, PlayerColor.BLUE));

    assertEquals(2, model.getTotalScore(PlayerColor.RED));
    assertEquals(0, model.getTotalScore(PlayerColor.BLUE));
  }

  @Test
  public void testGameOverCondition() {
    model.pass();
    assertFalse(model.isGameOver());
    model.pass();
    assertTrue(model.isGameOver());
    assertNull(model.getWinner());
  }

  @Test
  public void testGetWinnerRedWins() {
    model.placeCard(0, 0, 0);
    model.pass();
    model.pass();
    model.pass();
    model.pass();

    assertTrue(model.isGameOver());
    assertEquals(PlayerColor.RED, model.getWinner());
  }

  @Test
  public void testBlueInfluenceMirroring() {
    model.pass();

    model.placeCard(0, 0, 4);


    ReadOnlyCell influenced = model.getCellState(0, 3);
    assertEquals(CellType.PAWNS, influenced.type);
    assertEquals(PlayerColor.BLUE, influenced.owner);
    assertEquals(1, influenced.pawnCount);
  }
}
