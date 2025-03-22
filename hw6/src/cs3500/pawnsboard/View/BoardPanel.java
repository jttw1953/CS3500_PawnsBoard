package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.Model.ReadOnlyCell;
import cs3500.pawnsboard.Model.PlayerColor;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
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
                // Figure out which cell
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
                    }
                    else {
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

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = c * cellW;
                int y = r * cellH;

                // highlight if selected
                if (r == selectedRow && c == selectedCol) {
                    g2.setColor(Color.CYAN);
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
                        // center text
                        g2.drawString(countStr, x + cellW/2, y + cellH/2);
                        break;
                    case CARD:
                        // draw a rectangle or shape for the card
                        if (cell.owner == PlayerColor.RED) {
                            g2.setColor(Color.PINK);
                        } else {
                            g2.setColor(Color.LIGHT_GRAY);
                        }
                        g2.fillRect(x+2, y+2, cellW-4, cellH-4);
                        // card name or value
                        g2.setColor(Color.BLACK);
                        String cardVal = cell.cardName; // or show the card's value if you had it
                        if (cardVal.length() > 5) {
                            cardVal = cardVal.substring(0,5); // limit text length
                        }
                        g2.drawString(cardVal, x + cellW/4, y + cellH/2);
                        break;
                }
            }
        }
    }
}
