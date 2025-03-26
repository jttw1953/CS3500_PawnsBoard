package cs3500.pawnsboard.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PawnsBoardModelTest {

  private List<Card> smallDeck;
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

  // Test: Card influence converts opponent pawn ownership
  @Test
  public void testCardInfluenceConvertsOwnership() {
    // Create a custom influence grid that will influence a cell to the right of the card's placement.
    // The grid has 'C' at (2,2) and an 'I' at (2,3) so that when the card is placed at (1,3),
    // it will influence the board cell at (1,4).
    char[][] customGrid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'I', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(customGrid);
    Card customCard = new Card("TestCard", 1, 1, ig); // cost 1, value 1

    // Create simple decks for Red and Blue (using only the custom card)
    List<Card> redDeck = new ArrayList<>();
    redDeck.add(customCard);
    List<Card> blueDeck = new ArrayList<>();
    blueDeck.add(customCard);

    // Instantiate a board with 3 rows x 5 columns and initial hand size 0 so we can manually add cards.
    PawnsBoardModel model = new PawnsBoardModel(3, 5, redDeck, blueDeck, 0);

    // By default, the leftmost column has Red pawns and the rightmost column has Blue pawns.
    // For this test, ensure cell (1,3) (the placement cell) is set to Red's pawn.
    model.board[1][3].setPawns(PlayerColor.RED, 1);

    // Confirm that cell (1,4) (in the last column) initially has Blue's pawn.
    ReadOnlyCell before = model.getCellState(1, 4);
    assertEquals("Before placement, cell (1,4) should be owned by BLUE", PlayerColor.BLUE, before.owner);

    // Add the custom card to Red's hand so that it can be played.
    // (Accessing redState directly is acceptable in tests within the same package.)
    model.redState.getHand().add(customCard);

    // Red places the card from hand index 0 at cell (1,3). This should apply influence,
    // converting the pawns at (1,4) from Blue to Red.
    model.placeCard(0, 1, 3);

    // After the move, cell (1,4) should now have its ownership converted to Red.
    ReadOnlyCell after = model.getCellState(1, 4);
    assertEquals("After placement, cell (1,4) should be owned by RED", PlayerColor.RED, after.owner);
  }

  // Test: Out-of-bounds card placement is prevented
  @Test
  public void testPlaceCardOutOfBounds() {
    // Set up a simple influence grid for our test card.
    char[][] grid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card testCard = new Card("TestCard", 1, 1, ig);

    // Create simple decks containing the test card for both players.
    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();
    redDeck.add(testCard);
    blueDeck.add(testCard);

    // Create a board with 3 rows and 5 columns and an initial hand size of 0.
    // (We will add the card manually to Red's hand.)
    PawnsBoardModel model = new PawnsBoardModel(3, 5, redDeck, blueDeck, 0);

    // Add the test card to Red's hand so that there is a card available for play.
    model.redState.getHand().add(testCard);

    // Set cell (1,0) to be a valid placement cell with enough pawns for Red.
    model.board[1][0].setPawns(PlayerColor.RED, 1);

    // Attempt to place the card at an out-of-bounds row (-1,2).
    assertThrows(IllegalArgumentException.class, () -> {
      model.placeCard(0, -1, 2);
    });

    // Attempt to place the card at an out-of-bounds column (1,5).
    assertThrows(IllegalArgumentException.class, () -> {
      model.placeCard(0, 1, 5);
    });
  }

  // Test: Model switches turns so that a player cannot play out-of-turn
  @Test
  public void testTurnSwitchingAndOutOfTurnPrevention() {
    // Create a simple influence grid for our test card (no influence needed for this test)
    char[][] grid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card testCard = new Card("SimpleCard", 1, 1, ig);

    // Create decks for Red and Blue each with one test card.
    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();
    redDeck.add(testCard);
    blueDeck.add(testCard);

    // Create a board with 3 rows and 5 columns, with initial hand size 0 for manual control.
    PawnsBoardModel model = new PawnsBoardModel(3, 5, redDeck, blueDeck, 0);

    // Initially, current player should be RED.
    assertEquals("Initial current player should be RED", PlayerColor.RED, model.getCurrentPlayer());

    // For a valid move, ensure cell (1,0) is set to have RED's pawn.
    model.board[1][0].setPawns(PlayerColor.RED, 1);

    // Add the test card to Red's hand.
    model.redState.getHand().add(testCard);

    // Red places the card at (1,0). This move is valid.
    model.placeCard(0, 1, 0);

    // After a valid move, the turn should switch to BLUE.
    assertEquals("After RED's move, current player should be BLUE", PlayerColor.BLUE, model.getCurrentPlayer());

    // Attempt to have Red (by using Red's hand) place another card on a cell that belongs to RED.
    // Since it's now BLUE's turn, this should throw an exception.
    model.redState.getHand().add(testCard);
    assertThrows(IllegalArgumentException.class, () -> {
      // Attempt to place on a cell with RED's pawns while current player is BLUE.
      model.placeCard(0, 1, 0);
    });
  }

  // Test: Valid and invalid move checking using canPlaceCard
  @Test
  public void testCanPlaceCard() {
    // Create a simple influence grid for our test card.
    char[][] grid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card testCard = new Card("TestCard", 1, 1, ig);

    // Create decks for both players with the test card.
    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();
    redDeck.add(testCard);
    blueDeck.add(testCard);

    // Create a board with 3 rows x 5 columns, with initial hand size 0.
    PawnsBoardModel model = new PawnsBoardModel(3, 5, redDeck, blueDeck, 0);

    // Add the test card to Red's hand.
    model.redState.getHand().add(testCard);

    // Valid placement: by default, leftmost column cells have RED pawns.
    // For example, cell (1,0) should be valid.
    assertTrue("Red should be able to place card at (1,0)",
            model.canPlaceCard(0, 1, 0));

    // Invalid: out-of-bounds row.
    assertFalse("Placement with row -1 should return false",
            model.canPlaceCard(0, -1, 0));

    // Invalid: out-of-bounds column.
    assertFalse("Placement with column 5 (out-of-bound) should return false",
            model.canPlaceCard(0, 1, 5));

    // Invalid: cell is not in PAWNS state.
    // Set cell (1,1) to be empty.
    model.board[1][1].setEmpty();
    assertFalse("Placement on an empty cell should return false",
            model.canPlaceCard(0, 1, 1));

    // Invalid: insufficient pawns.
    // Set cell (1,2) to have 0 pawns (illegal move since cost is 1).
    model.board[1][2].setPawns(PlayerColor.RED, 0);
    assertFalse("Placement on cell with insufficient pawns should return false",
            model.canPlaceCard(0, 1, 2));
  }

  // Test: copyModel creates an independent duplicate of the board state
  @Test
  public void testCopyModelIndependence() {
    // Create a simple influence grid for our test card.
    char[][] grid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    Card testCard = new Card("TestCard", 1, 1, ig);

    // Create decks.
    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();
    redDeck.add(testCard);
    blueDeck.add(testCard);

    // Create a board with 3 rows x 5 columns and initial hand size 0.
    PawnsBoardModel original = new PawnsBoardModel(3, 5, redDeck, blueDeck, 0);

    // Add a card to Red's hand and set a valid cell.
    original.redState.getHand().add(testCard);
    // Ensure cell (1,0) is a valid placement cell (it already has RED's pawn by default).
    original.board[1][0].setPawns(PlayerColor.RED, 1);

    // Red places a card at (1,0) to modify the board state.
    original.placeCard(0, 1, 0);

    // Create a copy of the current model.
    IPawnsBoardModel copy = original.copyModel();

    // Verify that a specific cell's state is the same in both original and copy.
    ReadOnlyCell originalCell = original.getCellState(1, 0);
    ReadOnlyCell copyCell = copy.getCellState(1, 0);
    assertEquals("Copied model should have the same cell type", originalCell.type, copyCell.type);
    assertEquals("Copied model should have the same owner", originalCell.owner, copyCell.owner);
    assertEquals("Copied model should have the same pawn count", originalCell.pawnCount, copyCell.pawnCount);

    // Modify the original: change cell (1,1) to have 2 RED pawns.
    original.board[1][1].setPawns(PlayerColor.RED, 2);

    // Check that the copy's corresponding cell remains unaffected.
    ReadOnlyCell modifiedOriginal = original.getCellState(1, 1);
    ReadOnlyCell copyAfterModification = copy.getCellState(1, 1);
    // Since the copy was made before the modification, the pawn count should differ.
    assertNotEquals("After modifying original, copy should remain unchanged in cell (1,1)",
            modifiedOriginal.pawnCount, copyAfterModification.pawnCount);
  }


}
