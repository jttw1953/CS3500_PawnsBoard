// File: src/cs3500/pawnsboard/provider/strategy/Move.java

package cs3500.pawnsboard.provider.strategy;

/**
 * Represents a move in the provider's strategy code.
 */
public interface Move {

  /**
   * Returns whether this move is a pass.
   *
   * @return true if pass, false otherwise
   */
  boolean isPass();

  /**
   * Returns the index of the card in the player's hand for a place-move.
   *
   * @return the hand index, or -1 if it's a pass
   */
  int getHandIndex();

  /**
   * Returns the row to place the card for a place-move.
   *
   * @return the row, or -1 if it's a pass
   */
  int getRow();

  /**
   * Returns the column to place the card for a place-move.
   *
   * @return the column, or -1 if it's a pass
   */
  int getCol();
}
