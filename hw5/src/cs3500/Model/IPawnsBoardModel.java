package cs3500.Model;

import java.util.List;

/**
 * The primary interface that represents the PawnsBoard game state and rules.
 */
public interface IPawnsBoardModel {

    int getRows();
    int getCols();

    PlayerColor getCurrentPlayer();

    /**
     * If the deck isn't empty for the current player, draw the top card.
     */
    void drawCardIfPossible();

    /**
     * Place a card from the current player's hand at (row, col), applying the rules:
     * - cell must have at least 'cost' pawns owned by current player
     * - if legal, remove those pawns, place card, apply influence
     * - switch turns
     */
    void placeCard(int handIndex, int row, int col);

    /**
     * Current player passes. If both players pass consecutively, the game ends.
     */
    void pass();

    /**
     * True if the game is over (both players have passed in a row).
     */
    boolean isGameOver();

    /**
     * Return the winner's color, or null if tie or if the game isn't over.
     */
    PlayerColor getWinner();

    /**
     * Get read-only info about the cell at (row, col).
     */
    ReadOnlyCell getCellState(int row, int col);

    /**
     * Return an unmodifiable view of the current player's hand.
     */
    List<Card> getCurrentPlayerHand();

    /**
     * Row-score for a given row and color (sum of card values in that row belonging to color).
     */
    int getRowScore(int row, PlayerColor color);

    /**
     * Return the total score for a color (sum of that color’s winning row-scores).
     */
    int getTotalScore(PlayerColor color);
}

