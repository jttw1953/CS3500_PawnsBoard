package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EnhancedPawnsBoardModelTest {

    private EnhancedPawnsBoardModel model;
    private Card upgradeCard;
    private Card devalueCard;
    private Card normalCard;

    @Before
    public void setUp() {
        // U-only grid: upgrade the cell to the right of placement
        char[][] uGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','U','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        upgradeCard = new Card("Upgrade", 1, 1, new InfluenceGrid(uGrid));

        // D-only grid: devalue the cell to the right of placement
        char[][] dGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','D','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        devalueCard = new Card("Devalue", 1, 1, new InfluenceGrid(dGrid));

        // Normal card: no U/D influences
        char[][] iGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','X','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        normalCard = new Card("Normal", 1, 5, new InfluenceGrid(iGrid));

        // Build decks and model with no initial hand draws
        List<Card> deck = List.of(upgradeCard, devalueCard, normalCard);
        model = new EnhancedPawnsBoardModel(3, 5, deck, deck, 0);

        // Give Red all three cards
        model.redState.getHand().add(upgradeCard);
        model.redState.getHand().add(devalueCard);
        model.redState.getHand().add(normalCard);

        // Prepare a pawn cell for Red at (1,1)
        model.board[1][1].setPawns(PlayerColor.RED, 1);
    }

    @Test
    public void testUpgradeInfluenceIncrementsModifier() {
        model.placeCard(0, 1, 1);
        int mod = model.valueModifiers[1][2];
        assertEquals(1, mod);
    }

    @Test
    public void testDevalueInfluenceDecrementsModifier() {
        model.placeCard(1, 1, 1);
        int mod = model.valueModifiers[1][2];
        assertEquals(-1, mod);
    }

    @Test
    public void testStackingUpgradeAndDevalue() {
        // Directly apply an upgrade then a devalue at (1,1):
        model.rawApplyInfluence(1, 1, upgradeCard);  // +1 at (1,2)
        model.rawApplyInfluence(1, 1, devalueCard);  // –1 at (1,2)

        // Net modifier should be 0
        assertEquals("Upgrade then devalue should net out to 0",
                0, model.valueModifiers[1][2]);
    }


    @Test
    public void testEffectiveValueRemovalWhenNonPositive() {
        // Stack two devalues via rawApplyInfluence
        model.rawApplyInfluence(1, 1, devalueCard); // -1
        model.rawApplyInfluence(1, 1, devalueCard); // -2

        // Place a normal card (base value 5) at (1,2) to confirm eff>0
        model.board[1][2].setPawns(PlayerColor.RED, 5);
        model.redState.getHand().add(normalCard);
        int beforeMod = model.valueModifiers[1][2];
        model.placeCard(model.redState.getHand().size() - 1, 1, 2);
        assertEquals("Modifier unchanged when eff>0", beforeMod, model.valueModifiers[1][2]);
        assertEquals("Cell should be CARD when eff>0", CellType.CARD, model.getCellState(1, 2).type);
        dummyBlueMove();

        // Now stack devalues via rawApplyInfluence until effective <= 0
        int needed = 5 + model.valueModifiers[1][2];
        for (int i = 0; i < needed; i++) {
            model.rawApplyInfluence(1, 1, devalueCard);
        }

        // Prepare cell for removal: reset to PAWNS
        model.board[1][2].setPawns(PlayerColor.RED, normalCard.getCost());
        model.redState.getHand().add(normalCard);

        // Place to trigger removal
        model.placeCard(model.redState.getHand().size() - 1, 1, 2);
        ReadOnlyCell cell = model.getCellState(1, 2);
        assertEquals("Cell should revert to PAWNS when eff<=0", CellType.PAWNS, cell.type);
        assertEquals("Pawn count should equal cost (1)", 1, cell.pawnCount);
        assertEquals("Modifier resets to 0 after removal", 0, model.valueModifiers[1][2]);
    }

    /**
     * Blue plays a dummy card so turns switch without passing out.
     */
    private void dummyBlueMove() {
        model.blueState.getHand().add(normalCard);
        int row = 1, col = model.getCols() - 1;
        model.placeCard(model.blueState.getHand().size() - 1, row, col);
    }


    // Blue U/D mirroring
    @Test
    public void testBlueUpgradeMirrorsHorizontally() {
        // make it Blue's turn
        model.pass();

        // Give Blue an upgrade card and a pawn stack at (1,3)
        model.blueState.getHand().add(upgradeCard);
        model.board[1][3].setPawns(PlayerColor.BLUE, 1);

        // Place card, whose U is one column *right* in grid (dc = +1)
        model.placeCard(model.blueState.getHand().size() - 1, 1, 3);
        // For BLUE that dc flips → modifier should appear at (1,2)
        assertEquals(1, model.valueModifiers[1][2]);
    }

    // Modifier affects row-score
    @Test
    public void testRowScoreUsesModifiedValue() {
        // two upgrades so modifier = +2 at (1,2)
        model.rawApplyInfluence(1,1,upgradeCard);
        model.rawApplyInfluence(1,1,upgradeCard);

        // prep cell for RED
        model.board[1][2].setPawns(PlayerColor.RED, 3);
        model.redState.getHand().add(normalCard); // value 5
        model.placeCard(model.redState.getHand().size()-1,1,2);

        // value 5 + mod 2 = 7
        assertEquals(7, model.getRowScore(1, PlayerColor.RED));
    }

    // copyModel deep-copy
    @Test
    public void testCopyModelIndependenceEnhanced() {
        IPawnsBoardModel copy = model.copyModel();
        // mutate original
        model.valueModifiers[1][2] = 99;
        assertNotEquals(((EnhancedPawnsBoardModel) copy).valueModifiers[1][2], 99);
    }

    
}
