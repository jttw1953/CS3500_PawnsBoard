package cs3500.pawnsboard.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class to read a deck configuration file and produce a List of Cards in order.
 */
public class DeckReader {

    /**
     * Reads the file at filePath and returns the list of Card objects.
     * Format per the assignment:
     * CARD_NAME COST VALUE
     * 5 lines of 5 chars (each is X, I, or C)
     */
    public static List<Card> readDeckFromFile(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner sc = new Scanner(f);
        List<Card> deck = new ArrayList<>();

        while (sc.hasNextLine()) {
            if (!sc.hasNext()) {
                break; // no more content
            }
            String name = sc.next();
            int cost = sc.nextInt();
            int value = sc.nextInt();
            sc.nextLine(); // consume remainder

            char[][] grid = new char[5][5];
            for (int r = 0; r < 5; r++) {
                String line = sc.nextLine();
                for (int c = 0; c < 5; c++) {
                    grid[r][c] = line.charAt(c);
                }
            }
            InfluenceGrid ig = new InfluenceGrid(grid);
            Card card = new Card(name, cost, value, ig);
            deck.add(card);
        }

        sc.close();
        return deck;
    }
}
