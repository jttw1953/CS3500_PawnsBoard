package cs3500.pawnsboard;

import java.util.Arrays;
import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.EnhancedPawnsBoardModel;
import cs3500.pawnsboard.model.InfluenceGrid;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyCell;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Simple textual demo of EnhancedPawnsBoardModel showing U/D influence stacking.
 */
public class EnhancedGameDemo {

  public static void main(String[] args) {
    /* ── 1. build three demo cards ──────────────────────────────── */
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

    List<Card> deck = Arrays.asList(upgrade, devalue, normal);

    /* ── 2. create 3×5 enhanced model (no initial draws) ────────── */
    EnhancedPawnsBoardModel model =
            new EnhancedPawnsBoardModel(3, 5, deck, deck, 0);

    /* ── 3. seed hands & a legal pawn square for Red ────────────── */
    model.getHandFor(PlayerColor.RED) .addAll(deck);
    model.getHandFor(PlayerColor.BLUE).addAll(deck);
    model.board[1][0].setPawns(PlayerColor.RED, 1);           // (1,0)
    model.board[1][4].setPawns(PlayerColor.BLUE, 1);          // (1,4)

    System.out.println("=== Initial Board ===");
    render(model);

    /* ── 4. Red plays Upgrade at (1,0) ─────────────────────────── */
    model.placeCard(0, 1, 0);                                 // index 0 = Upgrade
    System.out.println("\n=== After Red Upgrade at (1,0) ===");
    render(model);

    /* ── 5. Blue plays Devalue at (1,4) ────────────────────────── */
    model.placeCard(1, 1, model.getCols() - 1);               // index 1 = Devalue
    System.out.println("\n=== After Blue Devalue at (1,4) ===");
    render(model);  
  
  }

  /* ------------------------------------------------------------------ */
  /* Textual renderer that shows modifiers on ANY cell                  */
  /* ------------------------------------------------------------------ */
  private static void render(ReadOnlyPawnsBoardModel m) {
    int rows = m.getRows(), cols = m.getCols();
    EnhancedPawnsBoardModel em = (m instanceof EnhancedPawnsBoardModel)
          ? (EnhancedPawnsBoardModel) m : null;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        ReadOnlyCell cell = m.getCellState(r, c);
        int mod = (em == null) ? 0 : em.valueModifiers[r][c];
        String s;

        switch (cell.type) {
          case EMPTY:
            s = (mod != 0) ? String.format(".(%+d)", mod) : ".";
            break;

          case PAWNS:
            s = String.format("P(%s:%d%s)",
            cell.owner, cell.pawnCount,
            (mod != 0) ? String.format("|%+d", mod) : "");
            break;

          case CARD:
            Card card = em.board[r][c].getCard();
            int eff = Math.max(card.getValue() + mod, 0);
            s = String.format("C(%s:%d|%+d)", cell.owner, eff, mod);
            break;

          default:
            s = "?";
        }
        System.out.print(String.format("%-12s", s));
      }
      System.out.println(" | R=" + m.getRowScore(r, PlayerColor.RED)
                    + " B=" + m.getRowScore(r, PlayerColor.BLUE));
    }
    System.out.println("Total → Red:" + m.getTotalScore(PlayerColor.RED)
                + " Blue:" + m.getTotalScore(PlayerColor.BLUE));
  }
}
