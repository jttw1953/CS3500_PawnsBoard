package cs3500.pawnsboard.model;

/**
 * Represents a card in PawnsBoard, with a name, cost, value, and a 5x5 influence grid.
 */
public class Card {
  private final String name;
  private final int cost;   // must be 1-3
  private final int value;  // must be > 0
  private final InfluenceGrid grid;

  /**
   * Constructs a Card with a name, cost, value, and influence grid.
   * @param name  the name of the card (must be a non-empty string).
   * @param cost  the cost in pawns to play this card (must be between 1 and 3).
   * @param value the score value of the card (must be greater than 0).
   * @param grid  the 5x5 influence grid defining the card’s effect.
   * @throws IllegalArgumentException if cost is not in range [1,3] or value is <= 0.
   */
  public Card(String name, int cost, int value, InfluenceGrid grid) {
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost must be between 1 and 3");
    }
    if (value < 1) {
      throw new IllegalArgumentException("Value must be positive");
    }
    this.name = name;
    this.cost = cost;
    this.value = value;
    this.grid = grid;
  }

  /**
   * Gets name of a card.
   * @return name of this card
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the cost of a card.
   * @return cost of this card
   */
  public int getCost() {
    return cost;
  }

  /**
   * Gets the value of a card.
   * @return value of this card
   */
  public int getValue() {
    return value;
  }

  /**
   * Gets the influence grid of the card.
   * @return influence grid of this card
   */
  public InfluenceGrid getInfluenceGrid() {
    return grid;
  }
}

