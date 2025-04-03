package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;

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
