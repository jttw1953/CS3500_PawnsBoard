package cs3500.pawnsboard.model;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PawnsBoardModelTest {

  @Test
  public void testModelInitialization() {
    List<Card> deck = new ArrayList<>();
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    deck.add(new Card("Guardian", 2, 5, new InfluenceGrid(grid)));

    PawnsBoardModel model = new PawnsBoardModel(3, 5, deck, deck, 2);

    assertEquals(3, model.getRows());
    assertEquals(5, model.getCols());
    assertEquals(PlayerColor.RED, model.getCurrentPlayer());
  }

  @Test
  public void testInitialPawnSetup() {
    List<Card> deck = new ArrayList<>();
    PawnsBoardModel model = new PawnsBoardModel(3, 5, deck, deck, 2);

    assertEquals(CellType.PAWNS, model.getCellState(0, 0).type);
    assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);

    assertEquals(CellType.PAWNS, model.getCellState(0, 4).type);
    assertEquals(PlayerColor.BLUE, model.getCellState(0, 4).owner);
  }

  @Test
  public void testGameOverCondition() {
    List<Card> deck = new ArrayList<>();
    PawnsBoardModel model = new PawnsBoardModel(3, 5, deck, deck, 2);

    model.pass();
    model.pass();

    assertTrue(model.isGameOver());
    assertNull(model.getWinner());
  }
}
