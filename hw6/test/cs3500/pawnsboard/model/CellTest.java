package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for the Cell class.
 * Verifies correct behavior for initializing and mutating Cell state.
 */
public class CellTest {

  /**
   * Tests that a newly created Cell is initialized with default values:
   * EMPTY type, no owner, no pawns, and no card.
   */
  @Test
  public void testCellInitialization() {
    Cell cell = new Cell();
    assertEquals(CellType.EMPTY, cell.getType());
    assertNull(cell.getOwner());
    assertEquals(0, cell.getPawnCount());
    assertNull(cell.getCard());
  }

  /**
   * Tests setting pawns on a cell updates its type, owner, and pawn count correctly.
   */
  @Test
  public void testSetPawns() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);

    assertEquals(CellType.PAWNS, cell.getType());
    assertEquals(PlayerColor.RED, cell.getOwner());
    assertEquals(2, cell.getPawnCount());
  }

  /**
   * Tests incrementing pawn count, ensuring it is capped at a maximum of 3.
   */
  @Test
  public void testIncrementPawns() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);
    cell.incrementPawns(); // becomes 3
    assertEquals(3, cell.getPawnCount());
    cell.incrementPawns(); // tries to go to 4 => capped at 3
    assertEquals(3, cell.getPawnCount());
  }

  /**
   * Tests that changing a cell's owner keeps the pawn count and type consistent.
   */
  @Test
  public void testChangeOwner() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);
    cell.changeOwner(PlayerColor.BLUE);

    assertEquals(CellType.PAWNS, cell.getType());
    assertEquals(PlayerColor.BLUE, cell.getOwner());
    assertEquals(2, cell.getPawnCount());
  }

  /**
   * Tests placing a card in a cell correctly updates the type to CARD,
   * resets pawn count to 0, and sets the card and owner properly.
   */
  @Test
  public void testSetCard() {
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card card = new Card("Guardian", 2, 5, ig);

    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);
    cell.setCard(PlayerColor.RED, card);

    assertEquals(CellType.CARD, cell.getType());
    assertEquals(PlayerColor.RED, cell.getOwner());
    assertEquals(card, cell.getCard());
    assertEquals(0, cell.getPawnCount());
  }

  /**
   * Tests resetting a cell to an EMPTY state clears owner, card, and pawns.
   */
  @Test
  public void testSetEmpty() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 3);
    cell.setEmpty();
    assertEquals(CellType.EMPTY, cell.getType());
    assertNull(cell.getOwner());
    assertNull(cell.getCard());
    assertEquals(0, cell.getPawnCount());
  }
}

