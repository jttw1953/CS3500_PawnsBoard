package cs3500.pawnsboard.view;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A Swing-based view of the PawnsBoard.
 * It creates a JFrame with:
 * - BoardPanel (the main grid of cells)
 * - HandPanel  (cards for the current player)
 * - A status area at the top, showing current player, deck sizes, & maybe scores
 */
public class PawnsBoardSwingView extends JFrame implements IPawnsBoardGuiView {
  private final ReadOnlyPawnsBoardModel model;
  private IPawnsBoardController controller;

  private final BoardPanel boardPanel;
  private final HandPanel handPanel;
  private final JLabel statusLabel; // displays current player, deck sizes, etc.

  /**
   * Constructs a new {@code PawnsBoardSwingView}, a GUI-based view for the Pawns Board game.
   * This view uses Java Swing components to display the current board state, the player's hand,
   * and a status area for turn and deck information.
   *
   * @param model the read-only view of the game model used to initialize board and hand displays
   * @throws NullPointerException if the model is null
   */
  public PawnsBoardSwingView(ReadOnlyPawnsBoardModel model) {
    super("Pawns Board Game");
    this.model = model;
    this.controller = null; // not set until setController is called

    // Basic frame settings
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // Status label at the top
    // Initially, we display current player. Deck sizes will be updated in refresh().
    statusLabel = new JLabel("Current player: " + model.getCurrentPlayer());
    statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    this.add(statusLabel, BorderLayout.NORTH);

    // Board panel in the center
    boardPanel = new BoardPanel(model);
    // Give boardPanel a reference to this view (or to the controller) if needed
    boardPanel.setView(this);
    this.add(boardPanel, BorderLayout.CENTER);

    // Hand panel at the bottom
    handPanel = new HandPanel(model);
    handPanel.setView(this);
    this.add(handPanel, BorderLayout.SOUTH);

    // Add a key listener to capture pass/confirm keys
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (controller == null) {
          return; // no controller => do nothing
        }
        int code = e.getKeyCode();
        // Example: Enter => confirm, P => pass
        if (code == KeyEvent.VK_ENTER) {
          controller.onConfirm(); // prints debug in stub
        } else if (code == KeyEvent.VK_P) {
          controller.onPass(); // prints debug in stub
        }
      }
    });

    this.pack();
    this.setSize(800, 600); // initial size
    this.setLocationRelativeTo(null); // center on screen
    this.refresh(); // <— force an immediate refresh
  }

  @Override
  public void setController(IPawnsBoardController controller) {
    this.controller = controller;
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
    // Request focus so key events register
    this.requestFocusInWindow();
  }

  @Override
  public void refresh() {
    if (!model.isGameOver()) {
      // Retrieve deck sizes
      int redDeckSize = model.getDeckSize(PlayerColor.RED);
      int blueDeckSize = model.getDeckSize(PlayerColor.BLUE);

      // Update status label
      statusLabel.setText("Current player: " + model.getCurrentPlayer()
              + " | Red Deck: " + redDeckSize
              + " | Blue Deck: " + blueDeckSize);
    } else {
      // If game is over, show final message
      PlayerColor winner = model.getWinner();
      if (winner == null) {
        statusLabel.setText("Game over: Tie!");
      } else {
        statusLabel.setText("Game over! Winner: " + winner);
      }
    }

    // Repaint sub-panels to reflect the updated state
    boardPanel.repaint();
    handPanel.repaint();

    // Optionally repaint the entire window frame
    this.repaint();
  }


  /**
   * Expose the controller so sub-panels can notify it of clicks.
   * Alternatively, you could pass the controller reference to the sub-panels directly.
   */
  public IPawnsBoardController getController() {
    return this.controller;
  }

  /**
   * Accessor for the model, so sub-panels can retrieve data or compute
   * row/col from mouse clicks if needed.
   */
  public ReadOnlyPawnsBoardModel getModel() {
    return this.model;
  }
}
