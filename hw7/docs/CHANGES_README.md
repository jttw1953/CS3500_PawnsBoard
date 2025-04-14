Final Project Summary – HW8

Features Implemented:
---------------------
- Fully functional game model with:
  - Card placement and influence effects
  - Row scoring and total scoring
  - Turn handling and automatic card drawing
  - Game-over detection after two passes
- GUI-based view built with Swing:
  - Board and hand rendering
  - Cell and card highlighting
  - Mouse click interaction and controller-driven updates
- Multiple strategies: FillFirst, MaxRowScore, MaximizeControl
- Working GUI controller with turn enforcement and validation
- Complete integration with both customer and provider systems
- Cleanly defined public-facing interfaces: ICard, IInfluenceGrid, etc.
- Working `.jar` file with CLI-based player configuration:
  java -jar hw8.jar -gui RED -strategy BLUE fill-first docs/deck.config docs/deck.config

Missing or Incomplete Features:
-------------------------------
- The provider's graphical view was not implemented, so `ProviderViewImpl` is only a placeholder
- No sound effects or animations in GUI
- Limited GUI testing (manual verification only)

Testing Summary:
----------------
We wrote comprehensive tests for:
- Model mechanics (card placement, scoring, draw/pass rules)
- Strategies (move decisions in various scenarios)
- View smoke tests (GUI instantiation, mouse listeners)
- Integration via controller logic

All relevant tests from Assignments 5–7 were included in the submission. None had to be excluded due to space constraints.
