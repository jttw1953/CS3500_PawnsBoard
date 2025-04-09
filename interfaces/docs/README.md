## Overview
This project implements Pawns Board, a two-player strategy card game inspired by Queen’s Blood. 
The game is played on a rectangular board where players can use custom cards to influence pawns.

Each player starts with a deck of cards, takes turns playing cards, 
and can influence the board by adding or converting pawns. 
The game ends when both players pass consecutively, 
and the winner is determined based on row-based scoring.

### High Level Assumptions
- The board size is defined at the beginning of the game.
- Each player has a unique deck, drawn from a configuration file.
- Influence is applied based on a 5x5 influence grid for each card.
- The game follows the MVC pattern to allow for future extensions.

## Quick Start

The code below initialises and runs the game.

The code below runs a game where:
- Reads the deck from the file path "docs/deck.config"
- Both players are given the same deck
- Initializes a 3-row, 5-column board
- Initial deck hand size of 5

```
public static void main(String[] args) {
File deckFile = new File("docs/deck.config");
List<Card> deck = DeckReader.readDeckFromFile(deckFile);

    PawnsBoardModel model = new PawnsBoardModel(3, 5, deck, deck, 5);
    
    System.out.println("Game initialized with a 3x5 board.");
}
```

## Key Components and Subcomponents
- Model
  - PawnsBoardModel: Manages the game state, rules, and turn logic
  - Cell: Represents a single cell on the board
    - CellType: Enum representing EMPTY, PAWNS, or CARD states.
  - Card: Defines the properties of a card, including its name, cost, value, and influence grid. 
  - InfluenceGrid: Handles the 5x5 influence effect of a card.
  - PlayerState: Tracks each player’s deck and hand. 
    - PlayerColor: Enum for RED or BLUE player.
  - ReadOnlyCell: Provides an immutable snapshot of a cell’s state.
- View
  - TextualView: responsible for rendering the game state in text format
- Controller 
  - PawnsBoard - Entry point that initializes the game model, reads deck configurations, and runs the game.

## Source Organization
```
/src/cs3500/pawnsboard
├── Controller/
│   ├── IPawnsBoardController.java
│   └── StubController.java
├── Model/
│   ├── Card.java, Cell.java, CellType.java, InfluenceGrid.java
│   ├── PawnsBoardModel.java, IPawnsBoardModel.java, ReadOnlyPawnsBoardModel.java
│   ├── PlayerState.java, PlayerColor.java, ReadOnlyCell.java
├── Strategy/
│   ├── Strategy.java, Move.java
│   ├── FillFirstStrategy.java, MaxRowScoreStrategy.java
├── Util/
│   └── DeckReader.java
├── View/
│   ├── TextualView.java
│   ├── PawnsBoardSwingView.java, IPawnsBoardGuiView.java
│   └── BoardPanel.java, HandPanel.java
└── PawnsBoard.java
test/cs3500/pawnsboard
├── Model/
│   ├── CardTest.java
│   ├── CellTest.java
│   ├── DeckReaderTest.java
│   ├── EdgeCaseTest.java
│   ├── InfluenceGridTest.java
│   ├── PawnsBoardModelTest.java
│   └── PlayerStateTest.java
├── Strategy/
│   ├── FillFirstStrategyTest.java
│   ├── MaxRowScoreStrategyTest.java
│   └── MoveTest.java
└── View/
    ├── TextualViewTest.java
    └── PawnsBoardSwingViewTest.java
```


## Changes for Part 2

- Added a full **GUI-based view** (`PawnsBoardSwingView`) using Java Swing. It displays the board (`BoardPanel`), the current player's hand (`HandPanel`), and a status area showing player turn and deck sizes.
- Introduced the `ReadOnlyPawnsBoardModel` interface to enforce **view-only access** from GUI and textual views.
- Extended the controller with `IPawnsBoardController` and a `StubController` to support keyboard and mouse inputs.
- Strategies (`FillFirstStrategy` and `MaxRowScoreStrategy`) were implemented to support AI-driven decisions based on pawn placement and row score evaluation.
- Added mock-based tests and transcripts to verify strategy logic without mutating the actual model.

## Strategy Design

This project implements two AI strategies for the Red player:

- **FillFirstStrategy** (`FillFirstStrategy.java`): Scans the board in row-major order and places the first valid card on a cell the player owns with enough pawns to pay the cost.
- **MaxRowScoreStrategy** (`MaxRowScoreStrategy.java`): Attempts to place a card in a row where the player is losing or tied, to gain a scoring advantage.

Both strategies implement the `Strategy` interface. If no legal move is found, they pass.

Test coverage is provided via mock-based tests in:
- `FillFirstStrategyTest.java`
- `MaxRowScoreStrategyTest.java`

The required transcripts (`strategy-transcript-first.txt` and `strategy-transcript-score.txt`) show the mocked method calls made by each strategy.

## Extra Credit Strategies
To improve our move-selection logic in PawnsBoard, we implemented several strategies using the Strategy and Composite design patterns. In addition to our baseline strategies, we added two new ones for extra credit. Below is a summary of each strategy and their roles in our system:

1. Basic Strategies
FillFirstStrategy
Purpose:
This strategy scans through the current player's hand and then through the board (in row-major order) to find the first valid cell where a card can be placed. If a valid move is found, it returns that move; otherwise, it passes.

Location:
cs3500.pawnsboard.Strategy.FillFirstStrategy.java

Notes:

It performs basic legality checks (e.g., cell must have enough pawns, belong to the current player, and be empty of cards).

It’s very straightforward and tends to pick the first available move.

MaxRowScoreStrategy
Purpose:
This strategy scans the board row by row to identify a row where the current player is losing or tied with the opponent. It then tries to find a card placement that will improve that row's score, choosing a move that increases the player’s row-score. If no such move exists, it passes.

Location:
cs3500.pawnsboard.Strategy.MaxRowScoreStrategy.java

Notes:

It is more selective than FillFirstStrategy and aims to win a specific row.

For simplicity, it assumes that any card with a positive value is helpful in increasing the score.

Move
Purpose:
The Move class encapsulates a move in the game. It represents either a pass or a placement move (with the hand index, row, and column where the card should be played).

Location:
cs3500.pawnsboard.Strategy.Move.java

Notes:

It provides static factory methods passMove() and placeMove(...) for creating moves.

Our tests (see MoveTest.java) ensure that the move objects correctly represent pass moves and placement moves.

2. Extra Credit Strategies
ControlBoardStrategy
Purpose:
This strategy examines every valid move by simulating the move on a copy of the current board. It then evaluates the move based on “board control” – the number of cells that end up with a card owned by the current player. The move yielding the highest control is selected.

Location:
cs3500.pawnsboard.Strategy.ControlBoardStrategy.java

Key Features:

Uses a deep copy of the model (via copyModel()) so that simulation does not affect the original state.

Iterates through all cards and board cells to check valid moves.

Implements tie-breaking by selecting the move encountered first in row-major order.

Testing:
The tests in ControlBoardStrategyTest.java simulate various board states and verify that the strategy picks the move that maximizes board control.

CompositeStrategy
Purpose:
This strategy lets us combine multiple strategies into one cohesive move-selection process. It accepts a list of strategies and, when invoked, iterates through them until one returns a non-pass move. If all strategies return pass, then it passes.

Location:
cs3500.pawnsboard.Strategy.CompositeStrategy.java

Key Features:

Supports dynamic composition of simpler strategies (e.g., you might chain ControlBoardStrategy with FillFirstStrategy).

Facilitates flexibility; you can easily rearrange or extend the composite by adding new strategies.

Testing:
The tests in CompositeStrategyTest.java create stub strategies (one that always passes and one that returns a placement move) to verify that the composite returns the expected move according to its ordering.

3. Overall Benefits and Testing
Modularity:
Each strategy is implemented as a self-contained class conforming to our Strategy interface, making it easy to test and maintain.

Flexibility:
With CompositeStrategy, we can easily recombine strategies to form more sophisticated decision logic without rewriting existing code.

Testability:
We have written unit tests for each strategy (see FillFirstStrategyTest.java, MaxRowScoreStrategyTest.java, ControlBoardStrategyTest.java, and CompositeStrategyTest.java) that simulate various board states and verify correct move selection.

Documentation:
This README section, along with inline comments in the code, details the design and expected behavior of each strategy.

By implementing these extra strategies and providing comprehensive tests, our project now demonstrates an advanced use of the Strategy and Composite patterns. This allows for flexible move selection and sets a solid foundation for further improvements in our game’s AI.

## Changes for Part 3

This phase introduced full game interactivity by connecting the model and view through a real controller. The game is now fully playable via mouse clicks and keyboard input, with turn enforcement, user input handling, and move validation.

### Summary of Key Changes

- Added a complete controller: `PawnsBoardController`
- Created a controller interface: `IPawnsBoardController`
- Modularized GUI by separating card-handling into a new `HandPanel` class
- Updated the GUI view to enforce turn-based logic and support selection behavior
- Refactored the view to better follow MVC design principles

### New Classes

#### `PawnsBoardController.java`
- Implements `IPawnsBoardController`
- Validates and applies user actions (e.g., cell click, card select, confirm, pass)
- Refreshes the view after each action
- Prevents illegal moves and enforces correct turn order

#### `IPawnsBoardController.java`
- Interface used by the GUI to communicate player actions to the controller
- Declares methods:
  - `onCellClicked(int row, int col)`
  - `onCardClicked(int cardIndex)`
  - `onPass()`
  - `onConfirm()`

#### `HandPanel.java`
- New visual component for rendering the player's hand
- Detects clicks to select or deselect cards
- Highlights selected card and notifies controller of actions
- Automatically adjusts layout based on number of cards

### Design Updates

- Turn logic is now enforced in the controller
- View and controller are more cleanly separated
- The model is updated only through validated controller actions
- GUI feedback is tied to controller decisions, improving user experience