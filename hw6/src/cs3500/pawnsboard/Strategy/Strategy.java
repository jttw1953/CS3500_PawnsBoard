package cs3500.pawnsboard.Strategy;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;

/**
 * Represents a strategy for choosing a move in PawnsBoard.
 */
public interface Strategy {
  /**
   * Choose a move (handIndex, row, col) or decide to pass.
   * If no valid move, you must pass.
   */
  Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer);
}
