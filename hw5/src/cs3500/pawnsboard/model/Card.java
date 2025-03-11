package cs3500.pawnsboard.model;

/**
 * Represents a card in PawnsBoard, with a name, cost, value, and a 5x5 influence grid.
 */
public class Card {
    private final String name;
    private final int cost;   // must be 1-3
    private final int value;  // must be > 0
    private final InfluenceGrid grid;

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

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getValue() {
        return value;
    }

    public InfluenceGrid getInfluenceGrid() {
        return grid;
    }
}

