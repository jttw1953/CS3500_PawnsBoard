package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.EnhancedPawnsBoardModel;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyCell;

/**
 * A simple textual view for visualizing both the base model and the enhanced model with U/D modifiers.
 */
public class EnhancedPawnsBoardTextView {

  /**
   * Print the current board state, including cell types, owners, pawn counts,
   * and, if using EnhancedPawnsBoardModel, the effective card values and modifiers.
   */
  public static void render(IPawnsBoardModel model) {
    int rows = model.getRows();
    int cols = model.getCols();
    boolean isEnhanced = model instanceof EnhancedPawnsBoardModel;
    EnhancedPawnsBoardModel em = isEnhanced
            ? (EnhancedPawnsBoardModel) model
            : null;

    for (int r = 0; r < rows; r++) {
      StringBuilder line = new StringBuilder();
      for (int c = 0; c < cols; c++) {
        ReadOnlyCell cell = model.getCellState(r, c);
        String cellStr;
        switch (cell.type) {
          case PAWNS:
            cellStr = String.format("P[%s:%d]", cell.owner, cell.pawnCount);
            break;
          case CARD:
            Card card = em.board[r][c].getCard();
            int base = card.getValue();
            int mod = isEnhanced ? em.valueModifiers[r][c] : 0;
            int eff = base + mod;
            cellStr = String.format("C[%s:%d%+d]",
                    cell.owner, eff, mod);
            break;
          case EMPTY:
          default:
            cellStr = "[   ]";
        }
        line.append(String.format("%-8s", cellStr));
      }
      // append row scores
      line.append(" | R=").append(model.getRowScore(r, PlayerColor.RED));
      line.append(" B=").append(model.getRowScore(r, PlayerColor.BLUE));
      System.out.println(line.toString());
    }
    // total scores
    System.out.println(String.format(
            "Total → Red: %d   Blue: %d",
            model.getTotalScore(PlayerColor.RED),
            model.getTotalScore(PlayerColor.BLUE)
    ));
  }
}
