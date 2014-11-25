package auctionsniper

import spock.lang.Specification

class SniperTest extends Specification {

    def listener = Mock(SniperListener)
    def auction = Mock(Auction)
    def sniper = new Sniper(listener, auction)

    void "reports lost when auction closes"() {
        when:
        sniper.auctionClosed()

        then:
        (1.._) * listener.sniperLost()
        0 * _
    }

    void "bids higher and reports bidding when new price arrives"() {
        when:
        sniper.currentPrice(price, increment)

        then:
        1 * auction.bid(price + increment)
        (1.._) * listener.sniperBidding()
        0 * _

        where:
        price = 1001
        increment = 25
    }
}
