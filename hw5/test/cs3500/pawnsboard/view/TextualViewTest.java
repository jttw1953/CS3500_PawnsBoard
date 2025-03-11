package cs3500.pawnsboard.view;

import cs3500.Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the TextualView's output under various game states.
 */
public class TextualViewTest {
    private IPawnsBoardModel model;
    private TextualView view;

    @Before
    public void setup() {
        // Build a minimal deck
        char[][] grid = {
                {'X','X','I','X','X'},
                {'X','X','I','X','X'},
                {'I','I','C','I','I'},
                {'X','X','I','X','X'},
                {'X','X','I','X','X'}
        };
        InfluenceGrid ig = new InfluenceGrid(grid);
        Card card1 = new Card("C1", 1, 2, ig);
        Card card2 = new Card("C2", 1, 3, ig);

        // Enough deck for both players
        List<Card> deckRed = List.of(card1, card1, card1);
        List<Card> deckBlue = List.of(card2, card2, card2);

        // 3x5 board, each player has initial hand size=1
        model = new PawnsBoardModel(3, 5, deckRed, deckBlue, 1);
        view = new TextualView(model);
    }

    @Test
    public void testInitialBoardView() {
        // Right after creation, each row => col=0 has RED pawn '1', col=4 has BLUE pawn '1', others empty '_'
        // Also row-scores = 0, since no cards placed.

        String output = view.toString();
        // We expect "Current Player: RED" because Red starts
        assertTrue(output.contains("Current Player: RED"));
        // Each row should look like:
        // row 0 => "1___1  (R=0, B=0)"
        // row 1 => "1___1  (R=0, B=0)"
        // row 2 => "1___1  (R=0, B=0)"
        // Then "Running totals => RED: 0 | BLUE: 0"

        // Quick check:
        assertTrue(output.contains("1___1  (R=0, B=0)"));
        assertTrue(output.contains("Running totals => RED: 0 | BLUE: 0"));
        assertFalse(output.contains("GAME OVER"));
    }

    @Test
    public void testMidGameViewAfterPlacingCard() {
        // Red has 1 card in hand => cost=1 => can place at (0,0).
        model.placeCard(0, 0, 0);
        // Now Blue turn
        String output = view.toString();

        // Check "Current Player: BLUE"
        assertTrue(output.contains("Current Player: BLUE"));

        // Row 0 => 'R' at col=0 now, underscores in the middle, '1' at col=4
        // e.g. "R___1  (R=2, B=0)" because that card has value=2
        // Just check that row 0 starts with "R" and ends with "1" and that R=2 in the row-scores.
        assertTrue(output.contains("R") && output.contains("1") && output.contains("(R=2, B=0)"));

    }

    @Test
    public void testViewWhenGameOver() {
        // Force the game to end with consecutive passes
        model.pass(); // Now BLUE
        model.pass(); // gameOver = true

        // Re-generate the string
        String output = view.toString();
        // We expect "GAME OVER" plus final board layout lines, "Final totals => ..." and "It's a tie!"
        assertTrue(output.contains("GAME OVER"));
        assertTrue(output.contains("Final totals => RED: 0 | BLUE: 0"));
        assertTrue(output.contains("It's a tie!"));
    }
}
