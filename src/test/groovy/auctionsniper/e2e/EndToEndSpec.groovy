package auctionsniper.e2e

import auctionsniper.fixtures.ApplicationRunner
import auctionsniper.fixtures.FakeAuctionServer
import auctionsniper.fixtures.XMPPBroker
import org.junit.Rule
import spock.lang.AutoCleanup
import spock.lang.Specification

import static ApplicationRunner.SNIPER_ID

class EndToEndSpec extends Specification {

    @AutoCleanup
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321")

    @AutoCleanup
    private final ApplicationRunner application = new ApplicationRunner()

    @Rule
    final XMPPBroker broker = new XMPPBroker([SNIPER_ID, auction.username])

    void "sniper joins auction until auction closes"() {
        when:
        auction.startSellingItem()
        application.startBiddingIn(auction)

        then:
        auction.hasReceivedJoinRequestFromSniper(SNIPER_ID)

        when:
        auction.announceClosed()

        then:
        application.showsSniperHasLostAuction()
    }

    void "sniper makes a higher bid but looses"() {
        when:
        auction.startSellingItem()
        application.startBiddingIn(auction)

        then:
        auction.hasReceivedJoinRequestFromSniper(SNIPER_ID)

        when:
        auction.reportPrice(1000, 98, "other bidder")

        then:
        application.hasShownSniperIsBidding()
        auction.hasReceivedBid(1098, SNIPER_ID)

        when:
        auction.announceClosed()

        then:
        application.showsSniperHasLostAuction()
    }
}
