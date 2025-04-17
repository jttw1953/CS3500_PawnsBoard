package cs3500.pawnsboard.provider.controller;


/**
 * Adapter class that allows the provider's system to use our internal controller
 * by implementing the provider-facing {@code IPawnsBoardController} interface.
 */
public class ProviderControllerAdapter implements IPawnsBoardController {

  // Reference to the actual controller written by us.
  private final cs3500.pawnsboard.controller.IPawnsBoardController realController;

  /**
   * Constructs a {@code ProviderControllerAdapter} that wraps around the real
   * implementation of our controller.
   *
   * @param realController the actual controller written in our codebase that handles game logic
   * @throws NullPointerException if the provided controller is null
   */
  public ProviderControllerAdapter(cs3500.pawnsboard.controller.IPawnsBoardController
                                           realController) {
    this.realController = realController;
  }

  /**
   * Forwards a cell click event from the provider view to our real controller.
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  @Override
  public void onCellClicked(int row, int col) {
    realController.onCellClicked(row, col);
  }

  /**
   * Forwards a card click event from the provider view to our real controller.
   * @param cardIndex the index of the clicked card in the hand
   */
  @Override
  public void onCardClicked(int cardIndex) {
    realController.onCardClicked(cardIndex);
  }

  /**
   * Forwards a pass action to our real controller.
   */
  @Override
  public void onPass() {
    realController.onPass();
  }

  /**
   * Forwards a confirm action (used to finalize a selected move) to our real controller.
   */
  @Override
  public void onConfirm() {
    realController.onConfirm();
  }
}

