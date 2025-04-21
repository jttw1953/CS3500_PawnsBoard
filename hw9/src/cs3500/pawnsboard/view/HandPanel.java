package cs3500.pawnsboard.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.InfluenceGrid;
import cs3500.pawnsboard.model.PlayerColor;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Shows the current player’s hand; all colours come from the active ColorScheme.
 */
public class HandPanel extends JPanel {

  final ReadOnlyPawnsBoardModel model;
  private IPawnsBoardGuiView parentView;
  private int selectedCardIndex = -1;

  ColorScheme scheme;

  protected ReadOnlyPawnsBoardModel getModel() {
    return this.model;
  }

  protected ColorScheme getScheme() {
    return this.scheme;
  }

  public HandPanel(ReadOnlyPawnsBoardModel model, ColorScheme scheme) {
    this.model = model;
    this.scheme = scheme;

    /* correct background = scheme, not hard‑coded white */
    setBackground(scheme.boardBackground());
    setPreferredSize(new Dimension(800, 150));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int cardW = getWidth() / Math.max(model.getCurrentPlayerHand().size(), 1);
        int idx = e.getX() / cardW;
        if (idx >= 0 && idx < model.getCurrentPlayerHand().size()) {
          selectedCardIndex = (idx == selectedCardIndex) ? -1 : idx;
          if (parentView != null && parentView.getController() != null) {
            parentView.getController().onCardClicked(idx);
          }
          repaint();
        }
      }
    });
    setFocusable(true);                // e.g. in BoardPanel and HandPanel

  }

  /* palette swap at runtime */
  public void setScheme(ColorScheme s) {
    this.scheme = (s == null) ? new DefaultScheme() : s;
    setBackground(scheme.boardBackground());
    repaint();
  }

  public void setView(IPawnsBoardGuiView v) {
    this.parentView = v;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    List<Card> hand = model.getCurrentPlayerHand();
    if (hand.isEmpty()) {
      g2.setColor(scheme.cardBorder());
      g2.drawString("No cards in hand", 10, 20);
      return;
    }

    int cardW = getWidth() / hand.size();
    int cardH = getHeight() - 10;

    for (int i = 0; i < hand.size(); i++) {
      int x = i * cardW, y = 0;

      /* card background by player colour */
      g2.setColor(model.getCurrentPlayer() == PlayerColor.RED
              ? scheme.handRedFill() : scheme.handBlueFill());
      g2.fillRect(x, y, cardW, cardH);

      /* outer border */
      g2.setColor(scheme.cardBorder());
      g2.drawRect(x, y, cardW, cardH);

      /* influence grid – unchanged except centre uses border colour so it toggles in HC  */
      InfluenceGrid ig = hand.get(i).getInfluenceGrid();
      int subW = cardW / 5, subH = cardH / 5;
      for (int r = 0; r < 5; r++)
        for (int c = 0; c < 5; c++) {
          char ch = ig.getCell(r, c);
          Color fill = Color.WHITE;
          if (ch == 'C') fill = scheme.cardBorder();
          else if (ch == 'I') fill = Color.ORANGE;
          g2.setColor(fill);
          g2.fillRect(x + c * subW, y + r * subH, subW, subH);
          g2.setColor(Color.GRAY);
          g2.drawRect(x + c * subW, y + r * subH, subW, subH);
        }

      /* label (cost/value) */
      // pick black in HC mode so it pops on red/cyan, otherwise use your normal border color
      Color labelColor = (scheme instanceof HighContrastScheme)
              ? Color.BLACK
              : scheme.cardBorder();
      g2.setColor(labelColor);

      String lab = hand.get(i).getName()
              + " (c=" + hand.get(i).getCost()
              + ",v=" + hand.get(i).getValue() + ")";
      FontMetrics fm = g2.getFontMetrics();
      int tx = x + (cardW - fm.stringWidth(lab)) / 2;
      int ty = y + cardH - fm.getAscent() / 2;
      g2.drawString(lab, tx, ty);


      /* selection border */
      if (i == selectedCardIndex) {
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(x, y, cardW, cardH);
        g2.setStroke(new BasicStroke(1));
      }
    }
  }
}
