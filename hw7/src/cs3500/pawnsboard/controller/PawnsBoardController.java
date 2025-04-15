package cs3500.pawnsboard.Controller;

import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Strategy.Move;
import cs3500.pawnsboard.Strategy.Strategy;
import cs3500.pawnsboard.View.IPawnsBoardGuiView;

/**
 * A real controller that handles user input and enforces
 * the turn-based rules for one player (RED or BLUE).
 * Now enhanced to optionally handle an AI (machine) player
 * if aiStrategy != null. If aiStrategy is null => human player.
 */
public class PawnsBoardController implements IPawnsBoardController {

    private final IPawnsBoardModel model;
    private final IPawnsBoardGuiView view;
    private final PlayerColor myColor;

    // If non-null, this side is an AI that auto-plays whenever it's my turn.
    private final Strategy aiStrategy;

    // Selections for human input
    private int selectedCardIndex = -1;
    private int selectedRow = -1;
    private int selectedCol = -1;

    /**
     * @param model the shared game model
     * @param view a GUI view for this player's perspective (can be null if not needed)
     * @param color which player color (RED/BLUE) we handle
     * @param aiStrategy if non-null, we auto-play using this strategy
     */
    public PawnsBoardController(IPawnsBoardModel model,
                                IPawnsBoardGuiView view,
                                PlayerColor color,
                                Strategy aiStrategy) {
        this.model = model;
        this.view = view;
        this.myColor = color;
        this.aiStrategy = aiStrategy;
    }

    @Override
    public void onCellClicked(int row, int col) {
        if (model.isGameOver()) {
            return;
        }
        // If we are a HUMAN and it's our turn, handle the click:
        if (aiStrategy == null && model.getCurrentPlayer() == myColor) {
            this.selectedRow = row;
            this.selectedCol = col;
            if (view != null) {
                view.refresh();
            }
        }
        // Now let an AI move if needed
        tryAiMoveIfMyTurn();
    }

    @Override
    public void onCardClicked(int cardIndex) {
        if (model.isGameOver()) {
            return;
        }
        // For a HUMAN on our turn:
        if (aiStrategy == null && model.getCurrentPlayer() == myColor) {
            this.selectedCardIndex = cardIndex;
            if (view != null) {
                view.refresh();
            }
        }
        tryAiMoveIfMyTurn();
    }

    @Override
    public void onConfirm() {
        if (model.isGameOver()) {
            return;
        }
        // Human logic
        if (aiStrategy == null && model.getCurrentPlayer() == myColor) {
            if (selectedCardIndex < 0 || selectedRow < 0 || selectedCol < 0) {
                System.out.println("No card/cell selected yet!");
                return;
            }
            try {
                model.placeCard(selectedCardIndex, selectedRow, selectedCol);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println("Cannot place card: " + e.getMessage());
                // optionally: if (view != null) { view.showErrorMessage(e.getMessage()); }
            }
            resetSelections();
            if (view != null) {
                view.refresh();
            }
        }
        // Then see if AI should move
        tryAiMoveIfMyTurn();
    }

    @Override
    public void onPass() {
        if (model.isGameOver()) {
            return;
        }
        // Human logic
        if (aiStrategy == null && model.getCurrentPlayer() == myColor) {
            model.pass();
            resetSelections();
            if (view != null) {
                view.refresh();
            }
        }
        // Then let AI move if it’s AI’s turn
        tryAiMoveIfMyTurn();
    }

    /**
     * If aiStrategy != null and it's currently our turn and the game isn't over,
     * automatically pick a move and place/pass.
     */
    private void tryAiMoveIfMyTurn() {
        if (aiStrategy == null) {
            return;
        }
        if (model.isGameOver() || model.getCurrentPlayer() != myColor) {
            return;
        }
        System.out.println("AI (" + myColor + ") checking for a move...");

        Move chosen = aiStrategy.chooseMove(model, myColor);
        if (chosen.isPass) {
            System.out.println("AI (" + myColor + ") PASSED");
            model.pass();
        } else {
            System.out.println("AI (" + myColor + ") placing card index " + chosen.handIndex +
                    " at (" + chosen.row + "," + chosen.col + ")");
            try {
                model.placeCard(chosen.handIndex, chosen.row, chosen.col);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("AI (" + myColor + ") had invalid move; passing. " + e.getMessage());
                model.pass();
            }
        }
        if (view != null) {
            view.refresh();
        }
    }



    private void resetSelections() {
        selectedCardIndex = -1;
        selectedRow = -1;
        selectedCol = -1;
    }
    public void forceAiCheck() {
        tryAiMoveIfMyTurn();
    }

}
