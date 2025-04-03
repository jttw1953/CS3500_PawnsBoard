package cs3500.pawnsboard.strategy;

import java.util.List;

import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * A strategy that combines multiple other strategies in priority order.
 * This composite strategy delegates to each strategy in the provided list, in order.
 * It returns the first move that is not a pass. If all strategies return pass moves,
 * this strategy also returns a pass.
 * This is useful for creating layered or fallback strategies where
 * multiple heuristics are attempted in sequence.
 */
public class CompositeStrategy implements Strategy {
  private final List<Strategy> strategies;

  /**
   * Constructs a {@code CompositeStrategy} with the given list of strategies.
   * The strategies are tried in the order they appear in the list.
   *
   * @param strategies the list of strategies to apply in order
   * @throws IllegalArgumentException if the list is null or empty
   */
  public CompositeStrategy(List<Strategy> strategies) {
    this.strategies = strategies;
  }

  /**
   * Chooses a move by applying each strategy in order until one returns
   * a non-pass move. If all strategies return a pass, this method returns a pass as well.
   *
   * @param model the read-only view of the game state
   * @param forPlayer the player for whom the move is being chosen
   * @return the first non-pass move returned by the strategies, or a pass if none apply
   */
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
