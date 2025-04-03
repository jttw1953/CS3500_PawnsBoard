package cs3500.pawnsboard.controller;

/**
 * A stub implementation of {@link IPawnsBoardController} used primarily for testing
 * or demonstration purposes. This controller simply logs user interactions to the console
 * rather than performing any actual game logic.
 * It is useful for verifying that the view is correctly sending events (such as
 * clicks and key presses) to the controller without requiring a full implementation.
 */
public class StubController implements IPawnsBoardController {

  /**
   * Logs the coordinates of the clicked cell.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  @Override
  public void onCellClicked(int row, int col) {
    System.out.println("Clicked cell: (" + row + ", " + col + ")");
  }

  /**
   * Logs the index of the clicked card in the player's hand.
   *
   * @param cardIndex the index of the selected card
   */
  @Override
  public void onCardClicked(int cardIndex) {
    System.out.println("Clicked card in hand with index: " + cardIndex);
  }

  /**
   * Logs when the user chooses to pass their turn.
   */
  @Override
  public void onPass() {
    System.out.println("Pass pressed");
  }

  /**
   * Logs when the user chooses to confirm their selected action.
   */
  @Override
  public void onConfirm() {
    System.out.println("Confirm pressed");
  }
}

