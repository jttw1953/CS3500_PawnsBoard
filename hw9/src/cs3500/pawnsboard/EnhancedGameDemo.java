package cs3500.pawnsboard;

import cs3500.pawnsboard.Model.*;
import java.util.List;
import java.util.Arrays;

/**
 * Simple textual demo of EnhancedPawnsBoardModel showing U/D influence stacking.
 */
public class EnhancedGameDemo {
    public static void main(String[] args) {
        // Define three cards: Upgrade (U), Devalue (D), and Normal (I)
        char[][] uGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','U','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        char[][] dGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','D','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        char[][] iGrid = {
                {'X','X','X','X','X'},
                {'X','X','X','X','X'},
                {'X','X','C','X','X'},
                {'X','X','X','X','X'},
                {'X','X','X','X','X'}
        };
        Card upgrade = new Card("Upgrade", 1, 1, new InfluenceGrid(uGrid));
        Card devalue = new Card("Devalue", 1, 1, new InfluenceGrid(dGrid));
        Card normal  = new Card("Neutral", 1, 1, new InfluenceGrid(iGrid));

        // Build deck and create model with no draws
        List<Card> deck = Arrays.asList(upgrade, devalue, normal);
        EnhancedPawnsBoardModel model = new EnhancedPawnsBoardModel(
                3, 5, deck, deck, 0
        );

        // Manually give each card to both players
        for (Card c : deck) {
            model.getHandFor(PlayerColor.RED).add(c);
            model.getHandFor(PlayerColor.BLUE).add(c);
        }

        System.out.println("=== Initial Board ===");
        render(model);

        // Red places upgrade at (1,0)
        model.placeCard(0, 1, 0);
        System.out.println("\n=== After Red Upgrade at (1,0) ===");
        render(model);

        // Blue places devalue at (1,4)
        model.placeCard(1, 1, model.getCols() - 1);
        System.out.println("\n=== After Blue Devalue at (1,4) ===");
        render(model);
    }

    private static void render(ReadOnlyPawnsBoardModel m) {
        int rows = m.getRows();
        int cols = m.getCols();
        EnhancedPawnsBoardModel em = m instanceof EnhancedPawnsBoardModel
                ? (EnhancedPawnsBoardModel) m : null;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ReadOnlyCell cell = m.getCellState(r, c);
                String s;
                switch (cell.type) {
                    case PAWNS:
                        s = String.format("P(%s:%d)", cell.owner, cell.pawnCount);
                        break;
                    case CARD:
                        Card card = em.board[r][c].getCard();
                        int base = card.getValue();
                        int mod  = em.valueModifiers[r][c];
                        int eff  = base + mod;
                        s = String.format("C(%s:%d|%+d)", cell.owner, eff, mod);
                        break;
                    default:
                        s = " . ";
                }
                System.out.print(String.format("%-10s", s));
            }
            System.out.println(" | R=" + m.getRowScore(r, PlayerColor.RED)
                    + " B=" + m.getRowScore(r, PlayerColor.BLUE));
        }
        System.out.println("Total → Red:" + m.getTotalScore(PlayerColor.RED)
                + " Blue:" + m.getTotalScore(PlayerColor.BLUE));
    }
}