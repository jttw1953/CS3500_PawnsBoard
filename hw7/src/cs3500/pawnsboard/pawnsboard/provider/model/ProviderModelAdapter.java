package cs3500.pawnsboard.provider.model;

import cs3500.pawnsboard.Model.PawnsBoardModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapts your real PawnsBoardModel to the provider's ReadOnlyPawnsBoardModel interface.
 */
public class ProviderModelAdapter implements ReadOnlyPawnsBoardModel {

    private final PawnsBoardModel realModel;

    /**
     * Construct an adapter around your real PawnsBoardModel.
     *
     * @param realModel the actual PawnsBoardModel from your code
     */
    public ProviderModelAdapter(PawnsBoardModel realModel) {
        if (realModel == null) {
            throw new IllegalArgumentException("realModel cannot be null");
        }
        this.realModel = realModel;
    }

    @Override
    public int getRows() {
        return realModel.getRows();
    }

    @Override
    public int getCols() {
        return realModel.getCols();
    }

    /**
     * Return the current player color in the *provider's* color enum.
     */
    @Override
    public PlayerColor getCurrentPlayer() {
        // Your real model returns cs3500.pawnsboard.Model.PlayerColor,
        // which we convert to cs3500.pawnsboard.provider.model.PlayerColor.
        cs3500.pawnsboard.Model.PlayerColor realColor = realModel.getCurrentPlayer();
        return ProviderColorAdapter.toProviderColor(realColor);
    }

    @Override
    public boolean isGameOver() {
        return realModel.isGameOver();
    }

    @Override
    public PlayerColor getWinner() {
        cs3500.pawnsboard.Model.PlayerColor realWinner = realModel.getWinner();
        if (realWinner == null) {
            return null;
        }
        return ProviderColorAdapter.toProviderColor(realWinner);
    }

    @Override
    public ReadOnlyCell getCellState(int row, int col) {
        // Your real model returns a cs3500.pawnsboard.Model.ReadOnlyCell,
        // which we must wrap in ProviderCellAdapter (which implements the *provider*'s ReadOnlyCell).
        cs3500.pawnsboard.Model.ReadOnlyCell realCell = realModel.getCellState(row, col);
        return new ProviderCellAdapter(realCell);
    }

    @Override
    public List<Card> getCurrentPlayerHand() {
        // realModel gives a List<cs3500.pawnsboard.Model.Card>.
        // We transform that into a List<provider.model.Card> by wrapping each in a ProviderCardAdapter.
        List<cs3500.pawnsboard.Model.Card> realHand = realModel.getCurrentPlayerHand();
        List<Card> adapted = new ArrayList<>();
        for (cs3500.pawnsboard.Model.Card c : realHand) {
            adapted.add(new ProviderCardAdapter(c));
        }
        return Collections.unmodifiableList(adapted);
    }

    @Override
    public int getRowScore(int row, PlayerColor color) {
        // convert from the provider's color -> your real color
        cs3500.pawnsboard.Model.PlayerColor realColor = ProviderColorAdapter.toRealColor(color);
        return realModel.getRowScore(row, realColor);
    }

    @Override
    public int getTotalScore(PlayerColor color) {
        cs3500.pawnsboard.Model.PlayerColor realColor = ProviderColorAdapter.toRealColor(color);
        return realModel.getTotalScore(realColor);
    }

    @Override
    public boolean canPlaceCard(int handIndex, int row, int col) {
        return realModel.canPlaceCard(handIndex, row, col);
    }

    @Override
    public int getDeckSize(PlayerColor color) {
        cs3500.pawnsboard.Model.PlayerColor realColor = ProviderColorAdapter.toRealColor(color);
        return realModel.getDeckSize(realColor);
    }
}


