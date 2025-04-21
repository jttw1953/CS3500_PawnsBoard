package cs3500.pawnsboard.model;

/**
 * Represents an observer that wants to be notified of important
 * changes in the PawnsBoardModel:
 * - when the current player changes,
 * - when the game ends, etc.
 */
public interface ModelObserver {

  /**
   * Called whenever the active player changes.
   */
  void onTurnChanged(PlayerColor newActivePlayer);

  /**
   * Called when the game ends.
   *
   * @param winner the winning color, or null if tie
   */
  void onGameOver(PlayerColor winner);
}
