package cs3500.pawnsboard.Strategy;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import java.util.List;

public class CompositeStrategy implements Strategy {
    private final List<Strategy> strategies;

    public CompositeStrategy(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer) {
        for (Strategy strat : strategies) {
            Move move = strat.chooseMove(model, forPlayer);
            if (!move.isPass) {
                return move;
            }
        }
        return Move.passMove();
    }
}
