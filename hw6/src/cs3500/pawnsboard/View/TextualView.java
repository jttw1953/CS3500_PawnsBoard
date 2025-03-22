package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyCell;
import cs3500.pawnsboard.Model.PlayerColor;

/**
 * A simple textual view of the PawnsBoard game.
 * It uses only read-only methods on the model to display the board state.
 */
public class TextualView {
    private final ReadOnlyPawnsBoardModel model;

    /**
     * Constructs a new textual view with the given read-only model.
     */
    public TextualView(ReadOnlyPawnsBoardModel model) {
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
        }
        else {
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

        // Show running totals
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
        // Print final board layout
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                ReadOnlyCell cell = model.getCellState(r, c);
                sb.append(cellChar(cell));
            }
            // row-scores
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
        }
        else {
            sb.append("Winner: ").append(winner).append("\n");
        }
        return sb.toString();
    }

    /**
     * Convert a ReadOnlyCell to a single-character representation:
     *  '_' if empty,
     *  '1','2','3' if pawns,
     *  'R' if a Red-owned card,
     *  'B' if a Blue-owned card.
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

