package cs3500.pawnsboard.view;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.controller.StubController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.CellType;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.InfluenceGrid;
import cs3500.pawnsboard.model.PawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyCell;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

import static org.junit.Assert.assertEquals;

/**
 * A minimal "smoke test" for PawnsBoardSwingView.
 * In a normal scenario, we'd do more manual testing or use special libraries for GUI testing.
 */
public class PawnsBoardSwingViewTest {

  @Test
  public void testConstructAndShow() throws Exception {
    // We must run GUI code on the Swing event dispatch thread (EDT) to avoid errors
    SwingUtilities.invokeAndWait(() -> {

      // Create a small model (2x2) with 2 cards
      List<Card> deck = new ArrayList<>();
      // Construct minimal card
      char[][] influence = {
              {'X', 'X', 'I', 'X', 'X'},
              {'X', 'X', 'I', 'X', 'X'},
              {'I', 'I', 'C', 'I', 'I'},
              {'X', 'X', 'I', 'X', 'X'},
              {'X', 'X', 'I', 'X', 'X'}
      };
      InfluenceGrid ig = new InfluenceGrid(influence);
      deck.add(new Card("Alpha", 1, 2, ig));
      deck.add(new Card("Beta", 1, 2, ig));

      IPawnsBoardModel model = new PawnsBoardModel(2, 3,
              new ArrayList<>(deck), new ArrayList<>(deck), 1);

      ReadOnlyPawnsBoardModel roModel = model;

      IPawnsBoardGuiView guiView = new PawnsBoardSwingView(roModel);
      IPawnsBoardController controller = new StubController();
      guiView.setController(controller);

      // Just verify no exceptions
      guiView.makeVisible();
      guiView.refresh();
    });
  }

  @Test
  public void testBoardPanelMouseListener() throws Exception {
    // Quick check: The BoardPanel should have 1 or more mouse listeners
    SwingUtilities.invokeAndWait(() -> {
      ReadOnlyPawnsBoardModel emptyModel = new ReadOnlyPawnsBoardModel() {
        @Override
        public int getRows() {
          return 2;
        }

        @Override
        public int getCols() {
          return 2;
        }

        @Override
        public PlayerColor getCurrentPlayer() {
          return PlayerColor.RED;
        }

        @Override
        public boolean isGameOver() {
          return false;
        }

        @Override
        public PlayerColor getWinner() {
          return null;
        }

        @Override
        public ReadOnlyCell getCellState(int row, int col) {
          // Return an empty cell
          return new ReadOnlyCell(CellType.EMPTY, null, 0, "");
        }

        @Override
        public List<Card> getCurrentPlayerHand() {
          return new ArrayList<>();
        }

        @Override
        public int getRowScore(int row, PlayerColor color) {
          return 0;
        }

        @Override
        public int getTotalScore(PlayerColor color) {
          return 0;
        }

        @Override
        public boolean canPlaceCard(int handIndex, int row, int col) {
          return false;
        }

        @Override
        public int getDeckSize(PlayerColor color) {
          return 0;
        }
      };

      BoardPanel board = new BoardPanel(emptyModel);
      assertEquals("Expected exactly 1 mouse listener or so", 1, board.getMouseListeners().length);
    });
  }

  @Test
  public void testHandPanelMouseListener() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      ReadOnlyPawnsBoardModel emptyModel = new ReadOnlyPawnsBoardModel() {
        @Override
        public int getRows() {
          return 2;
        }

        @Override
        public int getCols() {
          return 2;
        }

        @Override
        public PlayerColor getCurrentPlayer() {
          return PlayerColor.RED;
        }

        @Override
        public boolean isGameOver() {
          return false;
        }

        @Override
        public PlayerColor getWinner() {
          return null;
        }

        @Override
        public ReadOnlyCell getCellState(int row, int col) {
          return new ReadOnlyCell(CellType.EMPTY, null, 0, "");
        }

        @Override
        public List<Card> getCurrentPlayerHand() {
          return new ArrayList<>();
        }

        @Override
        public int getRowScore(int row, PlayerColor color) {
          return 0;
        }

        @Override
        public int getTotalScore(PlayerColor color) {
          return 0;
        }

        @Override
        public boolean canPlaceCard(int handIndex, int row, int col) {
          return false;
        }
      };

      HandPanel handPanel = new HandPanel(emptyModel);
      assertEquals("Expected exactly 1 mouse listener or so", 1, handPanel.getMouseListeners().length);
    });
  }
}
