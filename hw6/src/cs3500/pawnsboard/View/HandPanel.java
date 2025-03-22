package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.Card;
import cs3500.pawnsboard.Model.PlayerColor;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A panel that displays the current player's hand of cards in a row.
 */
public class HandPanel extends JPanel {
    private final ReadOnlyPawnsBoardModel model;
    private PawnsBoardSwingView parentView;

    // Track which card index is selected
    private int selectedCardIndex = -1;

    public HandPanel(ReadOnlyPawnsBoardModel model) {
        super();
        this.model = model;
        this.setBackground(Color.WHITE);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                List<Card> hand = model.getCurrentPlayerHand();
                if (hand.isEmpty()) {
                    return;
                }

                // We'll assume each card is displayed in a rectangle.
                // Figure out which was clicked:
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // We'll distribute the cards horizontally across the panel
                int cardWidth = (panelWidth / Math.max(hand.size(),1));
                int cardHeight = panelHeight - 10; // a little padding

                int cardIndex = e.getX() / cardWidth;

                if (cardIndex >= 0 && cardIndex < hand.size()) {
                    // if it's already selected, deselect
                    if (cardIndex == selectedCardIndex) {
                        selectedCardIndex = -1;
                    }
                    else {
                        selectedCardIndex = cardIndex;
                    }

                    // tell the controller
                    if (parentView != null && parentView.getController() != null) {
                        parentView.getController().onCardClicked(cardIndex);
                    }

                    repaint();
                }
            }
        });
    }

    public void setView(PawnsBoardSwingView view) {
        this.parentView = view;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the current player's hand
        List<Card> hand = model.getCurrentPlayerHand();
        if (hand.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.drawString("No cards in hand", 10, 20);
            return;
        }

        int panelW = getWidth();
        int panelH = getHeight();
        int cardCount = hand.size();

        int cardW = panelW / cardCount;
        int cardH = panelH - 10; // some vertical padding

        for (int i = 0; i < cardCount; i++) {
            int x = i * cardW;
            int y = 0;

            // highlight if selected
            if (i == selectedCardIndex) {
                g2.setColor(Color.CYAN);
                g2.fillRect(x, y, cardW, cardH);
            }

            // outline
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, cardW, cardH);

            // fill color depends on the current player
            PlayerColor current = model.getCurrentPlayer();
            if (current == PlayerColor.RED) {
                g2.setColor(new Color(255, 180, 180)); // light red
            } else {
                g2.setColor(new Color(180, 180, 255)); // light blue
            }
            g2.fillRect(x+1, y+1, cardW-2, cardH-2);

            // Draw card info
            g2.setColor(Color.BLACK);
            Card c = hand.get(i);
            String display = c.getName() + " (cost=" + c.getCost() + ", val=" + c.getValue() + ")";
            g2.drawString(display, x + 5, y + cardH/2);
        }
    }
}
