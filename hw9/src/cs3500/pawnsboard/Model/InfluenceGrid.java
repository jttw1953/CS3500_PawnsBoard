package cs3500.pawnsboard.Model;

/**
 * Represents the 5x5 influence layout of a Card.
 * 'C' is at [2][2].
 * 'I' adds pawns, 'U' upgrades value, 'D' devalues value, 'X' no effect.
 */
public class InfluenceGrid {
    private final char[][] grid; // always 5x5

    public InfluenceGrid(char[][] in) {
        if (in == null || in.length != 5) {
            throw new IllegalArgumentException("Influence grid must be 5 rows");
        }
        for (int r = 0; r < 5; r++) {
            if (in[r] == null || in[r].length != 5) {
                throw new IllegalArgumentException("Each influence grid row must have 5 chars");
            }
        }
        // Center must be 'C'
        if (in[2][2] != 'C') {
            throw new IllegalArgumentException("Center [2,2] must be 'C', found '" + in[2][2] + "'");
        }
        // Validate allowed characters
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                char ch = in[r][c];
                if (r == 2 && c == 2) continue; // already checked C
                boolean ok = (ch == 'X' || ch == 'I' || ch == 'U' || ch == 'D');
                if (!ok) {
                    throw new IllegalArgumentException(
                            "Invalid influence character '" + ch + "' at [" + r + "," + c + "]. " +
                                    "Allowed: X, I, U, D, and center must be C.");
                }
            }
        }
        // Copy into internal array
        this.grid = new char[5][5];
        for (int r = 0; r < 5; r++) {
            System.arraycopy(in[r], 0, this.grid[r], 0, 5);
        }
    }

    /**
     * Get the char at [row, col] in this 5x5.
     * Valid returns: 'C', 'I', 'U', 'D', 'X'.
     */
    public char getCell(int row, int col) {
        if (row < 0 || row >= 5 || col < 0 || col >= 5) {
            throw new IndexOutOfBoundsException("InfluenceGrid indices must be between 0 and 4");
        }
        return grid[row][col];
    }
}
