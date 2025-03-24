package cs3500.pawnsboard.Model;


public interface IPawnsBoardModel extends ReadOnlyPawnsBoardModel {
    // All the mutating methods that read-only shouldn't have:
    void drawCardIfPossible();
    void placeCard(int handIndex, int row, int col);
    void pass();
    IPawnsBoardModel copyModel();
}


