This README explains the changes we made to support our customers’ requests in HW8.

Files Changed or Added for Our Customers:
-----------------------------------------

1. **ICard.java**
   - We introduced this interface to expose only the public methods of our `Card` class (e.g., `getName`, `getCost`, `getValue`, `getInfluenceGrid`).
   - This allowed views and strategies to interact with cards without relying on our concrete implementation.
   - Necessary because customers requested that views not depend on `Card`.

2. **IInfluenceGrid.java**
   - This interface represents a read-only abstraction of the `InfluenceGrid` used inside our `Card` class.
   - Implemented by `InfluenceGrid` so that view logic could safely access grid data.
   - Requested by customers so that rendering and strategies wouldn't require access to model internals.

3. **Updated usages of Card → ICard and InfluenceGrid → IInfluenceGrid**
   - In classes like `HandPanel`, `ReadOnlyPawnsBoardModel`, and parts of our strategy code, we replaced all `Card` references with `ICard`, and `InfluenceGrid` with `IInfluenceGrid`.
   - This helped decouple the view from the model and allowed external integration without leaking implementation details.

Why These Changes Were Necessary:
---------------------------------
Our customers (other teams in the class) needed to be able to plug in their own views and strategies without being tied to our specific model implementation. To make our model pluggable and view-safe, we exposed only interfaces (`ICard`, `IInfluenceGrid`) in the relevant public-facing code. This made our model more modular and interface-compliant, as required by the assignment.

We also ensured that `ReadOnlyCell` and `ReadOnlyPawnsBoardModel` used these interfaces where applicable, and ensured that no concrete `Card` or `InfluenceGrid` types were leaked through those APIs.

All of these changes helped ensure compatibility, flexibility, and clean design boundaries between model, view, and controller.
