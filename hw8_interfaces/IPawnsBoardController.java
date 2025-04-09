package interfaces;

import model.assistingobj.Pos;

/**
 * An interface for the sub controller which can skip and place cards.
 */
public interface IPawnsBoardController {
  void startGame(int handSize, boolean shuffle);

  void skipTurn();

  void placeCard(Pos pos, int cardIdx);

  void switchTurn();

  void firstTurnCompGo();
}
