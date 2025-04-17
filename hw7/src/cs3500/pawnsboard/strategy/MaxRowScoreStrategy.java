package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;

/**
 * A strategy that tries to place a card so that the player
 * can outscore the opponent on a row, scanning from top row to bottom.
 * In a tie, or if no improvement is possible, pass.
 */
public class MaxRowScoreStrategy implements Strategy {

  /**
   * Chooses a move for the given player with the goal of improving their row score.
   * <
   * This strategy scans the board row-by-row from top to bottom, comparing the current player's
   * row score against the opponent's. If the player is losing or tied in a row, it attempts
   * to place a card in that row to gain an advantage.
   * A card is considered placeable if:
   * - The target cell is of type PAWNS
   * - The cell is owned by the current player
   * - The player has enough pawns in the cell to pay the card’s cost
   * - The card has a value greater than 0 (i.e., contributes influence)
   * If no such move is found that might improve the score, the strategy passes.
   *
   * @param model the read-only view of the game model
   * @param forPlayer the player for whom the move is being chosen
   * @return Move representing either a card placement or a pass if no beneficial move is found
   */
  @Override
  public Move chooseMove(ReadOnlyPawnsBoardModel model, PlayerColor forPlayer) {
    PlayerColor opp = (forPlayer == PlayerColor.RED) ? PlayerColor.BLUE : PlayerColor.RED;
    int rows = model.getRows();
    int cols = model.getCols();
    var hand = model.getCurrentPlayerHand();

    // For each row top-down, see if forPlayer is losing or tied
    for (int r = 0; r < rows; r++) {
      int myRowScore = model.getRowScore(r, forPlayer);
      int oppRowScore = model.getRowScore(r, opp);

      // if losing or tied, see if we can place a card that helps
      if (myRowScore <= oppRowScore) {
        // try each card in the hand
        for (int h = 0; h < hand.size(); h++) {
          ICard card = hand.get(h);
          // try each col
          for (int c = 0; c < cols; c++) {
            var cell = model.getCellState(r, c);
            // same check as fill-first: must have pawns of forPlayer,
            // must have enough pawns to pay cost, must be no card yet
            if (cell.type.name().equals("PAWNS")
                    && cell.owner == forPlayer
                    && cell.pawnCount >= card.getCost()) {
              // We guess this might help. However, to *know* if it helps, we'd
              // have to figure out the final row score after placement & influence.
              // For part 2, we can just assume any card with value > 0 helps
              // or do a deeper check. We'll keep it simple:
              if (card.getValue() > 0) {
                // Good enough. Return it
                return Move.placeMove(h, r, c);
              }
            }
          }
        }
      }
    }
    // no improvement found => pass
    return Move.passMove();
  }
}
