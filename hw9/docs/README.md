# Demo Preparation Summary (Assignment 9)
## Features Implemented
We have successfully implemented the following Assignment 9 features:

### Level 0 – High Contrast Mode:
- Alternate color palette available via the HighContrastScheme. Ensures visibility improvements and disables bonus rendering.

### Level 1 – Alternate Color Schemes:
- Introduced the ColorScheme interface with DefaultScheme and HighContrastScheme. GUI adapts all board and hand rendering colors based on the current scheme.

### Level 2 – Visualizing New Influence:

- Added support for 'U' (upgrade) and 'D' (devalue) influence types.

- Value modifiers are tracked per cell and affect the final score of cards placed.

- These modifiers are displayed in both the GUI (BoardPanel) and textual view.

- Influence grids now render 'U' in green and 'D' in purple, in both GUI and text.

- High Contrast mode correctly disables this value bonus visualization.
  
## Key Files Added or Modified
### New Launcher Classes:

- EnhancedGuiGame: GUI-based game runner for enhanced model.

- EnhancedGameDemo: Text-based runner showing value stacking behavior.

### Model Changes:

- EnhancedPawnsBoardModel: Supports value stacking via 'U' and 'D' influence.

- Modified Card/InfluenceGrid parsing to support new characters.

### View Updates:

- BoardPanel.java: Displays value modifiers and supports color schemes.

- HandPanel.java: Draws influence grid with color-coded U/D/I.

- TextualView.java and EnhancedPawnsBoardTextView.java: Show modified card values and updated grid formatting.

### Color Scheme System:

- ColorScheme interface

- DefaultScheme and HighContrastScheme implementations