package cs3500.pawnsboard.Model;

/**
 * Represents one cell on the board. It can have:
 * - 0-3 pawns belonging to a certain color, OR
 * - a single Card belonging to a color, OR
 * - be empty.
 */
public class Cell {
    private CellType type;
    private PlayerColor owner; // who owns the pawns/card
    private Card card;         // if type == CARD
    private int pawnCount;     // if type == PAWNS, between 0 and 3 inclusive

    public Cell() {
        this.type = CellType.EMPTY;
        this.owner = null;
        this.card = null;
        this.pawnCount = 0;
    }

    public CellType getType() {
        return type;
    }

    public PlayerColor getOwner() {
        return owner;
    }

    public int getPawnCount() {
        return pawnCount;
    }

    public Card getCard() {
        return card;
    }

    /**
     * Place pawns in this cell. If it's EMPTY, it becomes PAWNS with the given owner and count.
     */
    public void setPawns(PlayerColor owner, int count) {
        this.type = CellType.PAWNS;
        this.owner = owner;
        this.card = null;
        this.pawnCount = count;
    }

    /**
     * Place a card in this cell. If it was PAWNS, we remove them and replace with a Card.
     */
    public void setCard(PlayerColor owner, Card card) {
        this.type = CellType.CARD;
        this.owner = owner;
        this.card = card;
        this.pawnCount = 0;
    }

    /**
     * Make this cell empty again.
     */
    public void setEmpty() {
        this.type = CellType.EMPTY;
        this.owner = null;
        this.card = null;
        this.pawnCount = 0;
    }

    /**
     * Change just the owner of existing pawns (same pawn count).
     * This is used when we "convert" opponent pawns to the current player's pawns.
     */
    public void changeOwner(PlayerColor newOwner) {
        if (this.type == CellType.PAWNS) {
            this.owner = newOwner;
        }
    }

    /**
     * Increase the number of pawns in this cell by 1, up to a max of 3.
     */
    public void incrementPawns() {
        if (this.type == CellType.PAWNS) {
            if (this.pawnCount < 3) {
                this.pawnCount++;
            }
        }
    }
}
