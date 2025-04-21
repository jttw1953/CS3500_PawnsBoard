
package cs3500.pawnsboard.View;

import java.awt.*;

public class HighContrastScheme implements ColorScheme {
    private static final Color BLACK = Color.BLACK;
    private static final Color WHITE = Color.WHITE;
    private static final Color PURE_RED  = Color.RED;
    private static final Color CYAN      = Color.CYAN;

    public Color boardBackground()  { return BLACK; }
    public Color highlight()        { return Color.YELLOW; }
    public Color highlightText()    { return BLACK; }

    public Color pawnRed()          { return PURE_RED; }
    public Color pawnBlue()         { return CYAN;     }

    public Color cardRedFill()      { return PURE_RED; }
    public Color cardBlueFill()     { return CYAN;     }
    public Color cardBorder()       { return WHITE;    }

    public Color handRedFill()      { return PURE_RED; }
    public Color handBlueFill()     { return CYAN;     }

    @Override public Color scoreText()    { return Color.WHITE; }

    @Override public Color upgradeFill()   { return Color.YELLOW; }  // or another high‑contrast hue
    @Override public Color devalueFill()   { return Color.WHITE;  }
}
