package cs3500.pawnsboard.model;

import java.util.Collections;
import java.util.List;

/**
 * Concrete implementation of IPawnsBoardModel for Pawns Board.
 */
public class PawnsBoardModel implements IPawnsBoardModel {
  private final int rows;
  private final int cols;

  // Track the two player states
  private final PlayerState redState;
  private final PlayerState blueState;

  // Current player's color
  private PlayerColor currentPlayer;

  private final Cell[][] board;

  // For detecting game end (two consecutive passes)
  private boolean lastWasPass;
  private boolean gameOver;

  /**
   * Construct a PawnsBoardModel.
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
    Collections.shuffle(redDeck);
    Collections.shuffle(blueDeck);

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
  public void drawCardIfPossible() {
    if (isGameOver()) {
      return; // do nothing if game is over
    }
    getCurrentPlayerState().drawCard();
  }

  @Override
  public void placeCard(int handIndex, int row, int col) {
    if (isGameOver()) {
      throw new IllegalStateException("Game is over, cannot place card");
    }
    // 1. Validate (row, col) in range
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Row or col out of bounds");
    }

    // 2. Check board cell must have pawns belonging to current player
    Cell cell = board[row][col];
    if (cell.getType() != CellType.PAWNS) {
      throw new IllegalArgumentException("Cannot place card on a cell without pawns");
    }
    if (cell.getOwner() != currentPlayer) {
      throw new IllegalArgumentException("That cell's pawns do not belong to current player");
    }

    // 3. Check if enough pawns to cover cost
    Card cardToPlace = getCurrentPlayerState().getHand().get(handIndex);
    if (cell.getPawnCount() < cardToPlace.getCost()) {
      throw new IllegalArgumentException("Not enough pawns to pay for this card cost");
    }

    // 4. Remove the card from hand
    cardToPlace = getCurrentPlayerState().removeCardFromHand(handIndex);

    // 5. Remove pawns from the cell => place the card
    cell.setCard(currentPlayer, cardToPlace);

    // 6. Apply influence
    applyInfluence(row, col, cardToPlace);

    // 7. Switch turn
    this.lastWasPass = false; // reset pass
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
      throw new IllegalArgumentException("Out of bounds");
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
      throw new IllegalArgumentException("Invalid row");
    }
    int score = 0;
    for (int c = 0; c < cols; c++) {
      Cell cell = board[row][c];
      if (cell.getType() == CellType.CARD && cell.getOwner() == color) {
        // Add the card's value
        Card card = cell.getCard();
        score += card.getValue();
      }
    }
    return score;
  }

  @Override
  public int getTotalScore(PlayerColor color) {
    // For each row: compare row-scores for RED vs BLUE
    // The color that "wins" that row gets that row's entire sum for themselves.
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

    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (ig.getCell(row, col) == 'I') {
          int dr = row - 2;
          int dc = col - 2;

          // For BLUE, we flip columns => i.e. dc -> -dc
          if (owner == PlayerColor.BLUE) {
            dc = -dc;
          }

          int boardR = centerRow + dr;
          int boardC = centerCol + dc;

          // check bounds
          if (boardR < 0 || boardR >= rows || boardC < 0 || boardC >= cols) {
            // off-board => no effect
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
              // same owner => increment by 1 up to 3
              affectedCell.incrementPawns();
            } else {
              // opponent's pawns => change ownership but keep the same count
              affectedCell.changeOwner(owner);
            }
          }
        }
      }
    }
  }
}
