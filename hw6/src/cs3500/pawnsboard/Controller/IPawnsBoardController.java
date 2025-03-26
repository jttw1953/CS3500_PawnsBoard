package cs3500.pawnsboard.Controller;

public interface IPawnsBoardController {
  void onCellClicked(int row, int col);

  void onCardClicked(int cardIndex);

  void onPass();

  void onConfirm();
}
