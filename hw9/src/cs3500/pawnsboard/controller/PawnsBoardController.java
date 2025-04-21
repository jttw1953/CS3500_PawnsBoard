package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.ModelObserver;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.strategy.Move;
import cs3500.pawnsboard.view.IPawnsBoardGuiView;

/**
 * A controller responsible for a single player (RED or BLUE).
 * It mediates between:
 * - the model,
 * - a Player (human or AI),
 * - and an optional IPawnsBoardGuiView (GUI).
 * It does NOT implement strategy logic itself;
 * that is in the Player implementation.
 */
public class PawnsBoardController implements IPawnsBoardController, ModelObserver {

  private final IPawnsBoardModel model;
  private final IPawnsBoardGuiView view;   // May be null if this side doesn't need a GUI
  private final PlayerColor myColor;
  private final Player player;             // The abstraction that decides moves

  /**
   * Constructs a controller for one player/color.
   *
   * @param model   the shared game model
   * @param view    a GUI view (or null) for this player
   * @param myColor which PlayerColor (RED or BLUE) this controller manages
   * @param player  the entity that decides moves (human or AI)
   */
  public PawnsBoardController(IPawnsBoardModel model,
                              IPawnsBoardGuiView view,
                              PlayerColor myColor,
                              Player player) {
    this.model = model;
    this.view = view;
    this.myColor = myColor;
    this.player = player;

    model.addObserver(this);
  }

  @Override
  public void onTurnChanged(PlayerColor newActivePlayer) {
    // 1) Always redraw
    if (view != null) {
      view.refresh();
    }

    // 2) If it's my turn *and* I'm an AI player, fire off the move
    this.forceAiMoveIfMyTurn();
  }


  @Override
  public void onGameOver(PlayerColor winner) {

    if (view != null) {
      view.refresh();
    }
  }

  @Override
  public void onCellClicked(int row, int col) {
    if (model.isGameOver()) {
      return;
    }
    // Only pass the click to our player if it's actually our turn:
    if (model.getCurrentPlayer() == myColor) {
      player.handleCellClick(row, col);
      attemptMove();
    }
    if (view != null) {
      view.refresh();
    }
  }

  @Override
  public void onCardClicked(int cardIndex) {
    if (model.isGameOver()) {
      return;
    }
    if (model.getCurrentPlayer() == myColor) {
      player.handleCardClick(cardIndex);
      attemptMove();
    }
    if (view != null) {
      view.refresh();
    }
  }

  @Override
  public void onConfirm() {
    if (model.isGameOver()) {
      return;
    }
    if (model.getCurrentPlayer() == myColor) {
      player.confirm();
      attemptMove();
    }
    if (view != null) {
      view.refresh();
    }
  }

  @Override
  public void onPass() {
    if (model.isGameOver()) {
      return;
    }
    if (model.getCurrentPlayer() == myColor) {
      player.pass();
      attemptMove();
    }
    if (view != null) {
      view.refresh();
    }
  }

  /**
   * Tries to fetch a completed move from the Player and apply it to the model.
   * If the player isn't ready to commit a move yet (returns null), we do nothing.
   * If we get a valid move, we place or pass in the model.
   */
  private void attemptMove() {
    if (model.isGameOver() || model.getCurrentPlayer() != myColor) {
      return;
    }

    // Ask the player for the next move
    Move move = player.getNextMove(model, myColor);
    if (move == null) {
      // Means the user hasn't finalized or the AI is "thinking".
      return;
    }

    if (move.isPass) {
      model.pass();
    } else {
      try {
        model.placeCard(move.handIndex, move.row, move.col);
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.err.println("Invalid move: " + e.getMessage());
        // The player might see the error next time around
      }
    }
  }

  public void forceAiMoveIfMyTurn() {
    attemptMove();          // just call the private helper
  }
}
