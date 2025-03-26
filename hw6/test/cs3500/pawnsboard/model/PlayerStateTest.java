package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the PlayerState class in isolation.
 * Ensures correct hand/deck behavior including drawing, 
 * removing, and error handling.
 */
public class PlayerStateTest {

  private PlayerState redState;

  @Before
  public void setup() {
    // Create a small deck of 3 identical cards
    List<Card> smallDeck = new ArrayList<>();
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    for (int i = 1; i <= 3; i++) {
      smallDeck.add(new Card("TestCard" + i, 1, i, ig));
    }

    redState = new PlayerState(PlayerColor.RED, smallDeck);
  }

  /**
   * Tests initial state of the player: color, empty hand, and non-empty deck.
   */
  @Test
  public void testInitialSetup() {
    assertEquals(PlayerColor.RED, redState.getColor());
    assertTrue(redState.hasNextCard());
    assertEquals(0, redState.getHand().size());
  }

  /**
   * Tests drawing one card moves it from the deck to the hand.
   */
  @Test
  public void testDrawCard() {
    redState.drawCard();
    assertEquals(1, redState.getHand().size());
    assertTrue(redState.hasNextCard());
  }

  /**
   * Tests drawing all cards empties the deck and fills the hand.
   * Further draws have no effect.
   */
  @Test
  public void testDrawAllCards() {
    redState.drawCard();
    redState.drawCard();
    redState.drawCard();

    assertEquals(3, redState.getHand().size());
    assertFalse(redState.hasNextCard());

    redState.drawCard(); // no-op
    assertEquals(3, redState.getHand().size());
  }

  /**
   * Tests removing a card from the hand by index.
   */
  @Test
  public void testRemoveCardFromHand() {
    redState.drawCard(); // "TestCard1"
    redState.drawCard(); // "TestCard2"

    assertEquals(2, redState.getHand().size());

    Card removed = redState.removeCardFromHand(0);
    assertEquals("TestCard1", removed.getName());
    assertEquals(1, redState.getHand().size());

    Card inHand = redState.getHand().get(0);
    assertEquals("TestCard2", inHand.getName());
  }

  /**
   * Tests that removing a card with a negative index throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveInvalidIndexNegative() {
    redState.drawCard();
    redState.removeCardFromHand(-1);
  }

  /**
   * Tests that removing a card with an out-of-range index throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveInvalidIndexTooLarge() {
    redState.drawCard();
    redState.removeCardFromHand(999);
  }
}
