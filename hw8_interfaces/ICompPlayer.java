package interfaces;

import java.util.ArrayList;
import java.util.Map;

import model.assistingobj.Card;
import model.assistingobj.Cell;
import model.assistingobj.CompStrat;
import model.assistingobj.Pos;

/**
 * Interface for a computer-controlled player in the Pawns Board game.
 * Defines the contract for computer player implementations that can
 * make automated moves based on different strategies.
 */
public interface ICompPlayer extends YourPlayer {

  /**
   * Executes a computer move based on the implemented strategy.
   * The implementing class should evaluate possible moves according
   * to its strategy and either make a move or pass if no valid move is found.
   *
   * @param model The game model containing current state
   * @param hand The current hand of cards available to the player
   * @param board The current state of the game board as a map of positions to cells
   */
  void compMove(IPawnsBoardModel model, ArrayList<Card> hand, Map<Pos, Cell> board);

  /**
   * Gets the primary strategy used by this computer player.
   *
   * @return The computer strategy enum value
   */
  CompStrat getStrategy();

  /**
   * Gets the tie-breaking strategy used by this computer player.
   * Returns null if no tie-breaking strategy is configured.
   *
   * @return The tie-breaking strategy enum value, or null
   */
  CompStrat getTieStrategy();
}