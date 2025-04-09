package interfaces;

import java.util.ArrayList;
import java.util.Map;

import controller.PawnsBoardController;
import model.assistingobj.Card;
import model.assistingobj.Cell;
import model.assistingobj.Player;
import model.assistingobj.Pos;

/**
 * An interface for models of the PawnsBoard game.
 */
public interface IPawnsBoardModel extends ReadOnlyIPawnsBoardModel {

  /**
   * Starts the game by initalizing the board and giving out cards.
   * @param handSize determines the handsize for the game, must be less than 1/3 deck size
   * @param shuffle determines if the decks get shuffled
   */
  public void startGame(int handSize, boolean shuffle);

  /**
   * Skips the turn of the current player and draws them a new card.
   * If the preivous player also skipped their turn, the game ends.
   */
  public void skipTurn();

  /**
   * Places a card on a cell the player controls, applies the influence of that card to cells.
   * @param pos the position of the cell the card is placed on
   * @param cardIdx the position of the placed card in the players hand
   */
  public void placeCard(Pos pos, int cardIdx);

  /**
   * Gets the player whose turn it currently is.
   * @return Player whose turn it is
   */
  public Player getPlayerTurn();

  /**
   * Checks to see if the game is over.
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
   * @param row the row of the cell
   * @param col the column of the cell
   * @return the cell at the position
   */
  public Cell getCellAt(int row, int col);

  /**
   * Gets the number of rows on the board.
   * @return number of rows
   */
  public int getNumRows();

  /**
   * Gets the number of columns on the board.
   * @return number of columns
   */
  int getNumCols();

  /**
   * Gets the handsize of the current game.
   * @return the handsize
   */
  int getHandSize();


  /**
   * Gets the Red players hand.
   */
  ArrayList<Card> getRedHand();

  /**
   * Gets the Blue players hand.
   */
  ArrayList<Card> getBlueHand();

  boolean canPlaceCard(Pos tempPos, Card card, Player player);

  void addController(PawnsBoardController controller);

  void setCompPlayer(YourPlayer player);

  Map<Pos, Cell> getBoardCopy();
}
