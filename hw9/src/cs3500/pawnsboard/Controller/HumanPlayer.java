package cs3500.pawnsboard.Controller;

import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Strategy.Move;

/**
 * A player that waits for user clicks and confirm/pass
 * before returning a move.
 */
public class HumanPlayer implements Player {
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int selectedCardIndex = -1;
    private boolean confirmed = false;
    private boolean passMove = false;

    @Override
    public void handleCellClick(int row, int col) {
        selectedRow = row;
        selectedCol = col;
    }

    @Override
    public void handleCardClick(int cardIndex) {
        selectedCardIndex = cardIndex;
    }

    @Override
    public void confirm() {
        confirmed = true;
    }

    @Override
    public void pass() {
        passMove = true;
    }

    @Override
    public Move getNextMove(IPawnsBoardModel model, PlayerColor myColor) {
        if (passMove) {
            // user clicked "pass"
            passMove = false;
            return Move.passMove();
        }

        // if the user clicked "confirm" and has valid selections
        if (confirmed && selectedCardIndex >= 0 && selectedRow >= 0 && selectedCol >= 0) {
            confirmed = false;
            int row = selectedRow;
            int col = selectedCol;
            int hand = selectedCardIndex;
            // reset
            selectedRow = -1;
            selectedCol = -1;
            selectedCardIndex = -1;
            return Move.placeMove(hand, row, col);
        }
        // else not ready => null
        return null;
    }
}
