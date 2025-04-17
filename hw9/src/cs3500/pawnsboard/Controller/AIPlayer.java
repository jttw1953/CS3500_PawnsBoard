package cs3500.pawnsboard.Controller;

import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Strategy.Move;
import cs3500.pawnsboard.Strategy.Strategy;

/**
 * A player that uses an AI strategy to pick moves instantly.
 */
public class AIPlayer implements Player {
    private final Strategy strategy;

    public AIPlayer(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void handleCellClick(int row, int col) {
        // AI ignores user clicks
    }

    @Override
    public void handleCardClick(int cardIndex) {
        // AI ignores user clicks
    }

    @Override
    public void confirm() {
        // AI doesn't need confirm
    }

    @Override
    public void pass() {
        // AI doesn't rely on a manual pass
    }

    @Override
    public Move getNextMove(IPawnsBoardModel model, PlayerColor myColor) {
        // AI instantly chooses a move or pass
        return strategy.chooseMove(model, myColor);
    }
}
