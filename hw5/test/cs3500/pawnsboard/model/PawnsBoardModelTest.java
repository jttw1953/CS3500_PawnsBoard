package cs3500.pawnsboard.model;

import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PawnsBoardModelTest {

    private List<Card> smallDeck;
    private PawnsBoardModel model;

    @Before
    public void setup() {
        // A simple 5x5 influence for demonstration
        // Center = C at [2][2], 'I' in a cross shape
        char[][] influence = {
                {'X','X','I','X','X'},
                {'X','X','I','X','X'},
                {'I','I','C','I','I'},
                {'X','X','I','X','X'},
                {'X','X','I','X','X'}
        };
        InfluenceGrid ig = new InfluenceGrid(influence);

        // Build a small deck of 4 identical cards:
        smallDeck = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            smallDeck.add(new Card("TestCard" + i, 1, 2, ig));
        }

        // Create a 3x5 board, each player gets the same small deck, with initialHandSize=2
        model = new PawnsBoardModel(3, 5, smallDeck, smallDeck, 2);
    }

    @Test
    public void testModelInitialization() {
        // from your existing test
        assertEquals(3, model.getRows());
        assertEquals(5, model.getCols());
        assertEquals(PlayerColor.RED, model.getCurrentPlayer());
    }

    @Test
    public void testInitialPawnSetup() {
        // from your existing test
        assertEquals(CellType.PAWNS, model.getCellState(0, 0).type);
        assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);

        assertEquals(CellType.PAWNS, model.getCellState(0, 4).type);
        assertEquals(PlayerColor.BLUE, model.getCellState(0, 4).owner);
    }

    @Test
    public void testDrawCardIfPossible() {
        // Current player is RED. Already has 2 cards in hand. The deck has 2 more cards left.
        // Draw one card => hand should go from size 2 to 3
        int initialHandSize = model.getCurrentPlayerHand().size();
        model.drawCardIfPossible();
        assertEquals(initialHandSize + 1, model.getCurrentPlayerHand().size());

        // Switch to BLUE turn: place a pass or something to switch player
        model.pass(); // now BLUE
        // BLUE also has 2 in hand initially, draws 1 => size=3
        model.drawCardIfPossible();
        assertEquals(3, model.getCurrentPlayerHand().size());
    }

    @Test
    public void testPlaceCardValid() {
        // RED has 1 pawn at (0,0). Our card cost is 1. So we can place a card there.
        // If successful, that cell becomes a card, and influence is applied around it.

        // Before place:
        assertEquals(CellType.PAWNS, model.getCellState(0, 0).type);
        assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);
        assertFalse(model.isGameOver());

        // place card from hand index 0 at (0,0)
        model.placeCard(0, 0, 0);

        // Now (0,0) should be a CARD, belonging to RED
        assertEquals(CellType.CARD, model.getCellState(0, 0).type);
        assertEquals(PlayerColor.RED, model.getCellState(0, 0).owner);

        // Check influence around (0,0):
        // Our influence shape tries to add pawns to (0,1), (0,-1), (1,0), (-1,0) etc.
        // But negative or out-of-bounds is ignored.
        // We do expect (0,1) to become a RED pawn if it's in bounds,
        // for row=0, col=1 => previously empty? Actually, it might be empty or also have blue pawns if it's the last col.
        // In a 3x5 board, (0,1) was empty => should now have 1 red pawn.
        ReadOnlyCell influencedCell = model.getCellState(0, 1);
        assertEquals(CellType.PAWNS, influencedCell.type);
        assertEquals(PlayerColor.RED, influencedCell.owner);
        assertEquals(1, influencedCell.pawnCount);

        // Turn should switch to BLUE
        assertEquals(PlayerColor.BLUE, model.getCurrentPlayer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceCardNotEnoughPawns() {
        // If we try to place a cost=1 card in a cell that belongs to BLUE, or a cell that has 0 pawns, it should fail.
        // Let’s try to place a card at (0,4) right away => that is BLUE's cell (with 1 pawn).
        // But the current player is RED => not owner => should fail.
        model.placeCard(0, 0, 4);
    }

    @Test
    public void testRowScoreAndTotalScore() {
        // Initially, no cards => row-scores all 0 => total score = 0
        for (int r = 0; r < 3; r++) {
            assertEquals(0, model.getRowScore(r, PlayerColor.RED));
            assertEquals(0, model.getRowScore(r, PlayerColor.BLUE));
        }
        assertEquals(0, model.getTotalScore(PlayerColor.RED));
        assertEquals(0, model.getTotalScore(PlayerColor.BLUE));

        // Place a RED card in (0,0) => cost=1 => card value=2
        model.placeCard(0, 0, 0);

        // Switch => now BLUE. Pass to switch back to RED for convenience
        model.pass(); // BLUE pass => back to RED

        // Now the row-scores for row 0 => RED should have 2, BLUE 0
        assertEquals(2, model.getRowScore(0, PlayerColor.RED));
        assertEquals(0, model.getRowScore(0, PlayerColor.BLUE));

        // total score => we check each row. row 0 => RED has 2 vs BLUE=0 => RED wins that row => +2
        // row 1 => tie at 0 => no points. row 2 => tie => 0
        assertEquals(2, model.getTotalScore(PlayerColor.RED));
        assertEquals(0, model.getTotalScore(PlayerColor.BLUE));
    }

    @Test
    public void testGameOverCondition() {
        // from your existing test, plus some checks
        model.pass(); // RED pass => not over yet
        assertFalse(model.isGameOver());
        model.pass(); // BLUE pass => now game over
        assertTrue(model.isGameOver());
        // No winner => total scores are 0,0 => tie => getWinner() => null
        assertNull(model.getWinner());
    }

    @Test
    public void testGetWinnerRedWins() {
        // We’ll place a card for RED => pass => place a card for BLUE => pass => see who wins
        // Or forcibly pass to end.

        // Place a RED card in row=0 => value=2
        model.placeCard(0, 0, 0);
        // Switch => BLUE
        model.pass(); // switch => RED
        model.pass(); // switch => BLUE, pass again => ??? Actually let's make BLUE place something…

        // Actually let's have BLUE also place a card in row=0 => also value=2 => tie in that row => 0 points
        // But if we skip that, then row0 => RED=2, BLUE=0 => Red wins row0 => red total=2
        // row1 => tie(0,0), row2 => tie(0,0)
        // final => red=2, blue=0 => red is winner

        // Let's do it quickly:
        // Now currentPlayer is RED (since BLUE passed once, we are back to RED).
        model.pass(); // red passes => switch => BLUE
        model.pass(); // now 2 consecutive passes => game over

        // RED has 2 points, BLUE has 0
        assertTrue(model.isGameOver());
        assertEquals(PlayerColor.RED, model.getWinner());
    }

    @Test
    public void testBlueInfluenceMirroring() {
        // Let's forcibly end Red's turn quickly, so Blue can place a card.
        // Start => current=RED
        model.pass();  // Now BLUE

        // Now BLUE tries to place a card in (0,4) => cost=1 => valid
        model.placeCard(0, 0, 4);

        // That cell becomes a BLUE card. Check influence is mirrored horizontally.
        // The card's normal influence from the center (2,2) => (2,3), (2,1), etc.
        // But for BLUE, we flip column deltas => effectively "I" squares go to the left of (0,4) if in range.

        // For example, the square (2,3) in the influence grid (row=2 col=3 => dc=1)
        // becomes column offset = -1 from center for BLUE => (0,4 - 1) => (0,3).
        // So (0,3) should now have 1 BLUE pawn if it was empty.

        ReadOnlyCell influenced = model.getCellState(0, 3);
        assertEquals(CellType.PAWNS, influenced.type);
        assertEquals(PlayerColor.BLUE, influenced.owner);
        assertEquals(1, influenced.pawnCount);
    }
}
