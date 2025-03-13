package cs3500.pawnsboard.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Holds the deck, hand, and color for one player.
 */
public class PlayerState {
  private final PlayerColor color;
  private final Deque<Card> deck;
  private final List<Card> hand;

  /**
   * Constructs a PlayerState with a given color and deck.
   * @param color         the player's color (RED or BLUE).
   * @param deckInOrder   the player's deck of cards (must not be null).
   */
  public PlayerState(PlayerColor color, List<Card> deckInOrder) {
    this.color = color;
    // no shuffle => store as given
    this.deck = new ArrayDeque<>(deckInOrder);
    this.hand = new ArrayList<>();
  }

  public PlayerColor getColor() {
    return color;
  }

  /**
   * Check if there is a card left in our deck to draw.
   */
  public boolean hasNextCard() {
    return !deck.isEmpty();
  }

  /**
   * Draw the top card into our hand, if any.
   */
  public void drawCard() {
    if (!deck.isEmpty()) {
      hand.add(deck.removeFirst());
    }
  }

  /**
   * Get this player's hand of cards (modifiable list).
   */
  public List<Card> getHand() {
    return hand;
  }

  /**
   * Remove and return the card at index from the hand.
   */
  public Card removeCardFromHand(int index) {
    if (index < 0 || index >= hand.size()) {
      throw new IllegalArgumentException("Invalid hand index");
    }
    return hand.remove(index);
  }
}

