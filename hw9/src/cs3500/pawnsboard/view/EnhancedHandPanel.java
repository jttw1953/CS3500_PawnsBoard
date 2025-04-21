package cs3500.pawnsboard.view;

import java.awt.*;
import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.InfluenceGrid;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Like HandPanel, but paints U‑cells in green and D‑cells in purple.
 */
public class EnhancedHandPanel extends HandPanel {

  public EnhancedHandPanel(ReadOnlyPawnsBoardModel model, ColorScheme scheme) {
    super(model, scheme);
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();

    List<Card> hand = getModel().getCurrentPlayerHand();
    if (hand.isEmpty()) {
      g2.dispose();
      return;
    }

    int cardW = getWidth() / hand.size();
    int cardH = getHeight() - 10;
    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      InfluenceGrid ig = card.getInfluenceGrid();
      int x0 = i * cardW, y0 = 0;
      int subW = cardW / 5, subH = cardH / 5;

      // for each U or D cell, overlay its color
      for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
          char ch = ig.getCell(r, c);
          if (ch == 'U' || ch == 'D') {
            Color fill = (ch == 'U')
                    ? scheme.upgradeFill()
                    : scheme.devalueFill();
            g2.setColor(fill);
            g2.fillRect(x0 + c * subW, y0 + r * subH, subW, subH);
            g2.setColor(Color.GRAY);
            g2.drawRect(x0 + c * subW, y0 + r * subH, subW, subH);
          }
        }
      }
    }

    g2.dispose();
  }
}
