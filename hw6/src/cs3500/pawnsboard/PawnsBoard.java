package cs3500.pawnsboard;

import java.io.FileNotFoundException;
import java.util.List;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.controller.StubController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.util.DeckReader;
import cs3500.pawnsboard.view.IPawnsBoardGuiView;
import cs3500.pawnsboard.view.PawnsBoardSwingView;

/**
 * The main entry point for a demonstration of PawnsBoard.
 */
public class PawnsBoard {
  /**
   * The main entry point for a demonstration of the PawnsBoard game.
   * This method initializes the game model, loads the deck configuration,
   * and runs a simple demonstration of gameplay.
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    try {
      String path = "docs/deck.config";
      List<Card> deck = DeckReader.readDeckFromFile(path);

      // Build the model: 5x7 board, each player gets the same deck, initial hand size=5
      IPawnsBoardModel model = new PawnsBoardModel(
              5, 7,
              deck, // red deck
              deck, // blue deck
              5
      );

      // No moves, no passes => still Red's turn, no cards placed

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


