package cs3500.pawnsboard.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import cs3500.pawnsboard.controller.IPawnsBoardController;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Main frame for one player; supports live high‑contrast toggle.
 */
public class PawnsBoardSwingView extends JFrame
        implements IPawnsBoardGuiView {

  private final ReadOnlyPawnsBoardModel model;
  private IPawnsBoardController controller;

  private final BoardPanel boardPanel;
  private final HandPanel handPanel;
  private final JLabel statusLabel;

  private ColorScheme scheme = new DefaultScheme();

  public PawnsBoardSwingView(ReadOnlyPawnsBoardModel model) {
    super("Pawns Board Game");

    // allow the frame to receive key events
    this.setFocusable(true);
    this.requestFocusInWindow();

    this.model = model;

    /* -------------------------------------------------- */
    /* frame basics                                       */
    /* -------------------------------------------------- */
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    getContentPane().setBackground(scheme.boardBackground());

    /* -------------------------------------------------- */
    /* status‑bar  (create BEFORE the HC checkbox)        */
    /* -------------------------------------------------- */
    statusLabel = new JLabel();
    statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    add(statusLabel, BorderLayout.NORTH);

    /* -------------------------------------------------- */
    /* board + hand panels                                */
    /* -------------------------------------------------- */
    boardPanel = new BoardPanel(model, scheme);
    boardPanel.setView(this);
    add(boardPanel, BorderLayout.CENTER);

    handPanel = new HandPanel(model, scheme);
    handPanel.setView(this);
    add(handPanel, BorderLayout.SOUTH);

    /* -------------------------------------------------- */
    /* high‑contrast toggle (per‑window)                  */
    /* -------------------------------------------------- */
    JCheckBox hcBox = new JCheckBox("High‑contrast");
    hcBox.addActionListener(e -> {
      scheme = hcBox.isSelected()
              ? new HighContrastScheme()
              : new DefaultScheme();

      boardPanel.setScheme(scheme);
      handPanel.setScheme(scheme);
      getContentPane().setBackground(scheme.boardBackground());
      statusLabel.setForeground(scheme.scoreText());   // now safe

      refresh();
    });
    add(hcBox, BorderLayout.WEST);

    // in your PawnsBoardSwingView constructor, *instead* of addKeyListener(...)
    JComponent root = this.getRootPane();

    // map the ENTER key (no modifiers) to a “confirm” action
    root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ENTER"), "confirmMove");
    root.getActionMap().put("confirmMove", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controller != null) {
          controller.onConfirm();
        }
      }
    });

    // similarly for “P” → onPass()
    root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("P"), "passMove");
    root.getActionMap().put("passMove", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controller != null) {
          controller.onPass();
        }
      }
    });

    pack();
    setSize(880, 640);
    setLocationRelativeTo(null);
    refresh();
  }


  /* ---------------- IPawnsBoardGuiView ---------------- */
  @Override
  public void setController(IPawnsBoardController c) {
    controller = c;
  }

  @Override
  public void makeVisible() {
    setVisible(true);
    requestFocusInWindow();              // make key‑bindings work immediately
  }

  @Override
  public void refresh() {
    if (!model.isGameOver()) {
      statusLabel.setText(
              "Current: " + model.getCurrentPlayer() +
                      " | RedDeck: " + model.getDeckSize(PlayerColor.RED) +
                      " | BlueDeck: " + model.getDeckSize(PlayerColor.BLUE)
      );
    } else {
      PlayerColor w = model.getWinner();
      statusLabel.setText(
              (w == null) ? "Game over – tie!"
                      : "Game over – winner: " + w);
    }

    /* let children redraw */
    boardPanel.repaint();
    handPanel.repaint();

    // Re‑grab focus so our KeyListener will fire on Enter/P
    this.requestFocusInWindow();
  }

  /* helpers for sub‑components */
  public IPawnsBoardController getController() {
    return controller;
  }

  public ReadOnlyPawnsBoardModel getModel() {
    return model;
  }

}
