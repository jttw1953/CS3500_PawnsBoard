package cs3500.pawnsboard.provider.view;

import cs3500.pawnsboard.provider.controller.IPawnsBoardController;
import cs3500.pawnsboard.provider.model.ReadOnlyPawnsBoardModel;

/**
 * A minimal do-nothing implementation of IPawnsBoardGuiView,
 * just so we can compile and run. Real functionality would
 * come from the provider's code if it existed.
 */
public class ProviderViewImpl implements IPawnsBoardGuiView {

    private final ReadOnlyPawnsBoardModel model;
    private IPawnsBoardController controller;

    public ProviderViewImpl(ReadOnlyPawnsBoardModel model) {
        this.model = model;
    }

    @Override
    public void makeVisible() {
        // If we had real GUI code from the provider, we'd open a JFrame etc.
        System.out.println("Pretending to show provider's view...");
    }

    @Override
    public void refresh() {
        // Redraw logic would go here
        System.out.println("Pretending to refresh provider's view...");
    }

    @Override
    public void setController(IPawnsBoardController controller) {
        this.controller = controller;
    }
}
