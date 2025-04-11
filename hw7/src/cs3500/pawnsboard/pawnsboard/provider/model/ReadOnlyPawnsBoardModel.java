package cs3500.pawnsboard.provider.model;

import java.util.List;

/**
 * Represents a read-only view of the Pawns Board game model.
 * This interface exposes only methods that allow inspection of the game's current state
 * without permitting any modifications. It is intended for use by views and strategies
 * to make decisions or render the game without risking accidental mutation.
 */
public interface ReadOnlyPawnsBoardModel {

    /**
     * Returns the number of rows on the game board.
     *
     * @return the number of rows
     */
    int getRows();

    /**
     * Returns the number of columns on the game board.
     *
     * @return the number of columns
     */
    int getCols();

    /**
     * Returns the player whose turn it is to act.
     *
     * @return the current player's color
     */
    PlayerColor getCurrentPlayer();

    /**
     * Returns whether the game is over.
     * A game ends after two consecutive passes or if no moves are possible.
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver();

    /**
     * Returns the winner of the game if it is over.
     * If the game is not yet over, this may return null.
     * If the game ends in a tie, this may also return null depending on the implementation.
     *
     * @return the winning player's color, or null if undecided or tied
     */
    PlayerColor getWinner();

    /**
     * Returns a read-only view of the cell at the specified row and column.
     *
     * @param row the row of the cell (0-indexed)
     * @param col the column of the cell (0-indexed)
     * @return the read-only state of the cell
     * @throws IllegalArgumentException if the position is out of bounds
     */
    ReadOnlyCell getCellState(int row, int col);

    /**
     * Returns the current player's hand as a list of cards.
     * The list is read-only and should not be mutated by the caller.
     *
     * @return a list of cards in the current player's hand
     */
    List<Card> getCurrentPlayerHand();

    /**
     * Returns the score for the specified player in the given row.
     * Row scores are determined by pawn counts and card values.
     *
     * @param row the row index (0-indexed)
     * @param color the player color
     * @return the score of the player in the specified row
     */
    int getRowScore(int row, PlayerColor color);

    /**
     * Returns the total score for the specified player across the entire board.
     *
     * @param color the player color
     * @return the total score of the player
     */
    int getTotalScore(PlayerColor color);

    /**
     * Returns whether the current player is allowed to place the specified card
     * at the given row and column according to the rules.
     *
     * @param handIndex the index of the card in the player's hand
     * @param row the row where the card would be placed
     * @param col the column where the card would be placed
     * @return true if the move is legal, false otherwise
     * @throws IllegalArgumentException if indices are out of bounds
     */
    boolean canPlaceCard(int handIndex, int row, int col);

    /**
     * Returns the number of cards remaining in the specified player's deck.
     *
     * @param color the player color
     * @return the number of remaining cards in the player's deck
     */
    int getDeckSize(PlayerColor color);
}
