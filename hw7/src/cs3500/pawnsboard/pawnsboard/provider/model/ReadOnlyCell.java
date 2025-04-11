package cs3500.pawnsboard.provider.model;

/**
 * A minimal interface to represent a read-only cell in the provider's model.
 */
public interface ReadOnlyCell {

    /*
     * The provider might expect certain methods here; if their code references
     * getType(), getOwner(), getPawnCount(), getCardName(), etc., you can
     * declare them below. For now, we'll declare them in a generic way:
     */

    /**
     * Returns the owner of this cell, or null if there is none.
     *
     * @return the PlayerColor that owns this cell
     */
    PlayerColor getOwner();

    /**
     * Returns the number of pawns on this cell.
     *
     * @return the pawn count on this cell
     */
    int getPawnCount();

    /**
     * Returns the name of the card in this cell, or an empty string if none.
     *
     * @return the name of the card, or "" if no card
     */
    String getCardName();

    /*
     * Add any other methods that the provider’s code calls on ReadOnlyCell.
     */
}
