package cs3500.pawnsboard.View;

import cs3500.pawnsboard.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Draws the board grid using the active {@link ColorScheme}. */
public class BoardPanel extends JPanel {

    private final ReadOnlyPawnsBoardModel model;
    private PawnsBoardSwingView   parentView;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private ColorScheme scheme;          // can be swapped at run‑time

    /* ---------------- constructor & helpers ---------------- */

    public BoardPanel(ReadOnlyPawnsBoardModel model, ColorScheme scheme) {
        this.model  = model;
        this.scheme = scheme;
        setBackground(scheme.boardBackground());

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int cellW = getWidth()  / model.getCols();
                int cellH = getHeight() / model.getRows();
                int r = e.getY() / cellH, c = e.getX() / cellW;
                if (r<0 || r>=model.getRows() || c<0 || c>=model.getCols()) return;

                if (r == selectedRow && c == selectedCol) {
                    selectedRow = selectedCol = -1;      // deselect
                } else {
                    selectedRow = r;  selectedCol = c;
                }
                if (parentView!=null && parentView.getController()!=null)
                    parentView.getController().onCellClicked(r,c);

                repaint();
            }
        });
    }

    /* run‑time palette swap (used by the HC‑checkbox in the frame) */
    public void setScheme(ColorScheme s) {
        this.scheme = s;
        setBackground(s.boardBackground());
        repaint();
    }
    public void setView(PawnsBoardSwingView v) { this.parentView = v; }

    /* ---------------- paint ---------------- */

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int rows  = model.getRows();
        int cols  = model.getCols();
        int cellW = getWidth()  / cols;
        int cellH = getHeight() / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                int x = c * cellW, y = r * cellH;

                /* ---- 1  background (plain or highlighted) ---------------- */
                g2.setColor((r==selectedRow && c==selectedCol)
                        ? scheme.highlight()
                        : scheme.boardBackground());
                g2.fillRect(x, y, cellW, cellH);

                /* ---- 2  border ------------------------------------------- */
                g2.setColor(scheme.cardBorder());
                g2.drawRect(x, y, cellW, cellH);

                /* ---- 3  contents ----------------------------------------- */
                ReadOnlyCell cell = model.getCellState(r, c);

                switch (cell.type) {
                    case EMPTY:
                        break;

                    case PAWNS:
                        g2.setColor(cell.owner==PlayerColor.RED
                                ? scheme.pawnRed() : scheme.pawnBlue());
                        drawCentered(g2,
                                Integer.toString(cell.pawnCount),
                                x,y,cellW,cellH,
                                (r==selectedRow&&c==selectedCol)
                                        ? scheme.highlightText()
                                        : scheme.cardBorder() /* black/white */);
                        break;

                    case CARD:
                        /* coloured rectangle */
                        g2.setColor(cell.owner==PlayerColor.RED
                                ? scheme.cardRedFill() : scheme.cardBlueFill());
                        g2.fillRect(x+2, y+2, cellW-4, cellH-4);

                        /* card name / value (max 5 chars) */
                        String lbl = cell.cardName.length()>5
                                ? cell.cardName.substring(0,5)
                                : cell.cardName;
                        drawCentered(g2, lbl, x,y,cellW,cellH,
                                (r==selectedRow&&c==selectedCol)
                                        ? scheme.highlightText()
                                        : Color.BLACK /* pop! */);
                        break;
                }
            }

            /* ---- Row‑score ‑‑ 10 px to the right of last column ---------- */
            g2.setColor(scheme.scoreText());
            int baseY = r * cellH + 15;                  // baseline inside row
            int baseX = cols * cellW + 10;               // gap from board
            g2.drawString("R="+model.getRowScore(r,PlayerColor.RED)+
                            "  B="+model.getRowScore(r,PlayerColor.BLUE),
                    baseX, baseY);
        }
    }

    /* utility : draw txt centred in rectangle; optional colour override */
    private void drawCentered(Graphics2D g2, String txt,
                              int x,int y,int w,int h, Color override) {
        Color old = g2.getColor();
        if (override!=null) g2.setColor(override);
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (w - fm.stringWidth(txt))/2;
        int ty = y + (h + fm.getAscent())/2 - 2;
        g2.drawString(txt, tx, ty);
        g2.setColor(old);
    }
}

