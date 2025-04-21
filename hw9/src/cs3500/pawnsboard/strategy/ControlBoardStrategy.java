package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * A strategy that chooses a move by simulating all valid placements,
 * then picking the one that results in the highest "board control"
 * (i.e. the most cells with a card owned by forPlayer).
 * Ties are broken by row-major order and hand index order.
 */
public class ControlBoardStrategy implements Strategy {

  @Override
  public Move chooseMove(ReadOnlyPawnsBoardModel roModel, PlayerColor forPlayer) {
    // First, ensure we can cast to IPawnsBoardModel so we can call copyModel().
    if (!(roModel instanceof IPawnsBoardModel)) {
      // If it's truly read-only and doesn't implement IPawnsBoardModel, we can't simulate.
      return Move.passMove();
    }

    IPawnsBoardModel model = (IPawnsBoardModel) roModel;

    int rows = model.getRows();
    int cols = model.getCols();
    var hand = model.getCurrentPlayerHand();

    int bestControl = -1;
    Move bestMove = Move.passMove();

    // Iterate over each card and each cell to find valid moves.
    for (int h = 0; h < hand.size(); h++) {
      Card card = hand.get(h);
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          var cell = model.getCellState(r, c);
          // Check if this is a valid place to put the card:
          if (cell.type.name().equals("PAWNS")
                  && cell.owner == forPlayer
                  && cell.pawnCount >= card.getCost()) {

            // Simulate on a copy:
            IPawnsBoardModel copy = model.copyModel();
            try {
              copy.placeCard(h, r, c);
            } catch (Exception e) {
              // If for some reason this fails, skip it.
              continue;
            }

            // Evaluate board control: count cells with a card owned by forPlayer.
            int control = 0;
            for (int rr = 0; rr < copy.getRows(); rr++) {
              for (int cc = 0; cc < copy.getCols(); cc++) {
                var newCell = copy.getCellState(rr, cc);
                if (newCell.type.name().equals("CARD") && newCell.owner == forPlayer) {
                  control++;
                }
              }
            }

            // If this yields a higher control, record it.
            if (control > bestControl) {
              bestControl = control;
              bestMove = Move.placeMove(h, r, c);
            }
          }
        }
      }
    }

    return bestMove;
  }
}

