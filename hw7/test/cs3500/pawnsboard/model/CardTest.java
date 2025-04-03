package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the Card class.
 * Verifies correct behavior when creating valid and invalid Card instances.
 */
public class CardTest {

  /**
  * Tests creating a valid Card with a proper name, cost, value, and influence grid.
  * Verifies all getter methods return the expected values.
  */
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

  /**
  * Tests that creating a Card with an invalid cost (e.g., zero or negative)
  * throws an IllegalArgumentException.
  */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardCost() {
    new Card("Invalid", 0, 5, null);
  }

  /**
  * Tests that creating a Card with an invalid value (e.g., zero or negative)
  * throws an IllegalArgumentException.
  */ 
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardValue() {
    new Card("Invalid", 2, 0, null);
  }
}
