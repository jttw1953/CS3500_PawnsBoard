package cs3500.pawnsboard.strategy;

/**
 * Represents a move in PawnsBoard.
 * isPass = true => pass turn
 * otherwise, place the card at (handIndex, row, col).
 */
public class Move {
  public final boolean isPass;
  public final int handIndex;
  public final int row;
  public final int col;

  /**
   * Create a pass move.
   */
  public static Move passMove() {
    return new Move(true, -1, -1, -1);
  }

  /**
   * Create a place-card move.
   */
  public static Move placeMove(int handIndex, int row, int col) {
    return new Move(false, handIndex, row, col);
  }

  private Move(boolean isPass, int handIndex, int row, int col) {
    this.isPass = isPass;
    this.handIndex = handIndex;
    this.row = row;
    this.col = col;
  }
}
