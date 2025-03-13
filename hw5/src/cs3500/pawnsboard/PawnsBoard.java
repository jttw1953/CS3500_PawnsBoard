package cs3500.pawnsboard;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.DeckReader;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.view.TextualView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

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
      // Path to your config file in docs/ folder
      String path = "docs" + File.separator + "deck.config";
      File config = new File(path);
      List<Card> deck = DeckReader.readDeckFromFile(config);

      // Build the model: 3x5 board, each player gets the same deck, initial hand size=5
      IPawnsBoardModel model = new PawnsBoardModel(
              3, 5,
              deck, // red deck
              deck, // blue deck
              5
      );

      TextualView view = new TextualView(model);

      System.out.println("Initial state:");
      System.out.println(view.toString());

      // We'll do a simple demonstration of a few moves:

      // 1. Red tries to draw (though it's already got 5, but let's do it).
      model.drawCardIfPossible();
      System.out.println(view);

      // 2. Red places the first card in their hand at (0, 0) if possible.
      //    That cell has 1 Red pawn, so hopefully the cost is 1.
      try {
        model.placeCard(0, 0, 0);
      } catch (Exception e) {
        System.out.println("Red can't place at (0,0): " + e.getMessage());
        model.pass(); // pass instead
      }
      System.out.println(view);

      // 3. Blue turn - draw
      model.drawCardIfPossible();
      System.out.println(view);

      // 4. Let's attempt to place a card for Blue in (0,4),
      //    which has 1 Blue pawn if the cost is 1
      try {
        model.placeCard(0, 0, 4);
      } catch (Exception e) {
        System.out.println("Blue can't place at (0,4): " + e.getMessage());
        model.pass();
      }
      System.out.println(view);

      // keep going until no moves remain or we've done enough for demonstration
      // For a real demonstration, you'd either script out multiple moves
      // or do a loop that tries to place any valid card, else pass,
      // until gameOver.

      // We'll do forced passes to end quickly
      model.pass(); // Red passes
      System.out.println(view);

      model.pass(); // Blue passes => triggers game over
      System.out.println(view);

      System.out.println("Done demonstration.");

    } catch (FileNotFoundException e) {
      System.err.println("Could not find deck file!");
    }
  }
}

