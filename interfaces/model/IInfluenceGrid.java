package cs3500.pawnsboard.model;

/**
 * An interface representing a read-only view of a card's 5x5 influence grid.
 * Each cell is a character representing influence: 'I', 'C', or 'X'.
 */
public interface IInfluenceGrid {

  /**
   * Gets the character value at a specific position in the 5x5 grid.
   * @param row the row index (0-4)
   * @param col the column index (0-4)
   * @return the character at the specified cell
   */
  char getCell(int row, int col);
}
