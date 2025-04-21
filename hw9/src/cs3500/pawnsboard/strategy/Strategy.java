package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

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
