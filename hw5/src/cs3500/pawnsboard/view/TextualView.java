package cs3500.pawnsboard.view;


import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyCell;

public class TextualView {
  private final IPawnsBoardModel model;

  public TextualView(IPawnsBoardModel model) {
    this.model = model;
  }

  /**
   * Produce a string representation of the game state:
   * each row has characters for each cell, plus row-scores in parentheses.
   */
  @Override
  public String toString() {
    if (model.isGameOver()) {
      return endGameString();
    } else {
      return midGameString();
    }
  }

  /**
   * Representation while the game is ongoing.
   */
  private String midGameString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Current Player: ").append(model.getCurrentPlayer()).append("\n");

    // Print the board rows
    for (int r = 0; r < model.getRows(); r++) {
      for (int c = 0; c < model.getCols(); c++) {
        ReadOnlyCell cell = model.getCellState(r, c);
        sb.append(cellChar(cell));
      }
      // Show row-scores
      int redRow = model.getRowScore(r, PlayerColor.RED);
      int blueRow = model.getRowScore(r, PlayerColor.BLUE);
      sb.append("  (R=").append(redRow).append(", B=").append(blueRow).append(")\n");
    }

    // Show total so far
    int redTotal = model.getTotalScore(PlayerColor.RED);
    int blueTotal = model.getTotalScore(PlayerColor.BLUE);
    sb.append("Running totals => RED: ").append(redTotal)
            .append(" | BLUE: ").append(blueTotal).append("\n");

    return sb.toString();
  }

  /**
   * Representation after the game is over.
   */
  private String endGameString() {
    StringBuilder sb = new StringBuilder();
    sb.append("GAME OVER\n");
    // print final board layout
    for (int r = 0; r < model.getRows(); r++) {
      for (int c = 0; c < model.getCols(); c++) {
        ReadOnlyCell cell = model.getCellState(r, c);
        sb.append(cellChar(cell));
      }
      int redRow = model.getRowScore(r, PlayerColor.RED);
      int blueRow = model.getRowScore(r, PlayerColor.BLUE);
      sb.append("  (R=").append(redRow).append(", B=").append(blueRow).append(")\n");
    }

    // final totals
    int redTotal = model.getTotalScore(PlayerColor.RED);
    int blueTotal = model.getTotalScore(PlayerColor.BLUE);
    sb.append("Final totals => RED: ").append(redTotal)
            .append(" | BLUE: ").append(blueTotal).append("\n");

    // winner or tie
    PlayerColor winner = model.getWinner();
    if (winner == null) {
      sb.append("It's a tie!\n");
    } else {
      sb.append("Winner: ").append(winner).append("\n");
    }
    return sb.toString();
  }

  /**
   * Convert a ReadOnlyCell to a single-character representation:
   * '_' if empty,
   * '1','2','3' if pawns,
   * 'R' if a Red-owned card,
   * 'B' if a Blue-owned card.
   */
  private char cellChar(ReadOnlyCell cell) {
    switch (cell.type) {
      case EMPTY:
        return '_';
      case PAWNS:
        // digit = 1..3
        return (char) ('0' + cell.pawnCount);
      case CARD:
        return (cell.owner == PlayerColor.RED) ? 'R' : 'B';
      default:
        return '?';
    }
  }
}

