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



