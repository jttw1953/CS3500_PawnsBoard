package cs3500.pawnsboard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.strategy.*;
import cs3500.pawnsboard.util.DeckReader;
import cs3500.pawnsboard.view.PawnsBoardSwingView;
import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.provider.controller.ProviderControllerAdapter;
import cs3500.pawnsboard.provider.model.ProviderModelAdapter;
import cs3500.pawnsboard.provider.view.ProviderViewImpl;

/**
 * A main class that:
 * - Takes four args: <redDeckPath> <blueDeckPath> <redType> <blueType>
 * - Sets up Red with your view/controller + optional AI
 * - Sets up Blue using the provider's code (someone else's).
 * - Immediately checks if an AI can move (so you see it in action).
 */
public final class PawnsBoardGame {
  public static void main(String[] args) {
    // 1) Expect exactly four args
    if (args.length < 4) {
      System.err.println("Usage: java -jar pawnsboard.jar <redDeck> <blueDeck> <redType> <blueType>");
      System.err.println("Example: java -jar pawnsboard.jar docs/red.config docs/blue.config human fillfirst");
      System.exit(1);
    }

    // 2) Parse them
    String redDeckPath = args[0];
    String blueDeckPath = args[1];
    String redType = args[2].toLowerCase();   // "human", "fillfirst", ...
    String blueType = args[3].toLowerCase();  // "human", "maxrow", ...

    // 3) Read both decks
    List<Card> redDeck;
    List<Card> blueDeck;
    try {
      redDeck = DeckReader.readDeckFromFile(redDeckPath);
      blueDeck = DeckReader.readDeckFromFile(blueDeckPath);
    } catch (FileNotFoundException e) {
      System.err.println("Could not load deck file: " + e.getMessage());
      return;
    }

    // 4) Create your PawnsBoardModel with these decks
    IPawnsBoardModel model = new PawnsBoardModel(
            5,  // rows
            7,  // cols
            redDeck,
            blueDeck,
            5   // initial hand size
    );
    ReadOnlyPawnsBoardModel roModel = model; // for passing to your swing view

    // 5) Build strategy for Red, or null if "human"
    Strategy redStrategy = parseStrategy(redType);

    // 6) Build a swing view + your controller for Red
    PawnsBoardSwingView redView = new PawnsBoardSwingView(roModel);
    IPawnsBoardController redController =
            new PawnsBoardController(model, redView, PlayerColor.RED, redStrategy);
    redView.setController(redController);
    redView.makeVisible();

    // -------------------------------------------------------------------------
    // 7) Blue is "someone else's code" from the provider
    // -------------------------------------------------------------------------

    // (Optional) Build a strategy for Blue if your provider code can handle AI:
    Strategy blueStrategy = parseStrategy(blueType);
    // If your provider code can't do AI, just ignore blueStrategy or pass null

    // a) Adapt the model
    ProviderModelAdapter providerModelAdapter =
            new ProviderModelAdapter((PawnsBoardModel) model);

    // b) Create the provider’s view
    ProviderViewImpl providerBlueView = new ProviderViewImpl(providerModelAdapter);

    // c) Make *your* real controller for the Blue player
    IPawnsBoardController myBlueController =
            new PawnsBoardController(model, null, PlayerColor.BLUE, blueStrategy);

    // d) Wrap your controller so the provider’s view can talk to it
    ProviderControllerAdapter providerBlueController =
            new ProviderControllerAdapter(myBlueController);

    // e) Link them
    providerBlueView.setController(providerBlueController);
    providerBlueView.makeVisible();

    // -------------------------------------------------------------------------
    // 8) Force each side to "check if AI can move" so it goes immediately
    // -------------------------------------------------------------------------
    if (redController instanceof PawnsBoardController) {
      ((PawnsBoardController) redController).forceAiCheck();
    }
    if (myBlueController instanceof PawnsBoardController) {
      ((PawnsBoardController) myBlueController).forceAiCheck();
    }
  }

  /**
   * Convert a string (e.g. "human", "fillfirst", "maxrow", "control", "composite")
   * into a Strategy object or null if "human" or unknown.
   */
  private static Strategy parseStrategy(String s) {
    switch (s) {
      case "human":
        return null;
      case "fillfirst":
        return new FillFirstStrategy();
      case "maxrow":
        return new MaxRowScoreStrategy();
      case "control":
        return new ControlBoardStrategy();
      case "composite":
        // Example: fillfirst + maxrow
        List<Strategy> list = new ArrayList<>();
        list.add(new FillFirstStrategy());
        list.add(new MaxRowScoreStrategy());
        return new CompositeStrategy(list);
      default:
        System.out.println("Unknown strategy \"" + s + "\" => defaulting to human.");
        return null;
    }
  }
}
