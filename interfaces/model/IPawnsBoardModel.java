package cs3500.pawnsboard.model;


/**
 * Represents a mutable version of the Pawns Board game model.
 * Extends ReadOnlyPawnsBoardModel to include all game state query methods,
 * and adds methods that allow mutation of the model (i.e., gameplay actions).
 * This interface defines all actions that players can take to affect the game state.
 */
public interface IPawnsBoardModel extends ReadOnlyPawnsBoardModel {

  /**
   * Draws a card from the current player's deck and adds it to their hand,
   * if their deck is not empty. If the deck is empty, this method does nothing.
   * This is typically called automatically at the start of the player's turn.
   */
  void drawCardIfPossible();

  /**
   * Attempts to place the card at the given index from the current player's hand
   * onto the board at the specified row and column.
   * The card's cost must be paid using pawns in the target cell, and
   * the cell must be owned by the player and not already contain a card.
   *
   * @param handIndex the index of the card in the current player's hand
   * @param row the row on the board to place the card
   * @param col the column on the board to place the card
   * @throws IllegalArgumentException if the move is invalid (e.g., not enough pawns,
   *         wrong ownership, cell already occupied, or index out of bounds)
   */
  void placeCard(int handIndex, int row, int col);

  /**
   * Passes the current player's turn without taking any action.
   * If both players pass consecutively, the game ends.
   */
  void pass();

  /**
   * Creates and returns a deep copy of the current game model,
   * including all board state, player hands, and decks.
   * Useful for testing or simulating moves without mutating the real model.
   *
   * @return a copy of the current model
   */
  IPawnsBoardModel copyModel();
}


