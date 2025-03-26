package cs3500.pawnsboard.Model;

import java.util.Collections;
import java.util.List;

/**
 * Concrete implementation of IPawnsBoardModel for Pawns Board.
 * Implements all read-only methods (from ReadOnlyPawnsBoardModel)
 * and mutating methods (from IPawnsBoardModel).
 */
public class PawnsBoardModel implements IPawnsBoardModel {
  private final int rows;
  private final int cols;

  // Track the two players
  final PlayerState redState;
  private final PlayerState blueState;

  // Current player's color
  private PlayerColor currentPlayer;

  // The board, rows x cols
  final Cell[][] board;

  // For detecting game end (two consecutive passes)
  private boolean lastWasPass;
  private boolean gameOver;

  /**
   * Construct a PawnsBoardModel.
   *
   * @param rows            number of rows (>0)
   * @param cols            number of columns (>1 and odd)
   * @param redDeck         a list of cards for red
   * @param blueDeck        a list of cards for blue
   * @param initialHandSize the starting number of cards each player draws
   */
  public PawnsBoardModel(int rows, int cols,
                         List<Card> redDeck,
                         List<Card> blueDeck,
                         int initialHandSize) {
    if (rows <= 0) {
      throw new IllegalArgumentException("Rows must be > 0");
    }
    if (cols <= 1 || (cols % 2 == 0)) {
      throw new IllegalArgumentException("Cols must be > 1 and odd");
    }
    if (redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("Decks cannot be null");
    }

    int redMax = (int) Math.ceil(redDeck.size() / 3.0);
    int blueMax = (int) Math.ceil(blueDeck.size() / 3.0);
    if (initialHandSize > redMax || initialHandSize > blueMax) {
      throw new IllegalArgumentException(
              "Initial hand size cannot exceed one-third of the deck size.");
    }

    this.rows = rows;
    this.cols = cols;
    this.board = new Cell[rows][cols];

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        board[r][c] = new Cell();
      }
    }

    // Place starting pawns
    for (int r = 0; r < rows; r++) {
      board[r][0].setPawns(PlayerColor.RED, 1);
      board[r][cols - 1].setPawns(PlayerColor.BLUE, 1);
    }

    // Shuffle the decks before creating player states
    //Collections.shuffle(redDeck);
    // Collections.shuffle(blueDeck);

    this.redState = new PlayerState(PlayerColor.RED, redDeck);
    this.blueState = new PlayerState(PlayerColor.BLUE, blueDeck);

    // Deal initial hands
    for (int i = 0; i < initialHandSize; i++) {
      if (redState.hasNextCard()) {
        redState.drawCard();
      }
      if (blueState.hasNextCard()) {
        blueState.drawCard();
      }
    }

    this.currentPlayer = PlayerColor.RED; // Red starts
    this.lastWasPass = false;
    this.gameOver = false;
  }

  @Override
  public IPawnsBoardModel copyModel() {
    return new PawnsBoardModel(this);
  }

  /**
   * A private copy constructor used by copyModel() to produce a deep copy
   * of the board, players, and state.
   */
  private PawnsBoardModel(PawnsBoardModel that) {
    // Copy primitive fields
    this.rows = that.rows;
    this.cols = that.cols;
    this.gameOver = that.gameOver;
    this.lastWasPass = that.lastWasPass;
    this.currentPlayer = that.currentPlayer;

    // Deep-copy the board cells (Cell is mutable)
    this.board = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        this.board[r][c] = copyOneCell(that.board[r][c]);
      }
    }

    // Deep-copy the PlayerState objects
    this.redState = new PlayerState(that.redState);
    this.blueState = new PlayerState(that.blueState);
  }

  /**
   * Helper that creates a new Cell with the same type, owner, pawnCount, and card reference.
   * Since Card is immutable, referencing the same Card object is safe.
   */
  private Cell copyOneCell(Cell original) {
    Cell newCell = new Cell();
    if (original.getType() == CellType.EMPTY) {
      newCell.setEmpty();
    } else if (original.getType() == CellType.PAWNS) {
      newCell.setPawns(original.getOwner(), original.getPawnCount());
    } else if (original.getType() == CellType.CARD) {
      newCell.setCard(original.getOwner(), original.getCard());
    }
    return newCell;
  }


  // ----------------------------------------------------------------------------------------
  // Read-only methods (from ReadOnlyPawnsBoardModel)
  // ----------------------------------------------------------------------------------------

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public int getCols() {
    return cols;
  }

  @Override
  public PlayerColor getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public PlayerColor getWinner() {
    // If game is not over, we can return null or throw an exception:
    if (!gameOver) {
      return null;
    }
    // Compute total scores
    int redTotal = getTotalScore(PlayerColor.RED);
    int blueTotal = getTotalScore(PlayerColor.BLUE);
    if (redTotal > blueTotal) {
      return PlayerColor.RED;
    } else if (blueTotal > redTotal) {
      return PlayerColor.BLUE;
    } else {
      return null; // tie
    }
  }

  @Override
  public ReadOnlyCell getCellState(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Row/col out of bounds");
    }
    return new ReadOnlyCell(board[row][col]);
  }

  @Override
  public List<Card> getCurrentPlayerHand() {
    // Return an unmodifiable copy
    List<Card> hand = getCurrentPlayerState().getHand();
    return Collections.unmodifiableList(hand);
  }

  @Override
  public int getRowScore(int row, PlayerColor color) {
    if (row < 0 || row >= rows) {
      throw new IllegalArgumentException("Invalid row index");
    }
    int score = 0;
    for (int c = 0; c < cols; c++) {
      Cell cell = board[row][c];
      if (cell.getType() == CellType.CARD && cell.getOwner() == color) {
        Card card = cell.getCard();
        score += card.getValue();
      }
    }
    return score;
  }

  @Override
  public int getTotalScore(PlayerColor color) {
    // For each row: compare row-scores for RED vs BLUE
    // The color that "wins" the row gets the entire row-sum for themselves.
    // If tied, 0 points for both.
    int total = 0;
    for (int r = 0; r < rows; r++) {
      int redRow = getRowScore(r, PlayerColor.RED);
      int blueRow = getRowScore(r, PlayerColor.BLUE);
      if (redRow > blueRow && color == PlayerColor.RED) {
        total += redRow;
      } else if (blueRow > redRow && color == PlayerColor.BLUE) {
        total += blueRow;
      }
    }
    return total;
  }

  // ----------------------------------------------------------------------------------------
  // Mutating methods (from IPawnsBoardModel)
  // ----------------------------------------------------------------------------------------

  @Override
  public void drawCardIfPossible() {
    if (isGameOver()) {
      return; // do nothing if game is over
    }
    getCurrentPlayerState().drawCard();
  }

  @Override
  public void placeCard(int handIndex, int row, int col) {
    if (isGameOver()) {
      throw new IllegalStateException("Game is over, cannot place a card");
    }

    // Check valid hand index
    if (handIndex < 0 || handIndex >= getCurrentPlayerState().getHand().size()) {
      throw new IllegalArgumentException("Invalid hand index");
    }

    // Validate (row, col) in range
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Row or col out of bounds");
    }

    // Must have pawns belonging to current player
    Cell cell = board[row][col];
    if (cell.getType() != CellType.PAWNS) {
      throw new IllegalArgumentException("Cannot place card on a cell without pawns");
    }
    if (cell.getOwner() != currentPlayer) {
      throw new IllegalArgumentException("That cell's pawns do not belong to current player");
    }

    // Check cost
    Card cardToPlace = getCurrentPlayerState().getHand().get(handIndex);
    if (cell.getPawnCount() < cardToPlace.getCost()) {
      throw new IllegalArgumentException("Not enough pawns to pay for this card cost");
    }

    // Remove card from hand
    cardToPlace = getCurrentPlayerState().removeCardFromHand(handIndex);

    // Place the card and apply influence
    cell.setCard(currentPlayer, cardToPlace);
    applyInfluence(row, col, cardToPlace);

    // Reset pass flag and switch turn
    this.lastWasPass = false;
    switchPlayer();
  }

  @Override
  public void pass() {
    if (isGameOver()) {
      return;
    }
    if (lastWasPass) {
      // two consecutive passes => game over
      this.gameOver = true;
    } else {
      this.lastWasPass = true;
      switchPlayer();
    }
  }


  // ----------------------------------------------------------------------------------------
  // Private helpers
  // ----------------------------------------------------------------------------------------

  /**
   * Switch the current player from RED <-> BLUE.
   */
  private void switchPlayer() {
    if (this.currentPlayer == PlayerColor.RED) {
      this.currentPlayer = PlayerColor.BLUE;
    } else {
      this.currentPlayer = PlayerColor.RED;
    }
  }

  /**
   * Return the PlayerState for whichever player is current.
   */
  private PlayerState getCurrentPlayerState() {
    return (currentPlayer == PlayerColor.RED) ? redState : blueState;
  }

  /**
   * Apply the influence of the card placed at (centerRow, centerCol).
   * For each 'I' in the 5x5 grid, we either add pawns or convert pawns, ignoring off-board cells.
   * If the current player is BLUE, we horizontally flip the influence pattern.
   */
  private void applyInfluence(int centerRow, int centerCol, Card card) {
    InfluenceGrid ig = card.getInfluenceGrid();
    PlayerColor owner = currentPlayer;

    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        if (ig.getCell(r, c) == 'I') {
          // The center in the grid is (2,2).
          int dr = r - 2;
          int dc = c - 2;

          // For BLUE, flip columns horizontally
          if (owner == PlayerColor.BLUE) {
            dc = -dc;
          }

          int boardR = centerRow + dr;
          int boardC = centerCol + dc;

          // Check board bounds
          if (boardR < 0 || boardR >= rows
                  || boardC < 0 || boardC >= cols) {
            // Off-board => no effect
            continue;
          }

          Cell affectedCell = board[boardR][boardC];
          if (affectedCell.getType() == CellType.CARD) {
            // if cell has a card, do nothing
          } else if (affectedCell.getType() == CellType.EMPTY) {
            // if empty, add 1 pawn for current player
            affectedCell.setPawns(owner, 1);
          } else if (affectedCell.getType() == CellType.PAWNS) {
            // if pawns exist
            if (affectedCell.getOwner() == owner) {
              // same owner => increment by 1, up to 3
              affectedCell.incrementPawns();
            } else {
              // opponent's pawns => convert ownership
              // but keep the same count
              affectedCell.changeOwner(owner);
            }
          }
        }
      }
    }
  }

  @Override
  public boolean canPlaceCard(int handIndex, int row, int col) {
    // If the game is over, no moves are legal
    if (this.isGameOver()) {
      return false;
    }

    // Check valid hand index
    if (handIndex < 0 || handIndex >= getCurrentPlayerState().getHand().size()) {
      return false;
    }

    // Check board bounds
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return false;
    }

    Cell cell = board[row][col];
    // Must have pawns belonging to current player
    if (cell.getType() != CellType.PAWNS) {
      return false;
    }
    if (cell.getOwner() != currentPlayer) {
      return false;
    }

    // Check if enough pawns to pay for the card cost
    Card cardToPlace = getCurrentPlayerState().getHand().get(handIndex);
    if (cell.getPawnCount() < cardToPlace.getCost()) {
      return false;
    }

    // If none of the above checks failed, it's a valid placement
    return true;
  }

  @Override
  public int getDeckSize(PlayerColor color) {
    if (color == PlayerColor.RED) {
      return redState.getDeckSize();
    } else {
      return blueState.getDeckSize();
    }
  }


}

