package cs3500.pawnsboard.model;

import org.junit.Test;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for DeckReader class.
 */
public class DeckReaderTest {

  @Test
  public void testReadValidDeck() throws Exception {
    File file = new File("docs/deck.config");
    List<Card> deck = DeckReader.readDeckFromFile(file);

    assertNotNull(deck);
    assertFalse(deck.isEmpty());

    Card firstCard = deck.get(0);
    assertNotNull(firstCard.getName());
    assertTrue(firstCard.getCost() >= 1 && firstCard.getCost() <= 3);
    assertTrue(firstCard.getValue() > 0);
  }

  @Test(expected = java.io.FileNotFoundException.class)
  public void testFileNotFound() throws Exception {
    File file = new File("nonexistent-file.config");
    DeckReader.readDeckFromFile(file);
  }
}