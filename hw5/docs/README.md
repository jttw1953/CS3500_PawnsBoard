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





