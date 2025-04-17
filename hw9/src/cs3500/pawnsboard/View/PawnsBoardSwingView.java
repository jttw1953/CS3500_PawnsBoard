package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Controller.IPawnsBoardController;
import cs3500.pawnsboard.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** Main frame for one player; supports live high‑contrast toggle. */
public class PawnsBoardSwingView extends JFrame
        implements IPawnsBoardGuiView {

    private final ReadOnlyPawnsBoardModel model;
    private IPawnsBoardController controller;

    private final BoardPanel boardPanel;
    private final HandPanel  handPanel;
    private final JLabel     statusLabel;

    private ColorScheme scheme = new DefaultScheme();

    public PawnsBoardSwingView(ReadOnlyPawnsBoardModel model) {
        super("Pawns Board Game");
        this.model = model;

        /* -------------------------------------------------- */
        /* frame basics                                       */
        /* -------------------------------------------------- */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(scheme.boardBackground()); // fills unused gaps

        /* -------------------------------------------------- */
        /* board + hand panels                                */
        /* -------------------------------------------------- */
        boardPanel = new BoardPanel(model, scheme);
        boardPanel.setView(this);
        add(boardPanel, BorderLayout.CENTER);

        handPanel  = new HandPanel(model, scheme);
        handPanel.setView(this);
        add(handPanel, BorderLayout.SOUTH);

        /* -------------------------------------------------- */
        /* high‑contrast toggle (per‑window)                  */
        /* -------------------------------------------------- */
        JCheckBox hcBox = new JCheckBox("High‑contrast");
        hcBox.addActionListener(e -> {
            scheme = hcBox.isSelected() ? new HighContrastScheme()
                    : new DefaultScheme();
            boardPanel.setScheme(scheme);
            handPanel.setScheme(scheme);
            getContentPane().setBackground(scheme.boardBackground());
            refresh();                 // update status colour if desired later
        });
        add(hcBox, BorderLayout.WEST);

        /* -------------------------------------------------- */
        /* status‑bar                                         */
        /* -------------------------------------------------- */
        statusLabel = new JLabel();
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(statusLabel, BorderLayout.NORTH);

        /* -------------------------------------------------- */
        /* key shortcuts                                      */
        /* -------------------------------------------------- */
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (controller == null) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> controller.onConfirm();
                    case KeyEvent.VK_P     -> controller.onPass();
                }
            }
        });

        pack();
        setSize(880, 640);
        setLocationRelativeTo(null);
        refresh();
    }

    /* ---------------- IPawnsBoardGuiView ---------------- */
    @Override public void setController(IPawnsBoardController c) { controller = c; }

    @Override public void makeVisible() {
        setVisible(true);
        requestFocusInWindow();              // make key‑bindings work immediately
    }

    @Override public void refresh() {
        if (!model.isGameOver()) {
            statusLabel.setText(
                    "Current: " + model.getCurrentPlayer() +
                            " | RedDeck: "  + model.getDeckSize(PlayerColor.RED) +
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
    }

    /* helpers for sub‑components */
    public IPawnsBoardController    getController() { return controller; }
    public ReadOnlyPawnsBoardModel  getModel()      { return model;     }
}
