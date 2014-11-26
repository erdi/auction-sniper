package auctionsniper;

import auctionsniper.gui.MainWindow;
import com.google.inject.Inject;

import javax.swing.*;

public class SniperStateDisplayer implements SniperListener {

    private final MainWindow ui;

    @Inject
    public SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    @Override
    public void sniperLost() {
        SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_LOST));
    }

    @Override
    public void sniperBidding() {
        SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_BIDDING));
    }
}
