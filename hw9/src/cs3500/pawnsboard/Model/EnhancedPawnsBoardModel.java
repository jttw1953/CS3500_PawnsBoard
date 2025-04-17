package cs3500.pawnsboard.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Variant of PawnsBoardModel that supports U (upgrade) and D (devalue) influences.
 */
public class EnhancedPawnsBoardModel implements IPawnsBoardModel {
    private final int rows;
    private final int cols;

    protected final PlayerState redState;
    private final PlayerState blueState;

    private PlayerColor currentPlayer;
    private boolean lastWasPass;
    private boolean gameOver;

    // Board cells
    protected final Cell[][] board;
    // Value modifiers per cell from U/D influences
    private final int[][] valueModifiers;

    private final List<ModelObserver> observers = new ArrayList<>();

    /**
     * Constructs the enhanced model.
     */
    public EnhancedPawnsBoardModel(int rows, int cols,
                                   List<Card> redDeck, List<Card> blueDeck,
                                   int initialHandSize) {
        if (rows <= 0) throw new IllegalArgumentException("Rows must be > 0");
        if (cols <= 1 || cols % 2 == 0) throw new IllegalArgumentException("Cols must be >1 and odd");
        if (redDeck == null || blueDeck == null) throw new IllegalArgumentException("Decks cannot be null");
        int redMax = (int)Math.ceil(redDeck.size()/3.0);
        int blueMax = (int)Math.ceil(blueDeck.size()/3.0);
        if (initialHandSize > redMax || initialHandSize > blueMax) {
            throw new IllegalArgumentException("Initial hand size too large");
        }

        this.rows = rows;
        this.cols = cols;
        this.board = new Cell[rows][cols];
        this.valueModifiers = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Cell();
                valueModifiers[r][c] = 0;
            }
        }

        // Place starting pawns
        for (int r = 0; r < rows; r++) {
            board[r][0].setPawns(PlayerColor.RED, 1);
            board[r][cols - 1].setPawns(PlayerColor.BLUE, 1);
        }

        this.redState = new PlayerState(PlayerColor.RED, redDeck);
        this.blueState = new PlayerState(PlayerColor.BLUE, blueDeck);
        for (int i = 0; i < initialHandSize; i++) {
            if (redState.hasNextCard()) redState.drawCard();
            if (blueState.hasNextCard()) blueState.drawCard();
        }

        this.currentPlayer = PlayerColor.RED;
        this.lastWasPass = false;
        this.gameOver = false;
    }

    /** Copy constructor */
    private EnhancedPawnsBoardModel(EnhancedPawnsBoardModel that) {
        this.rows = that.rows;
        this.cols = that.cols;
        this.gameOver = that.gameOver;
        this.lastWasPass = that.lastWasPass;
        this.currentPlayer = that.currentPlayer;
        this.board = new Cell[rows][cols];
        this.valueModifiers = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.board[r][c] = new Cell();
                ReadOnlyCell roc = new ReadOnlyCell(that.board[r][c]);
                switch (roc.type) {
                    case PAWNS:
                        board[r][c].setPawns(roc.owner, roc.pawnCount);
                        break;
                    case CARD:
                        Card card = that.board[r][c].getCard();
                        board[r][c].setCard(roc.owner, card);
                        break;
                    default:
                        board[r][c].setEmpty();
                }
                this.valueModifiers[r][c] = that.valueModifiers[r][c];
            }
        }
        this.redState = new PlayerState(that.redState);
        this.blueState = new PlayerState(that.blueState);
    }

    @Override public IPawnsBoardModel copyModel() {
        return new EnhancedPawnsBoardModel(this);
    }

    @Override public void addObserver(ModelObserver obs) {
        if (obs != null) observers.add(obs);
    }
    private void notifyTurnChanged() {
        for (ModelObserver o : observers) o.onTurnChanged(currentPlayer);
    }
    private void notifyGameOver() {
        for (ModelObserver o : observers) o.onGameOver(getWinner());
    }

    // -------------------------------------------------------
    // Read-only methods
    // -------------------------------------------------------
    @Override public int getRows() { return rows; }
    @Override public int getCols() { return cols; }
    @Override public PlayerColor getCurrentPlayer() { return currentPlayer; }
    @Override public boolean isGameOver() { return gameOver; }
    @Override public PlayerColor getWinner() {
        if (!gameOver) return null;
        int rScore = getTotalScore(PlayerColor.RED);
        int bScore = getTotalScore(PlayerColor.BLUE);
        if (rScore > bScore) return PlayerColor.RED;
        if (bScore > rScore) return PlayerColor.BLUE;
        return null;
    }
    @Override public ReadOnlyCell getCellState(int row, int col) {
        return new ReadOnlyCell(board[row][col]);
    }
    @Override public List<Card> getCurrentPlayerHand() {
        List<Card> hand = getCurrentPlayerState().getHand();
        return Collections.unmodifiableList(hand);
    }
    @Override public int getRowScore(int row, PlayerColor color) {
        int total = 0;
        for (int c = 0; c < cols; c++) {
            ReadOnlyCell cell = getCellState(row, c);
            if (cell.type == CellType.CARD && cell.owner == color) {
                Card card = board[row][c].getCard();
                int eff = card.getValue() + valueModifiers[row][c];
                total += Math.max(eff, 0);
            }
        }
        return total;
    }
    @Override public int getTotalScore(PlayerColor color) {
        int sum = 0;
        for (int r = 0; r < rows; r++) {
            int redRow = getRowScore(r, PlayerColor.RED);
            int blueRow = getRowScore(r, PlayerColor.BLUE);
            if (redRow > blueRow && color == PlayerColor.RED) sum += redRow;
            if (blueRow > redRow && color == PlayerColor.BLUE) sum += blueRow;
        }
        return sum;
    }

    private PlayerState getCurrentPlayerState() {
        return (currentPlayer == PlayerColor.RED) ? redState : blueState;
    }

    // -------------------------------------------------------
    // Mutating methods
    // -------------------------------------------------------
    @Override public void drawCardIfPossible() {
        if (gameOver) return;
        getCurrentPlayerState().drawCard();
    }

    @Override public void placeCard(int handIndex, int row, int col) {
        if (gameOver) throw new IllegalStateException("Game is over");
        PlayerState state = getCurrentPlayerState();
        if (handIndex < 0 || handIndex >= state.getHand().size())
            throw new IllegalArgumentException("Invalid hand index");
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IllegalArgumentException("Out of bounds");

        Cell cell = board[row][col];
        if (cell.getType() != CellType.PAWNS || cell.getOwner() != currentPlayer)
            throw new IllegalArgumentException("Cannot place there");

        Card card = state.getHand().get(handIndex);
        if (cell.getPawnCount() < card.getCost())
            throw new IllegalArgumentException("Not enough pawns");

        // Remove card from hand
        Card placed = state.removeCardFromHand(handIndex);

        int eff = placed.getValue() + valueModifiers[row][col];
        if (eff <= 0) {
            // Card is immediately removed; leave cost as pawns, reset modifier
            cell.setPawns(currentPlayer, placed.getCost());
            valueModifiers[row][col] = 0;
        } else {
            cell.setCard(currentPlayer, placed);
            applyInfluence(row, col, placed);
        }

        lastWasPass = false;
        switchPlayer();
    }

    @Override public void pass() {
        if (gameOver) return;
        if (lastWasPass) {
            gameOver = true;
            notifyGameOver();
        } else {
            lastWasPass = true;
            switchPlayer();
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == PlayerColor.RED)
                ? PlayerColor.BLUE
                : PlayerColor.RED;
        notifyTurnChanged();
    }

    /** Applies I/U/D influences from the placed card. */
    private void applyInfluence(int centerR, int centerC, Card card) {
        InfluenceGrid ig = card.getInfluenceGrid();
        PlayerColor owner = currentPlayer;

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                char ch = ig.getCell(r, c);
                if (ch == 'X' || ch == 'C') continue;

                int dr = r - 2, dc = c - 2;
                if (owner == PlayerColor.BLUE) dc = -dc;
                int br = centerR + dr, bc = centerC + dc;
                if (br < 0 || br >= rows || bc < 0 || bc >= cols) continue;

                Cell tgt = board[br][bc];
                switch (ch) {
                    case 'I':
                        if (tgt.getType() == CellType.EMPTY) {
                            tgt.setPawns(owner, 1);
                        } else if (tgt.getType() == CellType.PAWNS) {
                            if (tgt.getOwner() == owner) {
                                tgt.incrementPawns();
                            } else {
                                tgt.changeOwner(owner);
                            }
                        }
                        break;
                    case 'U':
                        valueModifiers[br][bc]++;
                        break;
                    case 'D':
                        valueModifiers[br][bc]--;
                        break;
                }
            }
        }
    }

    @Override public boolean canPlaceCard(int handIndex, int row, int col) {
        if (gameOver) return false;
        PlayerState st = getCurrentPlayerState();
        if (handIndex < 0 || handIndex >= st.getHand().size()) return false;
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        Cell cell = board[row][col];
        if (cell.getType() != CellType.PAWNS || cell.getOwner() != currentPlayer)
            return false;
        Card card = st.getHand().get(handIndex);
        return cell.getPawnCount() >= card.getCost();
    }

    @Override public int getDeckSize(PlayerColor color) {
        return (color == PlayerColor.RED)
                ? redState.getDeckSize()
                : blueState.getDeckSize();
    }
}
