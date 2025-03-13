package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CellTest {

  @Test
  public void testCellInitialization() {
    Cell cell = new Cell();
    assertEquals(CellType.EMPTY, cell.getType());
    assertNull(cell.getOwner());
    assertEquals(0, cell.getPawnCount());
    assertNull(cell.getCard());
  }

  @Test
  public void testSetPawns() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);

    assertEquals(CellType.PAWNS, cell.getType());
    assertEquals(PlayerColor.RED, cell.getOwner());
    assertEquals(2, cell.getPawnCount());
  }

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

  @Test
  public void testChangeOwner() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);
    cell.changeOwner(PlayerColor.BLUE);

    assertEquals(CellType.PAWNS, cell.getType());
    assertEquals(PlayerColor.BLUE, cell.getOwner());
    assertEquals(2, cell.getPawnCount());
  }

  @Test
  public void testIncrementPawns() {
    Cell cell = new Cell();
    cell.setPawns(PlayerColor.RED, 2);
    cell.incrementPawns(); // becomes 3
    assertEquals(3, cell.getPawnCount());
    cell.incrementPawns(); // tries to go to 4 => capped at 3
    assertEquals(3, cell.getPawnCount());
  }
  
}
