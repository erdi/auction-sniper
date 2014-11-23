package auctionsniper.fixtures

import auctionsniper.Main
import auctionsniper.gui.MainWindow

import static FakeAuctionServer.XMPP_HOSTNAME

class ApplicationRunner {

    public static final String SNIPER_ID = "sniper"
    public static final String SNIPER_PASSWORD = SNIPER_ID

    private AuctionSniperDriver driver

    void startBiddingIn(final FakeAuctionServer auction) {
        def startupAction = { Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId) }
        def applicationStartupThread = new Thread(startupAction, "Test application")
        applicationStartupThread.daemon = true
        applicationStartupThread.start()
        driver = new AuctionSniperDriver()
        driver.showsSniperStatus(MainWindow.STATUS_JOINING)
    }

    void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST)
    }

    void close() {
        driver?.dispose()
    }
}
