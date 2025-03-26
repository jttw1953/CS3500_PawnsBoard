package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for InfluenceGrid.
 * Verifies grid initialization, cell access, and validation of grid structure.
 */
public class InfluenceGridTest {

  /**
   * Tests a valid 5x5 influence grid with a center cell 'C'.
   * Checks various cell values to confirm correct setup.
   */
  @Test
  public void testValidInfluenceGrid() {
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    InfluenceGrid ig = new InfluenceGrid(grid);
    assertEquals('C', ig.getCell(2, 2));
    assertEquals('I', ig.getCell(2, 3));
    assertEquals('X', ig.getCell(0, 0));
    assertEquals('I', ig.getCell(2, 0));
  }

  /**
   * Tests that an influence grid with incorrect dimensions (not 5x5)
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInfluenceGridSize() {
    char[][] grid = {
            {'X', 'X', 'I', 'X'},
            {'X', 'X', 'I', 'X'},
            {'I', 'I', 'C', 'I'},
            {'X', 'X', 'I', 'X'},
    };
    new InfluenceGrid(grid);
  }

  /**
   * Tests that an influence grid missing the center 'C' character
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMissingCenterC() {
    char[][] grid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'X', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    new InfluenceGrid(grid);
  }
}


