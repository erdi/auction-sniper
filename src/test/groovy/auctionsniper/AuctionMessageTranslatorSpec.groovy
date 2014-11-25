package auctionsniper

import org.jivesoftware.smack.packet.Message
import spock.lang.Specification

class AuctionMessageTranslatorSpec extends Specification {

    static final UNUSED_CHAT = null

    def listener = Mock(AuctionEventListener)
    def translator = new AuctionMessageTranslator(listener)

    void "notifies auction closed when close message is received"() {
        when:
        translator.processMessage(UNUSED_CHAT, new Message(body: "SOLVersion: 1.1; Event: CLOSE;"));

        then:
        1 * listener.auctionClosed()
        0 * _
    }

    void "notifies bid details when current price message is received"() {
        when:
        translator.processMessage(UNUSED_CHAT, new Message(body: "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"));

        then:
        1 * listener.currentPrice(192, 7)
        0 * _
    }
}
