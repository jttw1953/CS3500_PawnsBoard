package cs3500.pawnsboard;

import cs3500.pawnsboard.Controller.*;
import cs3500.pawnsboard.Model.*;
import cs3500.pawnsboard.Strategy.*;
import cs3500.pawnsboard.Util.DeckReader;
import cs3500.pawnsboard.View.PawnsBoardSwingView;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Launches a two–player PawnsBoard match **using only our own code**.
 *
 * Usage:
 *   java -jar pawnsboard.jar <redDeck> <blueDeck> <redType> <blueType>
 *
 * <redType>/<blueType> := human | fillfirst | maxrow | control | composite
 */
public final class PawnsBoardGame {

    public static void main(String[] args) {

        /* ------------------------------------------------------------------ */
        /* 0 ‑‑ validate & parse command‑line                                 */
        /* ------------------------------------------------------------------ */
        if (args.length != 4) {
            System.exit(1);
        }
        final String redDeckPath  = args[0];
        final String blueDeckPath = args[1];
        final String redType      = args[2].toLowerCase();
        final String blueType     = args[3].toLowerCase();

        List<Card> redDeck, blueDeck;
        try {
            redDeck  = DeckReader.readDeckFromFile(redDeckPath);
            blueDeck = DeckReader.readDeckFromFile(blueDeckPath);
        } catch (FileNotFoundException e) {
            System.err.println("Deck file not found: " + e.getMessage());
            return;
        }

        /* ------------------------------------------------------------------ */
        /* 1 ‑‑ model                                                         */
        /* ------------------------------------------------------------------ */
        IPawnsBoardModel model = new PawnsBoardModel(
                5,           // rows
                7,           // cols (must be odd)
                redDeck,
                blueDeck,
                5            // initial hand size
        );
        ReadOnlyPawnsBoardModel roModel = model;   // shared with both views

        /* ------------------------------------------------------------------ */
        /* 2 ‑‑ build controllers & views                                     */
        /* ------------------------------------------------------------------ */
        Player redPlayer  = makePlayer(redType);
        Player bluePlayer = makePlayer(blueType);

        PawnsBoardSwingView redView  = new PawnsBoardSwingView(roModel);
        PawnsBoardSwingView blueView = new PawnsBoardSwingView(roModel);

        IPawnsBoardController redCtrl  =
                new PawnsBoardController(model, redView, PlayerColor.RED,  redPlayer);
        IPawnsBoardController blueCtrl =
                new PawnsBoardController(model, blueView, PlayerColor.BLUE, bluePlayer);

        redView.setController(redCtrl);
        blueView.setController(blueCtrl);

        /* ------------------------------------------------------------------ */
        /* 3 ‑‑ show windows (on the EDT) & kick off AI if needed             */
        /* ------------------------------------------------------------------ */
        javax.swing.SwingUtilities.invokeLater(() -> {
            redView.makeVisible();
            blueView.makeVisible();

            triggerAiIfPossible(redCtrl);
            triggerAiIfPossible(blueCtrl);
        });
    }

    /* -------------------------------------------------------------------- */
    /* helpers                                                              */
    /* -------------------------------------------------------------------- */
    private static Player makePlayer(String token) {
        if ("human".equals(token)) {
            return new HumanPlayer();
        }

<<<<<<< HEAD
        Strategy s = null;
=======
        Strategy s;
>>>>>>> fc20e28c3fc012fc29487ae12fbd05fe29402295
        switch (token) {
            case "fillfirst":
                s = new FillFirstStrategy();
                break;
            case "maxrow":
                s = new MaxRowScoreStrategy();
                break;
            case "control":
                s = new ControlBoardStrategy();
                break;
            case "composite":
                s = new CompositeStrategy(
<<<<<<< HEAD
                        List.of(new FillFirstStrategy(), new MaxRowScoreStrategy()));
                break;
            default:
                // Unknown strategy, fall back to human
                break;
=======
                        List.of(new FillFirstStrategy(),
                                new MaxRowScoreStrategy()));
                break;
            default:
                s = null;   // unknown ⇒ treat as human
>>>>>>> fc20e28c3fc012fc29487ae12fbd05fe29402295
        }

        return (s == null) ? new HumanPlayer() : new AIPlayer(s);
    }

    /** If the concrete controller can auto‑move an AI, call it. */
    private static void triggerAiIfPossible(IPawnsBoardController ctrl) {
        if (ctrl instanceof PawnsBoardController) {
            ((PawnsBoardController) ctrl).forceAiMoveIfMyTurn();
        }
    }
}
