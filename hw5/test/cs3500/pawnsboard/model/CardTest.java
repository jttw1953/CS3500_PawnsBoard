package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

  @Test
  public void testValidCardCreation() {
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card card = new Card("Guardian", 2, 5, ig);

    assertEquals("Guardian", card.getName());
    assertEquals(2, card.getCost());
    assertEquals(5, card.getValue());
    assertNotNull(card.getInfluenceGrid());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardCost() {
    new Card("Invalid", 0, 5, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardValue() {
    new Card("Invalid", 2, 0, null);
  }
}
