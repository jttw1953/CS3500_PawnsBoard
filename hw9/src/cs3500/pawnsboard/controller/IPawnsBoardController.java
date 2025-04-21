package cs3500.pawnsboard.controller;

/**
 * Represents a controller interface for a GUI-based Pawns Board game.
 * This interface defines the actions that a view can notify the controller about
 * in response to user interactions (e.g. mouse clicks or keyboard inputs).
 * The controller handles all gameplay logic triggered by the view,
 * such as selecting a card, selecting a cell, passing a turn, or confirming a move.
 */
public interface IPawnsBoardController {

  /**
   * Notifies the controller that the user clicked on a specific cell on the board.
   *
   * @param row the row of the clicked cell (0-indexed)
   * @param col the column of the clicked cell (0-indexed)
   */
  void onCellClicked(int row, int col);

  /**
   * Notifies the controller that the user clicked on a specific card in their hand.
   *
   * @param cardIndex the index of the clicked card in the player's hand
   */
  void onCardClicked(int cardIndex);

  /**
   * Notifies the controller that the user wants to pass their turn.
   * This is typically triggered by pressing a "Pass" button or the 'P' key.
   */
  void onPass();

  /**
   * Notifies the controller that the user wants to confirm their selected action,
   * typically placing the selected card at the selected cell.
   * This is often triggered by pressing "Enter" or a confirm button.
   */
  void onConfirm();
}

