package cs3500.pawnsboard.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InfluenceGridTest {

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
  }

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