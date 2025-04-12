package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.Card;

/**
 * A strategy that picks the first valid card+location from
 * the current player's hand & board scanning row-major.
 * If none is valid, pass.
 */
public class FillFirstStrategy implements Strategy {

  /**
   * Chooses the first valid card and cell combination for the given player by
   * scanning the board row-by-row and checking each card in the player’s hand.
   *
   * @param model the read-only model representing the current game state
   * @param forPlayer the player for whom the strategy is choosing a move
   * @return a {@link Move} representing either a card placement or a pass
   */
  @Override
  public Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer) {
    // We need to check each card in hand, each board cell, to see if it's a valid move
    // But your model doesn't have a built-in "isLegal" method, so either you can:
    // 1) Add isLegal(...) to your model,
    // 2) or do your own checks (like cost <= pawns in cell, cell belongs to forPlayer, etc.),
    // 3) or try a "copy" approach that calls placeCard and catches exceptions (not recommended).

    // For demonstration, let's do simple checks:
    // We'll assume the "forPlayer" is actually the current player, or that the board is set up so
    // that the cell's owner matches forPlayer. If it doesn't, it's invalid.

    // get the board size:
    int rows = model.getRows();
    int cols = model.getCols();
    // get player's hand:
    var hand = model.getCurrentPlayerHand();
    // or track forPlayer's hand if your model supports that
    // naive row-major approach:
    for (int h = 0; h < hand.size(); h++) {
      ICard card = hand.get(h);
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          // We'll just do a quick check:
          // 1) cell must have pawns
          // 2) cell's owner == forPlayer
          // 3) cell's pawnCount >= card.cost
          // 4) cell not already has a card
          // This is basically partial "legality" logic

          var cell = model.getCellState(r, c);
          if (cell.type.name().equals("PAWNS")
                  && cell.owner == forPlayer
                  && cell.pawnCount >= card.getCost()) {
            // That’s a valid place
            return Move.placeMove(h, r, c);
          }
        }
      }
    }

    // If we found no valid card/cell combo, pass
    return Move.passMove();
  }
}
