package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Controller.IPawnsBoardController;
import cs3500.pawnsboard.Model.PlayerColor;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EnhancedPawnsBoardSwingView extends JFrame implements IPawnsBoardGuiView {
    private final ReadOnlyPawnsBoardModel model;
    private IPawnsBoardController controller;

    private final EnhancedBoardPanel boardPanel;
    private final EnhancedHandPanel  handPanel;
    private final JLabel             statusLabel;

    private ColorScheme scheme = new DefaultScheme();

    public EnhancedPawnsBoardSwingView(ReadOnlyPawnsBoardModel model) {
        super("Pawns Board Game (Enhanced)");
        this.model = model;

        // allow the frame to receive key events
        this.setFocusable(true);
        this.requestFocusInWindow();

        /* -------------------------------------------------- */
        /* frame basics                                       */
        /* -------------------------------------------------- */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(scheme.boardBackground());

        /* -------------------------------------------------- */
        /* status‑bar                                         */
        /* -------------------------------------------------- */
        statusLabel = new JLabel();
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(statusLabel, BorderLayout.NORTH);

        /* -------------------------------------------------- */
        /* enhanced board + hand panels                       */
        /* -------------------------------------------------- */
        boardPanel = new EnhancedBoardPanel(model, scheme);
        boardPanel.setView(this);
        add(boardPanel, BorderLayout.CENTER);

        handPanel = new EnhancedHandPanel(model, scheme);
        handPanel.setView(this);
        add(handPanel, BorderLayout.SOUTH);

        /* -------------------------------------------------- */
        /* high‑contrast toggle                                */
        /* -------------------------------------------------- */
        JCheckBox hcBox = new JCheckBox("High‑contrast");
        hcBox.addActionListener(e -> {
            scheme = hcBox.isSelected()
                    ? new HighContrastScheme()
                    : new DefaultScheme();

            boardPanel.setScheme(scheme);
            handPanel.setScheme(scheme);
            getContentPane().setBackground(scheme.boardBackground());
            statusLabel.setForeground(scheme.scoreText());

            refresh();
        });
        add(hcBox, BorderLayout.WEST);

        /* -------------------------------------------------- */
        /* key shortcuts                                       */
        /* -------------------------------------------------- */
        JComponent root = this.getRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "confirmMove");
        root.getActionMap().put("confirmMove", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.onConfirm();
                }
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("P"), "passMove");
        root.getActionMap().put("passMove", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.onPass();
                }
            }
        });

        /* -------------------------------------------------- */
        /* finalize                                           */
        /* -------------------------------------------------- */
        pack();
        setSize(880, 640);
        setLocationRelativeTo(null);
        refresh();
    }

    /* ---------------- IPawnsBoardGuiView ---------------- */

    @Override
    public void setController(IPawnsBoardController c) {
        this.controller = c;
    }

    @Override
    public void makeVisible() {
        setVisible(true);
        requestFocusInWindow();
    }

    @Override
    public void refresh() {
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
                            : "Game over – winner: " + w
            );
        }

        boardPanel.repaint();
        handPanel.repaint();
        this.requestFocusInWindow();
    }

    @Override
    public IPawnsBoardController getController() {
        return this.controller;
    }
}


