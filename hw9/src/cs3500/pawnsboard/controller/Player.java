package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.strategy.Move;

/**
 * Represents a single player's ability to produce a move
 * (either by user input or AI strategy).
 */
public interface Player {
  /**
   * Called when the user clicked a cell, if this is a human.
   * AI might ignore this method.
   */
  void handleCellClick(int row, int col);

  /**
   * Called when the user clicked a card, if this is a human.
   * AI might ignore this method.
   */
  void handleCardClick(int cardIndex);

  /**
   * Called when the user pressed the "confirm" button (human).
   * AI might ignore.
   */
  void confirm();

  /**
   * Called when the user pressed the "pass" button (human).
   * AI might ignore.
   */
  void pass();

  /**
   * Retrieves the next move the player wants to make, or null if they're not ready.
   * For an AI, this might always return a move instantly.
   * For a human, it may return null until they've selected and confirmed.
   */
  Move getNextMove(IPawnsBoardModel model, PlayerColor myColor);
}
