package cs3500.pawnsboard.provider.model;

/**
 * Utility class to convert between your real PlayerColor enum
 * and the provider's PlayerColor enum.
 */
public final class ProviderColorAdapter {

  private ProviderColorAdapter() {
    // private constructor to prevent instantiation of utility class
  }

  /**
   * Converts your model's color to the provider's color.
   *
   * @param realColor the color from your model
   * @return the corresponding provider enum value
   * @throws IllegalArgumentException if the color is unrecognized
   */
  public static cs3500.pawnsboard.provider.model.PlayerColor toProviderColor(
          cs3500.pawnsboard.model.PlayerColor realColor) {
    if (realColor == null) {
      return null;
    }
    switch (realColor) {
      case RED:
        return cs3500.pawnsboard.provider.model.PlayerColor.RED;
      case BLUE:
        return cs3500.pawnsboard.provider.model.PlayerColor.BLUE;
      default:
        throw new IllegalArgumentException("Unrecognized color: " + realColor);
    }
  }

  /**
   * Converts a {@code PlayerColor} from the provider's enum into the corresponding color
   * in our own implementation.
   * @param providerColor the color enum from the provider’s codebase
   * @return the equivalent {@code PlayerColor} from our implementation,
   *     or {@code null} if input is null
   * @throws IllegalArgumentException if the provided color is not recognized
   */
  public static cs3500.pawnsboard.model.PlayerColor toRealColor(
          cs3500.pawnsboard.provider.model.PlayerColor providerColor) {
    if (providerColor == null) {
      return null;
    }
    switch (providerColor) {
      case RED:
        return cs3500.pawnsboard.model.PlayerColor.RED;
      case BLUE:
        return cs3500.pawnsboard.model.PlayerColor.BLUE;
      default:
        throw new IllegalArgumentException("Unrecognized provider color: " + providerColor);
    }
  }


}
