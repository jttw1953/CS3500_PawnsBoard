package cs3500.pawnsboard.Model;

import java.util.List;

public interface ReadOnlyPawnsBoardModel {
    int getRows();
    int getCols();
    PlayerColor getCurrentPlayer();

    boolean isGameOver();
    PlayerColor getWinner();

    ReadOnlyCell getCellState(int row, int col);
    List<Card> getCurrentPlayerHand();

    int getRowScore(int row, PlayerColor color);
    int getTotalScore(PlayerColor color);
    boolean canPlaceCard(int handIndex, int row, int col);

}
