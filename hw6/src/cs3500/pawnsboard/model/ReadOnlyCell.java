package cs3500.pawnsboard.model;

/**
 * An immutable snapshot of a Cell's state, for exposing via the model's API
 * without letting callers mutate the Cell directly.
 */
public class ReadOnlyCell {
  public final CellType type;
  public final PlayerColor owner; // null if none
  public final int pawnCount;     // 0 if not pawns
  public final String cardName;   // "" if not a card

  /**
   * Main constructor: build a read-only snapshot from a Cell.
   */
  public ReadOnlyCell(Cell cell) {
    this.type = cell.getType();
    this.owner = cell.getOwner();
    this.pawnCount = cell.getPawnCount();
    Card c = cell.getCard();
    this.cardName = (c == null) ? "" : c.getName();
  }

  /**
   * Secondary constructor (often used in tests) to directly provide field values.
   */
  public ReadOnlyCell(CellType type, PlayerColor owner, int pawnCount, String cardName) {
    this.type = type;
    this.owner = owner;
    this.pawnCount = pawnCount;
    this.cardName = cardName;
  }
}

