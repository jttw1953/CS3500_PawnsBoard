package cs3500.pawnsboard.provider.model;

/**
 * An adapter for converting your real read-only cell data
 * into the provider's ReadOnlyCell interface.
 */
public class ProviderCellAdapter implements ReadOnlyCell {

  private final cs3500.pawnsboard.model.ReadOnlyCell realCell;

  /**
   * Construct an adapter around your real read-only cell.
   *
   * @param realCell the actual read-only cell from your model
   */
  public ProviderCellAdapter(cs3500.pawnsboard.model.ReadOnlyCell realCell) {
    if (realCell == null) {
      throw new IllegalArgumentException("realCell cannot be null");
    }
    this.realCell = realCell;
  }

  @Override
  public PlayerColor getOwner() {
    // convert your color to the provider color using the utility
    cs3500.pawnsboard.model.PlayerColor realOwner = realCell.owner;
    return ProviderColorAdapter.toProviderColor(realOwner);
  }

  @Override
  public int getPawnCount() {
    // If you have a method in your real cell, call it
    return realCell.pawnCount;
  }

  @Override
  public String getCardName() {
    // If your real cell’s card is represented by a string field or method
    return realCell.cardName;
  }


}
