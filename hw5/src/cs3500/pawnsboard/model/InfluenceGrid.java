package cs3500.pawnsboard.model;

/**
 * Represents the 5x5 influence layout of a Card.
 * 'C' is at [2][2], 'I' cells are influenced, 'X' cells are not influenced.
 */
public class InfluenceGrid {
  private final char[][] grid; // always 5x5

  /**
   * Constructs a 5x5 InfluenceGrid.
   * @param in the 5x5 character array representing influence
   *           ('I' for affected cells, 'X' for none, 'C' for center).
   * @throws IllegalArgumentException if the grid is not 5x5 or if the center [2,2] is not 'C'.
   */
  public InfluenceGrid(char[][] in) {
    if (in.length != 5 || in[0].length != 5) {
      throw new IllegalArgumentException("Influence grid must be 5x5");
    }
    // Validate single 'C' in the center
    if (in[2][2] != 'C') {
      throw new IllegalArgumentException("Center [2,2] must be 'C'");
    }
    this.grid = new char[5][5];
    for (int r = 0; r < 5; r++) {
      if (in[r].length != 5) {
        throw new IllegalArgumentException("Each row must have 5 chars");
      }
      for (int c = 0; c < 5; c++) {
        this.grid[r][c] = in[r][c];
      }
    }
  }

  /**
   * Get the char at [row, col] in this 5x5.
   */
  public char getCell(int row, int col) {
    return grid[row][col];
  }
}
