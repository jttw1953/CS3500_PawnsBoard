package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.InfluenceGrid;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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


  /**
   * Constructs a HandPanel that visually displays the current player's hand of cards.
   * This panel is part of the GUI view for the Pawns Board game and is positioned at the
   * bottom of the window.
   *
   * @param model the read-only view of the game model, used to retrieve the current player's hand
   * @throws IllegalArgumentException if any of the model is null
   */
  public HandPanel(ReadOnlyPawnsBoardModel model) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model given to HandPanel is null");
    }
    this.model = model;
    this.setBackground(Color.WHITE);

    // Force a decent height so it’s visible
    this.setPreferredSize(new Dimension(800, 150));

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        List<ICard> hand = model.getCurrentPlayerHand();
        if (hand.isEmpty()) {
          return;
        }

        // We'll assume each card is displayed in a rectangle.
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Distribute the cards horizontally across the panel
        int cardWidth = (panelWidth / Math.max(hand.size(), 1));
        int cardHeight = panelHeight - 10; // a little padding

        int cardIndex = e.getX() / cardWidth;

        if (cardIndex >= 0 && cardIndex < hand.size()) {
          // if it's already selected, deselect it
          if (cardIndex == selectedCardIndex) {
            selectedCardIndex = -1;
          } else {
            selectedCardIndex = cardIndex;
          }

          // Inform the controller
          if (parentView != null && parentView.getController() != null) {
            parentView.getController().onCardClicked(cardIndex);
          }
          repaint();
        }
      }
    });
  }

  /**
   * Sets the parent view reference for this panel.
   * @param view the parent that contains this panel
   */
  public void setView(PawnsBoardSwingView view) {
    this.parentView = view;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // Draw the current player's hand
    List<ICard> hand = model.getCurrentPlayerHand();
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

      // Draw background based on current player color
      PlayerColor current = model.getCurrentPlayer();
      if (current == PlayerColor.RED) {
        g2.setColor(new Color(255, 180, 180)); // light red
      } else {
        g2.setColor(new Color(180, 180, 255)); // light blue
      }
      g2.fillRect(x, y, cardW, cardH);

      // Draw the outline
      g2.setColor(Color.BLACK);
      g2.drawRect(x, y, cardW, cardH);

      // Draw the 5x5 influence grid
      ICard c = hand.get(i);
      InfluenceGrid ig = (InfluenceGrid) c.getInfluenceGrid();
      if (ig != null) {
        int subW = cardW / 5;
        int subH = cardH / 5;
        for (int row = 0; row < 5; row++) {
          for (int col = 0; col < 5; col++) {
            char cellVal = ig.getCell(row, col);
            Color fillColor;
            if (cellVal == 'C') {
              fillColor = Color.BLACK; // center
            } else if (cellVal == 'I') {
              fillColor = Color.ORANGE; // influenced cell
            } else {
              fillColor = Color.WHITE;  // no influence (or 'X')
            }
            int subX = x + col * subW;
            int subY = y + row * subH;
            g2.setColor(fillColor);
            g2.fillRect(subX, subY, subW, subH);
            // Draw border around each sub-cell for clarity
            g2.setColor(Color.GRAY);
            g2.drawRect(subX, subY, subW, subH);
          }
        }
      }

      // Draw card info text near the bottom center of the card
      g2.setColor(Color.BLACK);
      String display = c.getName() + " (cost=" + c.getCost() + ", val=" + c.getValue() + ")";
      FontMetrics fm = g2.getFontMetrics();
      int textWidth = fm.stringWidth(display);
      int textHeight = fm.getAscent();
      int textX = x + (cardW - textWidth) / 2;
      int textY = y + cardH - (textHeight / 2);
      g2.drawString(display, textX, textY);

      // Now, if this card is selected, draw a thick cyan border over it.
      if (i == selectedCardIndex) {
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(x, y, cardW, cardH);
        // Reset stroke to default (optional)
        g2.setStroke(new BasicStroke(1));
      }
    }
  }

}
