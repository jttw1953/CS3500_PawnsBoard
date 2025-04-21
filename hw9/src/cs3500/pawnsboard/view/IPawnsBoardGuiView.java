package cs3500.pawnsboard.view;

import cs3500.pawnsboard.controller.IPawnsBoardController;

/**
 * A general interface for the PawnsBoard GUI view.
 * In part 2, we primarily just need to show the frame and let the user interact.
 * In part 3, we might add more methods for the controller to call.
 */
public interface IPawnsBoardGuiView {

  /**
   * Make the view visible (if it isn’t already).
   */
  void makeVisible();

  /**
   * Force the view to redraw itself from the model.
   */
  void refresh();

  /**
   * Set the controller that will handle user interactions (mouse, keys).
   */
  void setController(IPawnsBoardController controller);

  IPawnsBoardController getController();
}
