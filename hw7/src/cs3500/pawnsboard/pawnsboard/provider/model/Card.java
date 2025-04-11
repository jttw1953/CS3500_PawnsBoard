package cs3500.pawnsboard.provider.model;

/**
 * Represents a read-only Card interface for the provider's model.
 * This stub simply declares the methods that the provider might expect.
 */
public interface Card {

    /**
     * Returns the name of this card.
     *
     * @return the card name
     */
    String getName();

    /**
     * Returns the cost of this card.
     *
     * @return the card's cost
     */
    int getCost();

    /**
     * Returns the value of this card.
     *
     * @return the card's value
     */
    int getValue();

    /*
     * If the provider’s code calls additional methods (e.g., getInfluenceGrid()),
     * you can add them here to match what they expect. Otherwise, keep it minimal.
     */
}
