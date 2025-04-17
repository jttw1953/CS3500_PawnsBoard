package cs3500.pawnsboard.provider.strategy;

/**
 * Adapter class that bridges our internal Move class with the provider-facing Move interface.
 * This allows the provider's strategy to treat our move objects as if they implemented
 * their own interface, enabling interoperability between systems without modifying
 * our original design.
 */
public class ProviderMoveAdapter implements Move {

  private final cs3500.pawnsboard.strategy.Move realMove;

  /**
   * Constructs a {@code ProviderMoveAdapter} to wrap around a real move from our strategy.
   * @param realMove the actual move object from our codebase
   * @throws IllegalArgumentException if {@code realMove} is {@code null}
   */
  public ProviderMoveAdapter(cs3500.pawnsboard.strategy.Move realMove) {
    if (realMove == null) {
      throw new IllegalArgumentException("realMove cannot be null");
    }
    this.realMove = realMove;
  }

  /**
   * Returns whether this move is a pass (i.e., no card placement).
   * @return {@code true} if this move is a pass; {@code false} otherwise
   */
  @Override
  public boolean isPass() {
    return realMove.isPass;
  }

  /**
   * Returns the index of the card in hand that this move plays.
   * @return the hand index of the card (only relevant if this is not a pass)
   */
  @Override
  public int getHandIndex() {
    return realMove.handIndex;
  }

  /**
   * Returns the row on the board where the card will be placed.
   * @return the target row of the move
   */
  @Override
  public int getRow() {
    return realMove.row;
  }

  /**
   * Returns the column on the board where the card will be placed.
   * @return the target column of the move
   */
  @Override
  public int getCol() {
    return realMove.col;
  }
}

