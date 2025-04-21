package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.EnhancedPawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;

import java.awt.*;

/**
 * Extends BoardPanel to overlay U/D modifiers on each square.
 */
public class EnhancedBoardPanel extends BoardPanel {

    public EnhancedBoardPanel(ReadOnlyPawnsBoardModel model, ColorScheme scheme) {
        super(model, scheme);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // first draw everything exactly as before
        super.paintComponent(g);

        // only proceed if we've really got an enhanced model
        if (! (getModel() instanceof EnhancedPawnsBoardModel)) {
            return;
        }
        EnhancedPawnsBoardModel em = (EnhancedPawnsBoardModel) getModel();
        Graphics2D g2 = (Graphics2D) g.create();

        int rows = em.getRows(), cols = em.getCols();
        int cellW = getWidth() / cols, cellH = getHeight() / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int mod = em.valueModifiers[r][c];
                if (mod == 0) {
                    continue;
                }

                // choose U vs D fill color
                Color fill = (mod > 0)
                        ? getScheme().upgradeFill()
                        : getScheme().devalueFill();

                int x = c * cellW, y = r * cellH;
                g2.setColor(fill);
                g2.fillRect(x + 1, y + 1, cellW - 2, cellH - 2);

                // draw the signed modifier in the corner
                g2.setColor(getScheme().cardBorder());
                String s = String.format("%+d", mod);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(s, x + 2, y + fm.getAscent() + 2);
            }
        }

        g2.dispose();
    }
}
