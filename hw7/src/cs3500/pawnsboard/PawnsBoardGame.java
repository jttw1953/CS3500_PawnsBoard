package cs3500.pawnsboard;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.controller.PawnsBoardController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.util.DeckReader;
import cs3500.pawnsboard.view.PawnsBoardSwingView;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Main class for a fully playable two-player Pawns Board game.
 * This version uses one deck configuration file for both players.
 */
public class PawnsBoardGame {
    public static void main(String[] args) {
        // Path to your single deck config file in the docs directory.
        String deckPath = "docs/deck.config";

        try {
            // Load the deck from file.
            List<Card> deck = DeckReader.readDeckFromFile(deckPath);

            // Create the model.
            // Here we create a board with 5 rows and 7 columns (columns must be odd)
            // and set the initial hand size to 5.
            IPawnsBoardModel model = new PawnsBoardModel(
                    5,          // rows
                    7,          // columns
                    deck,       // red deck (using the same deck for both players)
                    deck,       // blue deck
                    5           // initial hand size
            );

            // If your view requires a read-only version, wrap the model accordingly.
            ReadOnlyPawnsBoardModel roModel = model;

            // Create two views (windows) for each player.
            PawnsBoardSwingView redView = new PawnsBoardSwingView(roModel);
            PawnsBoardSwingView blueView = new PawnsBoardSwingView(roModel);

            // Create two controllers, one for each player.
            IPawnsBoardController redController =
                    new PawnsBoardController(model, redView, PlayerColor.RED);
            IPawnsBoardController blueController =
                    new PawnsBoardController(model, blueView, PlayerColor.BLUE);

            // Link each view with its controller.
            redView.setController(redController);
            blueView.setController(blueController);

            // Make both windows visible.
            redView.makeVisible();
            blueView.makeVisible();

        } catch (FileNotFoundException e) {
            System.err.println("Could not load deck file: " + e.getMessage());
            System.exit(1);
        }
    }
}
