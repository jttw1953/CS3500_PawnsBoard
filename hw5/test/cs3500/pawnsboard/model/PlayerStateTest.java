package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the PlayerState class in isolation.
 */
public class PlayerStateTest {

    private PlayerState redState;
    private List<Card> smallDeck;

    @Before
    public void setup() {
        // Create a small deck of 3 identical cards
        smallDeck = new ArrayList<>();
        char[][] grid = {
                {'X','X','I','X','X'},
                {'X','X','I','X','X'},
                {'I','I','C','I','I'},
                {'X','X','I','X','X'},
                {'X','X','I','X','X'}
        };
        InfluenceGrid ig = new InfluenceGrid(grid);
        for (int i = 1; i <= 3; i++) {
            smallDeck.add(new Card("TestCard" + i, 1, i, ig));
        }

        redState = new PlayerState(PlayerColor.RED, smallDeck);
    }

    @Test
    public void testInitialSetup() {
        // The deck should have 3 cards in the queue, the hand is empty initially
        assertEquals(PlayerColor.RED, redState.getColor());
        assertTrue(redState.hasNextCard());
        assertEquals(0, redState.getHand().size());
    }

    @Test
    public void testDrawCard() {
        // Draw the first card => now the hand should have 1 card
        redState.drawCard();
        assertEquals(1, redState.getHand().size());
        // We had 3 in the deck, so 2 remain
        assertTrue(redState.hasNextCard());
    }

    @Test
    public void testDrawAllCards() {
        // Draw 3 times => hand should have all 3, deck empty
        redState.drawCard();
        redState.drawCard();
        redState.drawCard();

        assertEquals(3, redState.getHand().size());
        // Now deck is empty => hasNextCard => false
        assertFalse(redState.hasNextCard());

        // One more draw => does nothing
        redState.drawCard();
        assertEquals(3, redState.getHand().size());
    }

    @Test
    public void testRemoveCardFromHand() {
        // Draw 2 cards
        redState.drawCard(); // "TestCard1"
        redState.drawCard(); // "TestCard2"

        assertEquals(2, redState.getHand().size());

        // Remove index 0 => should remove "TestCard1"
        Card removed = redState.removeCardFromHand(0);
        assertEquals("TestCard1", removed.getName());
        assertEquals(1, redState.getHand().size());

        // Check what's left in the hand
        Card inHand = redState.getHand().get(0);
        assertEquals("TestCard2", inHand.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidIndexNegative() {
        redState.drawCard();
        redState.removeCardFromHand(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidIndexTooLarge() {
        redState.drawCard();
        redState.removeCardFromHand(999);
    }
}
