package cs3500.pawnsboard;

import cs3500.pawnsboard.Model.Card;
import cs3500.pawnsboard.Util.DeckReader;
import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;

// NEW: imports for the GUI
import cs3500.pawnsboard.View.IPawnsBoardGuiView;
import cs3500.pawnsboard.View.PawnsBoardSwingView;
import cs3500.pawnsboard.Controller.IPawnsBoardController;
import cs3500.pawnsboard.Controller.StubController;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * The main entry point for a demonstration of PawnsBoard.
 */
public class PawnsBoard {
    public static void main(String[] args) {
        try {
            String path = "docs/deck.config";
            List<Card> deck = DeckReader.readDeckFromFile(path);

            // Build the model: 5x7 board, each player gets the same deck, initial hand size=5
            // (Make sure inside PawnsBoardModel you comment out the shuffle lines if you want
            // guaranteed first cards to be cost=1, etc.)
            IPawnsBoardModel model = new PawnsBoardModel(
                    5, 7,
                    deck, // red deck
                    deck, // blue deck
                    5
            );

            // Force one pass so that it's Blue's turn instead of Red's
            model.pass();

            // Wrap the model in a read-only reference
            ReadOnlyPawnsBoardModel roModel = model;

            // Launch the GUI
            IPawnsBoardGuiView guiView = new PawnsBoardSwingView(roModel);
            IPawnsBoardController controller = new StubController();
            guiView.setController(controller);
            guiView.makeVisible();

            System.out.println("=== GUI View Demo ===");
            System.out.println("A GUI window is now open.\n"
                    + "Use mouse clicks on the board or hand.\n"
                    + "Press ENTER to confirm or 'P' to pass.\n"
                    + "See console for stub output.");

        } catch (FileNotFoundException e) {
            System.err.println("Could not find deck file: " + e.getMessage());
        }
    }



}


