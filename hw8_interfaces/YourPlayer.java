package interfaces;

import java.util.ArrayList;
import java.util.Map;

import model.assistingobj.Card;
import model.assistingobj.Cell;
import model.assistingobj.Player;
import model.assistingobj.Pos;

/**
 * An interface that represents any sort of player for the game, human or computer.
 */
public interface YourPlayer {
  public boolean isComp();

  /**
   * Gets the player (RED or BLUE) that this computer controls.
   *
   * @return The player enum value
   */
  public Player getPlayer();

  void compMove(IPawnsBoardModel model, ArrayList<Card> hand, Map<Pos, Cell> board);

  void setSize(int rows, int cols);

  void addController(IPawnsBoardController controller);
}
