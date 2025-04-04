package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.view.IPawnsBoardGuiView;
import cs3500.pawnsboard.controller.IPawnsBoardController;

/**
 * A real controller that handles user input and enforces
 * the turn-based rules for one player (RED or BLUE).
 */
public class PawnsBoardController implements IPawnsBoardController {
    private final IPawnsBoardModel model;
    private final IPawnsBoardGuiView view;
    private final PlayerColor myColor;

    // We’ll store the user’s “selected” card/cell within the controller
    private int selectedCardIndex = -1;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public PawnsBoardController(IPawnsBoardModel model,
                                IPawnsBoardGuiView view,
                                PlayerColor color) {
        this.model = model;
        this.view = view;
        this.myColor = color;
    }

    @Override
    public void onCellClicked(int row, int col) {
        // Only care if it's actually my turn
        if (model.isGameOver()) {
            return; // do nothing if game’s over
        }
        if (model.getCurrentPlayer() != myColor) {
            // Optionally beep or show a message in your view:
            // view.showError("Not your turn!");
            return;
        }
        // Save the selection
        this.selectedRow = row;
        this.selectedCol = col;
        // Optionally: re-render or do something
        view.refresh();
    }

    @Override
    public void onCardClicked(int cardIndex) {
        if (model.isGameOver()) {
            return;
        }
        if (model.getCurrentPlayer() != myColor) {
            return;
        }
        // Save the selection
        this.selectedCardIndex = cardIndex;
        view.refresh();
    }

    @Override
    public void onConfirm() {
        if (model.isGameOver()) {
            return;
        }
        // Confirming a move means we try to place the selected card
        // into the selected cell, if it's actually our turn
        if (model.getCurrentPlayer() != myColor) {
            // not our turn => ignore
            return;
        }

        // Must have a valid selection
        if (selectedCardIndex < 0 || selectedRow < 0 || selectedCol < 0) {
            // E.g. show message
            // (Add a method in your view if you want a popup)
            System.out.println("No card/cell selected yet!");
            return;
        }

        try {
            // The model does the actual checks (pawns, cost, etc.)
            model.placeCard(selectedCardIndex, selectedRow, selectedCol);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Show error in console or a popup
            System.err.println("Cannot place card: " + e.getMessage());
            // Possibly add an error dialog in your Swing view: view.showErrorMessage(e.getMessage());
        }

        // Clear out selections for next time
        resetSelections();
        // Repaint the screen
        view.refresh();
    }

    @Override
    public void onPass() {
        if (model.isGameOver()) {
            return;
        }
        if (model.getCurrentPlayer() != myColor) {
            return;
        }
        // Attempt to pass
        model.pass();

        // If passing triggered game over, the model’s isGameOver() is true now.
        resetSelections();
        view.refresh();
    }

    private void resetSelections() {
        selectedCardIndex = -1;
        selectedRow = -1;
        selectedCol = -1;
    }
}
