package cs3500.pawnsboard;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.controller.PawnsBoardController;
import cs3500.pawnsboard.controller.Player;
import cs3500.pawnsboard.controller.HumanPlayer;
import cs3500.pawnsboard.controller.AIPlayer;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.strategy.FillFirstStrategy;
import cs3500.pawnsboard.strategy.MaxRowScoreStrategy;
import cs3500.pawnsboard.strategy.ControlBoardStrategy;
import cs3500.pawnsboard.strategy.CompositeStrategy;
import cs3500.pawnsboard.util.DeckReader;
import cs3500.pawnsboard.view.PawnsBoardSwingView;

// Provider code
import cs3500.pawnsboard.provider.controller.ProviderControllerAdapter;
import cs3500.pawnsboard.provider.model.ProviderModelAdapter;
import cs3500.pawnsboard.provider.view.ProviderViewImpl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

/**
 * A main class that:
 * - Takes four args: <redDeckPath> <blueDeckPath> <redType> <blueType>
 * - Sets up Red with your model & view (human or AI).
 * - Sets up Blue with the provider code (the "someone else's" code).
 * - Preserves old command-line usage while fulfilling HW8 requirements.
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
    List<Card> redDeck, blueDeck;
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
    // We can pass this read-only model to our SwingView
    ReadOnlyPawnsBoardModel roModel = model;

    // 5) Build a Player for Red (either HumanPlayer or AIPlayer)
    Player redPlayer = buildPlayer(redType);

    // 6) Build your Swing view + your controller for Red
    PawnsBoardSwingView redView = new PawnsBoardSwingView(roModel);
    IPawnsBoardController redController =
            new PawnsBoardController(model, redView, PlayerColor.RED, redPlayer);
    redView.setController(redController);
    redView.makeVisible();

    // -------------------------------------------------------------------------
    // 7) Blue is "someone else's code" from the provider
    // -------------------------------------------------------------------------

    // Build a Player for Blue if you want to pass an AI strategy
    // or create a HumanPlayer if the provider code can handle it
    Player bluePlayer = buildPlayer(blueType);

    // a) Adapt the model for the provider
    ProviderModelAdapter providerModelAdapter =
            new ProviderModelAdapter((PawnsBoardModel) model);

    // b) Create the provider’s view
    ProviderViewImpl providerBlueView = new ProviderViewImpl(providerModelAdapter);

    // c) Make your real controller for the Blue player
    IPawnsBoardController myBlueController =
            new PawnsBoardController(model, null, PlayerColor.BLUE, bluePlayer);

    // d) Wrap your controller so the provider’s view can talk to it
    ProviderControllerAdapter providerBlueController =
            new ProviderControllerAdapter(myBlueController);

    // e) Link them
    providerBlueView.setController(providerBlueController);
    providerBlueView.makeVisible();

    // That’s it! Both windows are visible and the game is ready to go.
    // If either side is AI, it should take a turn automatically if valid.

    // (You can forcibly trigger an immediate AI move if your Player design
    //  supports it, e.g. by calling a "checkAiMove()" method. If not needed, ignore.)
  }

  /**
   * Helper that returns a Player (human or AI) depending on the given strategy name.
   */
  private static Player buildPlayer(String type) {
    // "human" => new HumanPlayer()
    if ("human".equals(type)) {
      return new HumanPlayer();
    }

    // If it's anything else, interpret as an AI using one of your strategies:
    Strategy strategy = parseStrategy(type);
    if (strategy == null) {
      // parseStrategy returns null for unrecognized => default to human
      return new HumanPlayer();
    }
    return new AIPlayer(strategy);
  }

  /**
   * Convert a string (e.g. "human", "fillfirst", "maxrow", "control", "composite")
   * into a Strategy object or null if "human" or unknown.
   */
  private static Strategy parseStrategy(String s) {
    switch (s) {
      case "human":
        return null; // means "do a HumanPlayer"
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
