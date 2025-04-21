package cs3500.pawnsboard.view;

import java.awt.*;

public interface ColorScheme {
  /* board + grid */
  Color boardBackground();

  Color cardBorder();

  /* pawns & cards */
  Color pawnRed();

  Color pawnBlue();

  Color cardRedFill();

  Color cardBlueFill();

  /* hand‑panel background (may reuse card colours) */
  Color handRedFill();

  Color handBlueFill();

  /* highlight rectangle + text drawn on top of it */
  Color highlight();

  Color highlightText();

  /* row‑score / misc text */
  Color scoreText();

  /**
   * color used to highlight an U‑upgrade influence
   */
  Color upgradeFill();

  /**
   * color used to highlight a D‑devalue influence
   */
  Color devalueFill();
}
