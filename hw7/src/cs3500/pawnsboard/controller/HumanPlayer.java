package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.strategy.Move;

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

  /**
   * Handles the user's click on a board cell.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  @Override
  public void handleCellClick(int row, int col) {
    selectedRow = row;
    selectedCol = col;
  }

  /**
   * Handles the user's click on a card in their hand.
   *
   * @param cardIndex the index of the selected card
   */
  @Override
  public void handleCardClick(int cardIndex) {
    selectedCardIndex = cardIndex;
  }

  /**
   * Indicates that the user has confirmed their move.
   * A move will be returned on the next call to {@code getNextMove} if a card and cell
   * are both selected.
   */
  @Override
  public void confirm() {
    confirmed = true;
  }

  /**
   * Indicates that the user has chosen to pass their turn.
   */
  @Override
  public void pass() {
    passMove = true;
  }

  /**
   * Returns the next move for the human player based on the user's input.
   *
   * @param model    the current game model
   * @param myColor  the color of the current player
   * @return a {@code Move} if the user has completed their selection and confirmed or passed;
   *         otherwise returns {@code null}
   */
  @Override
  public Move getNextMove(IPawnsBoardModel model, PlayerColor myColor) {
    if (passMove) {
      passMove = false;
      return Move.passMove();
    }

    if (confirmed && selectedCardIndex >= 0 && selectedRow >= 0 && selectedCol >= 0) {
      confirmed = false;
      int row = selectedRow;
      int col = selectedCol;
      int hand = selectedCardIndex;
      selectedRow = -1;
      selectedCol = -1;
      selectedCardIndex = -1;
      return Move.placeMove(hand, row, col);
    }

    return null;
  }
}
