package auctionsniper.fixtures

import auctionsniper.gui.MainWindow
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JLabelDriver
import com.objogate.wl.swing.gesture.GesturePerformer

import static org.hamcrest.CoreMatchers.equalTo

class AuctionSniperDriver extends JFrameDriver {

    AuctionSniperDriver() {
        super(
                new GesturePerformer(),
                topLevelFrame(named(MainWindow.NAME), showingOnScreen()),
                new AWTEventQueueProber(1000, 100)
        )
    }

    void showsSniperStatus(String status) {
        new JLabelDriver(this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(status))
    }
}
