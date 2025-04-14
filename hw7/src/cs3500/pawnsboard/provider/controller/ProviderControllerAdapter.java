package cs3500.pawnsboard.provider.controller;


public class ProviderControllerAdapter implements IPawnsBoardController {

  // Use the fully qualified name for the real controller from your code.
  private final cs3500.pawnsboard.controller.IPawnsBoardController realController;

  /**
   * Construct an adapter around your real PawnsBoard controller.
   *
   * @param realController the actual controller you wrote (local implementation)
   */
  public ProviderControllerAdapter(cs3500.pawnsboard.controller.IPawnsBoardController realController) {
    this.realController = realController;
  }

  @Override
  public void onCellClicked(int row, int col) {
    realController.onCellClicked(row, col);
  }

  @Override
  public void onCardClicked(int cardIndex) {
    realController.onCardClicked(cardIndex);
  }

  @Override
  public void onPass() {
    realController.onPass();
  }

  @Override
  public void onConfirm() {
    realController.onConfirm();
  }
}
