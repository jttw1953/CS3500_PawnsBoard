package cs3500.pawnsboard.Controller;

public class StubController implements IPawnsBoardController {
    @Override
    public void onCellClicked(int row, int col) {
        System.out.println("Clicked cell: (" + row + ", " + col + ")");
    }

    @Override
    public void onCardClicked(int cardIndex) {
        System.out.println("Clicked card in hand with index: " + cardIndex);
    }

    @Override
    public void onPass() {
        System.out.println("Pass pressed");
    }

    @Override
    public void onConfirm() {
        System.out.println("Confirm pressed");
    }
}
