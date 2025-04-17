
package cs3500.pawnsboard.View;

import java.awt.*;

public class DefaultScheme implements ColorScheme {

    public Color boardBackground() { return Color.WHITE; }

    public Color highlight()       { return new Color(255, 220, 180); } // peach
    public Color highlightText()   { return Color.BLACK; }

    public Color pawnRed()         { return Color.RED;  }
    public Color pawnBlue()        { return Color.BLUE; }

    public Color cardRedFill()     { return new Color(255, 170, 170); }
    public Color cardBlueFill()    { return new Color(180, 180, 255); }
    public Color cardBorder()      { return Color.BLACK; }


    public Color handRedFill()     { return cardRedFill();  }
    public Color handBlueFill()    { return cardBlueFill(); }

    @Override public Color scoreText()    { return Color.BLACK; }
}
