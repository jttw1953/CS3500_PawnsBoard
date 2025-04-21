package cs3500.pawnsboard.model;

import org.junit.Test;
import static org.junit.Assert.*;

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
        // additional checks
        assertEquals('I', ig.getCell(2, 0));
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

    @Test
    public void testUpgradeDevalueCells() {
        char[][] grid = {
                {'X', 'X', 'U', 'X', 'X'},
                {'X', 'D', 'I', 'U', 'X'},
                {'I', 'U', 'C', 'D', 'I'},
                {'X', 'D', 'I', 'U', 'X'},
                {'X', 'X', 'I', 'X', 'X'}
        };
        InfluenceGrid ig = new InfluenceGrid(grid);
        assertEquals('U', ig.getCell(0, 2));
        assertEquals('D', ig.getCell(1, 1));
        assertEquals('I', ig.getCell(2, 0));
        assertEquals('U', ig.getCell(2, 1));
        assertEquals('D', ig.getCell(2, 3));
    }

}
