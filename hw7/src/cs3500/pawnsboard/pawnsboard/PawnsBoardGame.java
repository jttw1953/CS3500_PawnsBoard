package cs3500.pawnsboard;

import cs3500.pawnsboard.Controller.IPawnsBoardController;
import cs3500.pawnsboard.Controller.PawnsBoardController;
import cs3500.pawnsboard.Model.Card;
import cs3500.pawnsboard.Model.IPawnsBoardModel;
import cs3500.pawnsboard.Model.PawnsBoardModel;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Util.DeckReader;
import cs3500.pawnsboard.View.PawnsBoardSwingView;

// -- Provider imports --
import cs3500.pawnsboard.provider.model.ProviderModelAdapter;
import cs3500.pawnsboard.provider.controller.ProviderControllerAdapter;
import cs3500.pawnsboard.provider.view.ProviderViewImpl;

import java.io.FileNotFoundException;
import java.util.List;

public class PawnsBoardGame {
    public static void main(String[] args) {
        // Optionally parse arguments (deck paths, player types, etc.)
        // For now, we’ll just have a simple path to a deck config:
        String deckPath = "docs/deck.config";

        // Decide whether to use provider’s view for Player 2 (optional).
        // e.g. run:  java -jar pawnsboard.jar --provider2
        boolean useProviderForBlue = (args.length > 0 && args[0].equals("--provider2"));

        try {
            // 1) Read the deck
            List<Card> deck = DeckReader.readDeckFromFile(deckPath);

            // 2) Construct our real model
            IPawnsBoardModel model = new PawnsBoardModel(
                    5,  // rows
                    7,  // cols
                    deck, // red deck
                    deck, // blue deck
                    5   // initial hand size
            );

            // We'll use this read-only reference for our own view
            ReadOnlyPawnsBoardModel roModel = model;

            // --- Player 1 (RED) uses our own SwingView + our own controller ---
            PawnsBoardSwingView redView = new PawnsBoardSwingView(roModel);
            IPawnsBoardController redController =
                    new PawnsBoardController(model, redView, PlayerColor.RED);
            redView.setController(redController);
            redView.makeVisible();

            if (!useProviderForBlue) {
                // -- Player 2 uses YOUR old view + controller --

                PawnsBoardSwingView blueView = new PawnsBoardSwingView(roModel);
                IPawnsBoardController blueController =
                        new PawnsBoardController(model, blueView, PlayerColor.BLUE);
                blueView.setController(blueController);
                blueView.makeVisible();

            } else {
                // -- Player 2 uses PROVIDER’s code --

                // 1) providerModelAdapter
                ProviderModelAdapter providerModelAdapter =
                        new ProviderModelAdapter((PawnsBoardModel) model);

                // 2) The provider’s view (or your stub if you had to create one)
                ProviderViewImpl providerBlueView =
                        new ProviderViewImpl(providerModelAdapter);

                // 3) Your real controller for Player 2
                IPawnsBoardController myBlueController =
                        new PawnsBoardController(model, null, PlayerColor.BLUE);

                // 4) Wrap it so the provider’s view can call it
                ProviderControllerAdapter providerBlueController =
                        new ProviderControllerAdapter(myBlueController);

                // 5) Link them
                providerBlueView.setController(providerBlueController);
                providerBlueView.makeVisible();
            }


        } catch (FileNotFoundException e) {
            System.err.println("Could not load deck file: " + e.getMessage());
            System.exit(1);
        }
    }
}
