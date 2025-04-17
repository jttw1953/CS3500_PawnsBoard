package cs3500.pawnsboard.provider.view;

import cs3500.pawnsboard.provider.controller.IPawnsBoardController;
import cs3500.pawnsboard.provider.model.ReadOnlyPawnsBoardModel;

/**
 * A minimal do-nothing implementation of IPawnsBoardGuiView,
 * just so we can compile and run. Real functionality would
 * come from the provider's code if it existed.
 */
public class ProviderViewImpl implements IPawnsBoardGuiView {

  /**
   * Constructs a placeholder provider view. This constructor does not initialize any actual GUI.
   * @param model the read-only view of the game model, used in a real GUI implementation
   */
  public ProviderViewImpl(ReadOnlyPawnsBoardModel model) {
    // No-op stub constructor
  }

  /**
   * Simulates making the GUI visible. In a real implementation, this would create and display
   * a GUI window (e.g., a JFrame).
   */
  @Override
  public void makeVisible() {
    System.out.println("Pretending to show provider's view...");
  }

  /**
   * Simulates refreshing the GUI display. In a real implementation, this would repaint the board,
   * hand, and scores based on the model state.
   */
  @Override
  public void refresh() {
    System.out.println("Pretending to refresh provider's view...");
  }

  /**
   * Accepts a controller that would be used to handle input in a real provider GUI.
   * This implementation does not store or use the controller.
   * @param controller the controller handling user input and events
   */
  @Override
  public void setController(IPawnsBoardController controller) {
    // No-op stub method
  }
}
