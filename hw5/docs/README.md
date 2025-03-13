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
/src
/cs3500/Model
- Card.java
- Cell.java
- CellType.java
- InfluenceGrid.java
- DeckReader.java
- PlayerState.java
- PawnsBoardModel.java
/cs3500/Controller
- PawnsBoard.java  (Entry point)
/cs3500/View
- TextualView.java

/docs
- deck.config  (Deck configuration file)

/tests
- CardTest.java
- CellTest.java
- DeckReaderTest.java
- InfluenceGridTest.java
- PawnsBoardModelTest.java
```

------------------------------ Draft 2 ------------------------

Pawns Board - README

Overview

This project implements Pawns Board, a two-player strategy card game inspired by Queen’s Blood. The game is played on a rectangular board where players take turns playing custom cards to influence pawns.

Each player starts with a deck of cards, which is read from a configuration file. Players can place cards if they have enough pawns to cover the cost. Placing a card applies influence based on the card’s unique 5x5 influence grid. The game ends when both players pass consecutively, and the winner is determined based on row-based scoring.

High-Level Assumptions

The board size is defined at the beginning of the game and remains constant.

Each player has a deck read from a configuration file (deck.config).

Each deck can contain at most two copies of the same card.

Influence is applied based on a 5x5 influence grid for each card.

The game follows the Model-View-Controller (MVC) pattern for future extensions.

A text-based interface is used to visualize the game state.

Quick Start

To initialize and run the game, use the following code in the PawnsBoard entry point:

public static void main(String[] args) {
    File deckFile = new File("docs/deck.config");
    List<Card> deck = DeckReader.readDeckFromFile(deckFile);

    PawnsBoardModel model = new PawnsBoardModel(3, 5, deck, deck, 5);
    System.out.println("Game initialized with a 3x5 board.");
}

This runs a game where:

The deck is read from docs/deck.config.

Both players use the same deck.

The board is initialized with 3 rows and 5 columns.

The initial hand size is 5.

Key Components

Model (Game Logic)

PawnsBoardModel: Manages the game state, rules, and turn logic.

Cell: Represents a single cell on the board.

CellType: Enum representing EMPTY, PAWNS, or CARD states.

Card: Defines a card’s properties, including its name, cost, value, and influence grid.

InfluenceGrid: Handles the 5x5 influence effect of a card.

PlayerState: Tracks each player’s deck and hand.

PlayerColor: Enum for RED or BLUE.

ReadOnlyCell: Provides an immutable snapshot of a cell’s state.

View (Text Rendering)

TextualView: Renders the game state in a text-based format.

Controller (Game Execution)

PawnsBoard: Entry point that initializes the game model, reads deck configurations, and runs the game loop.

Source Organization

/src
  /cs3500/Model
    - Card.java
    - Cell.java
    - CellType.java
    - InfluenceGrid.java
    - DeckReader.java
    - PlayerState.java
    - PawnsBoardModel.java

  /cs3500/Controller
    - PawnsBoard.java  (Entry point)

  /cs3500/View
    - TextualView.java

/docs
  - deck.config  (Deck configuration file)

/tests
  - CardTest.java
  - CellTest.java
  - DeckReaderTest.java
  - InfluenceGridTest.java
  - PawnsBoardModelTest.java

Deck Configuration Format

The deck configuration file (deck.config) follows this structure:

CARD_NAME COST VALUE
ROW_0
ROW_1
ROW_2
ROW_3
ROW_4

Each card consists of:

CARD_NAME: The name of the card (no spaces).

COST: A number between 1 and 3 (pawn cost required to place the card).

VALUE: A positive integer representing the card’s contribution to scoring.

5 lines of 5 characters each, representing the card’s influence grid.

Characters in the influence grid:

X: No influence.

I: Influence applied.

C: The card's placement (always the center of the grid).

Example Deck Configuration File

Guardian 2 5
XXXXX
XXIXX
XICIX
XXIXX
XXXXX

Bee 1 1
XXIXX
XXXXX
XXCXX
XXXXX
XXIXX

How the Game Works

Turn-Based Actions

Current player draws a card (if available).

Player can either:

Place a card (if they have enough pawns to cover the cost).

Pass their turn.

Placing a card applies influence based on its 5x5 grid.

Turn switches to the other player.

Game ends when both players pass consecutively.

Winner is determined based on row-based scoring.

Scoring System

Each row has a row-score for both players (sum of card values in that row).

The player with the higher row-score wins that row.

If a player wins a row, they add its row-score to their total score.

The player with the higher total score wins the game.

If the scores are equal, the game is a tie.

Future Work

Implement graphical interface (GUI) instead of text-based view.

Introduce AI-based user-players with different strategies.

Expand networked multiplayer functionality.





