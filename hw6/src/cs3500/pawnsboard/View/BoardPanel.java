package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyCell;
import cs3500.pawnsboard.Model.PlayerColor;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A panel that displays the PawnsBoard grid, one cell per row x col.
 */
public class BoardPanel extends JPanel {
  private final ReadOnlyPawnsBoardModel model;
  // A link back to the main view (so we can access its controller)
  private PawnsBoardSwingView parentView;

  // We track which cell is currently "selected," if any
  private int selectedRow = -1;
  private int selectedCol = -1;

  public BoardPanel(ReadOnlyPawnsBoardModel model) {
    super();
    this.model = model;
    this.setBackground(Color.WHITE);

    // Mouse listener to find out which cell was clicked
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int rows = model.getRows();
        int cols = model.getCols();
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        int row = e.getY() / cellHeight;
        int col = e.getX() / cellWidth;

        // Check if in-bounds
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          // If it's already selected, deselect
          if (row == selectedRow && col == selectedCol) {
            selectedRow = -1;
            selectedCol = -1;
          } else {
            selectedRow = row;
            selectedCol = col;
          }

          // If we have a controller, log it
          if (parentView != null && parentView.getController() != null) {
            parentView.getController().onCellClicked(row, col);
          }

          // Repaint so we see highlight changes
          repaint();
        }
      }
    });
  }

  /**
   * Let the panel know which parent view it's in,
   * so we can access the controller if needed.
   */
  public void setView(PawnsBoardSwingView v) {
    this.parentView = v;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    int rows = model.getRows();
    int cols = model.getCols();

    int panelW = getWidth();
    int panelH = getHeight();

    // cell size
    int cellW = panelW / cols;
    int cellH = panelH / rows;

    // (Optional) choose a bigger font for clarity
    // g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int x = c * cellW;
        int y = r * cellH;

        // highlight if selected
        if (r == selectedRow && c == selectedCol) {
          // Example highlight: pink if RED, light blue if BLUE
          if (parentView.getModel().getCurrentPlayer() == PlayerColor.RED) {
            g2.setColor(new Color(255, 200, 200)); // pinkish
          } else {
            g2.setColor(new Color(200, 200, 255)); // bluish
          }
          g2.fillRect(x, y, cellW, cellH);
        }

        // draw border
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, cellW, cellH);

        // draw what's in the cell
        ReadOnlyCell cell = model.getCellState(r, c);
        switch (cell.type) {
          case EMPTY:
            // do nothing
            break;
          case PAWNS:
            // draw a text for the pawn count
            // and color to reflect the owner
            if (cell.owner == PlayerColor.RED) {
              g2.setColor(Color.RED);
            } else {
              g2.setColor(Color.BLUE);
            }
            String countStr = Integer.toString(cell.pawnCount);
            drawCenteredString(g2, countStr, x, y, cellW, cellH);
            break;
          case CARD:
            // draw a rectangle or shape for the card
            if (cell.owner == PlayerColor.RED) {
              g2.setColor(Color.PINK);
            } else {
              // Use a light-blue color for Blue
              g2.setColor(new Color(180, 180, 255));
            }
            g2.fillRect(x + 2, y + 2, cellW - 4, cellH - 4);

            // card name or value
            g2.setColor(Color.BLACK);
            String cardVal = cell.cardName; // or the card's value
            // limit text length so it fits
            if (cardVal.length() > 5) {
              cardVal = cardVal.substring(0, 5);
            }
            drawCenteredString(g2, cardVal, x, y, cellW, cellH);
            break;
        }
      }

      // Move the row-scores INSIDE the leftmost cell for that row:
      int redScore = model.getRowScore(r, PlayerColor.RED);
      int blueScore = model.getRowScore(r, PlayerColor.BLUE);

      // We'll draw them near the top-left corner of the row
      int rowStartX = 0;        // leftmost cell
      int rowStartY = r * cellH;
      g2.setColor(Color.BLACK);
      String rowScoreText = "R=" + redScore + " B=" + blueScore;
      g2.drawString(rowScoreText, rowStartX + 5, rowStartY + 15);
    }
  }

  /**
   * Helper to center text in a rectangle (x,y,width,height).
   */
  private void drawCenteredString(Graphics2D g2, String text,
                                  int rectX, int rectY, int rectW, int rectH) {
    FontMetrics fm = g2.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getAscent();  // or getHeight()

    int textX = rectX + (rectW - textWidth) / 2;
    int textY = rectY + (rectH + textHeight) / 2 - 2; // small tweak
    g2.drawString(text, textX, textY);
  }
}

