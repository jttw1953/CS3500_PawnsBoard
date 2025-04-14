package cs3500.pawnsboard.provider.strategy;

public class ProviderMoveAdapter implements Move {

  private final cs3500.pawnsboard.strategy.Move realMove;

  public ProviderMoveAdapter(cs3500.pawnsboard.strategy.Move realMove) {
    if (realMove == null) {
      throw new IllegalArgumentException("realMove cannot be null");
    }
    this.realMove = realMove;
  }

  @Override
  public boolean isPass() {
    return realMove.isPass;
  }

  @Override
  public int getHandIndex() {
    return realMove.handIndex;
  }

  @Override
  public int getRow() {
    return realMove.row;
  }

  @Override
  public int getCol() {
    return realMove.col;
  }
}
