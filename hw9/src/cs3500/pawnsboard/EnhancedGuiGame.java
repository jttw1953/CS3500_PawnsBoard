package cs3500.pawnsboard;

import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.*;

import cs3500.pawnsboard.controller.HumanPlayer;
import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.controller.PawnsBoardController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.EnhancedPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.util.DeckReader;
import cs3500.pawnsboard.view.EnhancedPawnsBoardSwingView;

public class EnhancedGuiGame {
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 5) {
      System.err.println("Usage: EnhancedGuiGame <rows> <cols> <redDeck> <blueDeck> <handSize>");
      System.exit(1);
    }
    int rows = Integer.parseInt(args[0]);
    int cols = Integer.parseInt(args[1]);
    List<Card> redDeck = DeckReader.readDeckFromFile(args[2]);
    List<Card> blueDeck = DeckReader.readDeckFromFile(args[3]);
    int handSize = Integer.parseInt(args[4]);

    // 1) build the enhanced model
    EnhancedPawnsBoardModel model =
            new EnhancedPawnsBoardModel(rows, cols, redDeck, blueDeck, handSize);
    ReadOnlyPawnsBoardModel roModel = model;

    // 2) create two Enhanced views
    EnhancedPawnsBoardSwingView redView = new EnhancedPawnsBoardSwingView(roModel);
    EnhancedPawnsBoardSwingView blueView = new EnhancedPawnsBoardSwingView(roModel);

    // 3) make controllers for RED and BLUE
    IPawnsBoardController redCtrl =
            new PawnsBoardController(model, redView, PlayerColor.RED, new HumanPlayer());
    IPawnsBoardController blueCtrl =
            new PawnsBoardController(model, blueView, PlayerColor.BLUE, new HumanPlayer());

    redView.setController(redCtrl);
    blueView.setController(blueCtrl);

    // 4) show them on the EDT
    SwingUtilities.invokeLater(() -> {
      redView.makeVisible();
      blueView.makeVisible();
    });
  }
}
