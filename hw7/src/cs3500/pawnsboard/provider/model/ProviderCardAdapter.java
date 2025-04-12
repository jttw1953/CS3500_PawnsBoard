package cs3500.pawnsboard.provider.model;

/**
 * An adapter that wraps your real Card and exposes
 * the provider's Card interface.
 */
public class ProviderCardAdapter implements Card {

    private final cs3500.pawnsboard.model.Card realCard;

    /**
     * Construct an adapter around your real Card.
     *
     * @param realCard the actual Card from your model
     */
    public ProviderCardAdapter(cs3500.pawnsboard.model.Card realCard) {
        if (realCard == null) {
            throw new IllegalArgumentException("realCard cannot be null");
        }
        this.realCard = realCard;
    }

    @Override
    public String getName() {
        return realCard.getName();
    }

    @Override
    public int getCost() {
        return realCard.getCost();
    }

    @Override
    public int getValue() {
        return realCard.getValue();
    }

    /*
     * If the provider's code calls more Card methods (e.g., getInfluenceGrid()),
     * add them here so they delegate to realCard as well.
     */
}
