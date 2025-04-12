package cs3500.pawnsboard.model;

/**
 * An interface representing the public-facing behavior of a card
 * used in the PawnsBoard game. This interface is designed to be used
 * by views and strategies to access card data without depending
 * on the full implementation.
 */
public interface ICard {

  /**
   * Gets the name of the card.
   * @return the name of the card
   */
  String getName();

  /**
   * Gets the cost of the card (how many pawns are needed to play it).
   * @return the cost of the card
   */
  int getCost();

  /**
   * Gets the score value of the card.
   * @return the value of the card
   */
  int getValue();

  /**
   * Gets the influence grid associated with this card.
   * @return the card's influence grid
   */
  IInfluenceGrid getInfluenceGrid();
}
