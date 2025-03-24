package cs3500.pawnsboard;

import cs3500.pawnsboard.Model.Card;
import cs3500.pawnsboard.Util.DeckReader;
import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.View.TextualView;

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
            // Path to your config file in docs/ folder
            String path = "docs/deck.config";
            // or e.g. "docs/myDeck.txt" depending on your file name
            List<Card> deck = DeckReader.readDeckFromFile(path);

            // Build the model: 3x5 board, each player gets the same deck, initial hand size=5
            IPawnsBoardModel model = new PawnsBoardModel(
                    3, 5,
                    deck, // red deck
                    deck, // blue deck
                    5
            );

            // Wrap the model in a read-only reference for the textual view
            ReadOnlyPawnsBoardModel roModel = model;

            // 1) Text-based demonstration:
            TextualView textView = new TextualView(roModel);

            System.out.println("=== Textual View Demo ===");
            System.out.println("Initial state:");
            System.out.println(textView);

            // We'll do a simple demonstration of a few moves:

            // 1. Red tries to draw (though it's already got 5).
            model.drawCardIfPossible();
            System.out.println(textView);

            // 2. Red places the first card in their hand at (0, 0), if possible
            try {
                model.placeCard(0, 0, 0);
            } catch (Exception e) {
                System.out.println("Red can't place at (0,0): " + e.getMessage());
                model.pass(); // pass instead
            }
            System.out.println(textView);

            // 3. Blue turn - draw
            model.drawCardIfPossible();
            System.out.println(textView);

            // 4. Attempt to place a card for Blue at (0,4)
            try {
                model.placeCard(0, 0, 4);
            } catch (Exception e) {
                System.out.println("Blue can't place at (0,4): " + e.getMessage());
                model.pass();
            }
            System.out.println(textView);

            // 5. Red passes
            model.pass();
            System.out.println(textView);

            // 6. Blue passes => triggers game over
            model.pass();
            System.out.println(textView);

            System.out.println("Text-based demonstration done.");

            // 2) GUI demonstration:
            // If you want to show the Swing GUI after that sequence:
            IPawnsBoardGuiView guiView = new PawnsBoardSwingView(roModel);
            IPawnsBoardController controller = new StubController();
            guiView.setController(controller);
            guiView.makeVisible();

            System.out.println("=== GUI View Demo ===");
            System.out.println("A GUI window is now open.\n"
                    + "Use mouse clicks on the board or hand.\n"
                    + "Press ENTER to confirm or 'P' to pass.\n"
                    + "See console for stub output.");

        }
        catch (FileNotFoundException e) {
            System.err.println("Could not find deck file: " + e.getMessage());
        }
    }
}


