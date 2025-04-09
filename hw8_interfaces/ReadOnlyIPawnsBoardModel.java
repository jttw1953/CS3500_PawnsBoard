package interfaces;

import java.util.ArrayList;

import model.assistingobj.Card;
import model.assistingobj.Cell;
import model.assistingobj.Player;
import model.assistingobj.Pos;

/**
 * An interface for only reading the info in a model, not changing it.
 */
public interface ReadOnlyIPawnsBoardModel {

  /**
   * Gets the player whose turn it currently is.
   * @return Player whose turn it is
   */
  public Player getPlayerTurn();

  /**
   * Checks to see if the game is over.
   *
   * @return true if the game is over, false otherwise.
   */
  public boolean isGameOver();

  /**
   * Takes in a player that it would like to find the final score for and then will return
   * the final score of that player.
   */
  public int getFinalScore(Player currentPlayer);

  /**
   * Will return the score of the given row for the given player.
   */
  public int rowScorer(Player currentPlayer, int row);

  /**
   * Returns the Player who won the game. If the game isn't over than it will throw an
   * IllegalStateExecption. If there is a tie the method will return null.
   */
  public Player getWinner();

  /**
   * Gets the cell at the row and columns given.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   * @return the cell at the position
   */
  public Cell getCellAt(int row, int col);

  /**
   * Gets the number of rows on the board.
   *
   * @return number of rows
   */
  public int getNumRows();

  /**
   * Gets the number of columns on the board.
   *
   * @return number of columns
   */
  public int getNumCols();

  /**
   * Gets the handsize of the current game.
   *
   * @return the handsize
   */
  public int getHandSize();

  /**
   * Gets the Red players hand.
   */
  public ArrayList<Card> getRedHand();

  /**
   * Gets the Blue players hand.
   */
  public ArrayList<Card> getBlueHand();

  boolean canPlaceCard(Pos selectedCell, Card selectedCard, Player playerTurn);
}
