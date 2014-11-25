package auctionsniper.fixtures

import auctionsniper.Main
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.junit.Assert

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

import static org.hamcrest.CoreMatchers.*

class FakeAuctionServer {

    public static final String XMPP_HOSTNAME = "localhost"

    final String itemId

    private final XMPPConnection connection
    private Chat auctionChat

    private final SingleMessageListener messageListener = new SingleMessageListener()

    FakeAuctionServer(String itemId) {
        this.itemId = itemId
        connection = new XMPPConnection(XMPP_HOSTNAME)
    }

    String getUsername() {
        itemId
    }

    private String getPassword() {
        username
    }

    void hasReceivedJoinRequestFromSniper(String sniperId) {
        messageListener.receivesMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT))
    }

    void announceClosed() {
        auctionChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;")
    }

    void reportPrice(int currentPrice, int priceIncrement, Object lastBidFrom) {
        auctionChat.sendMessage("SOLVersion: 1.1; Event: PRICE; CurrentPrice: $currentPrice; Increment: $priceIncrement; Bidder: $lastBidFrom;")
    }

    void hasReceivedBid(int bid, String sniperId) {
        def message = String.format(Main.BID_COMMAND_FORMAT, bid)
        messageListener.receivesMessageMatching(sniperId, equalTo(message))
    }

    void startSellingItem() {
        connection.with {
            connect()
            login(username, password, Main.AUCTION_RESOURCE)
            chatManager.addChatListener { Chat chat, boolean createdLocally ->
                auctionChat = chat
                chat.addMessageListener(messageListener)
            }
        }
    }

    private class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1)

        void processMessage(Chat chat, Message message) {
            messages.add(message)
        }

        void receivesMessageMatching(String sniperId, Matcher<? super String> messageMatcher) {
            def message = messages.poll(5, TimeUnit.SECONDS)
            Assert.assertThat("Message", message, Matchers.is(notNullValue()))
            Assert.assertThat(message.body, messageMatcher)
            Assert.assertThat(auctionChat.participant, Matchers.startsWith("$sniperId@"))
        }
    }

    void close() {
        connection.disconnect()
    }
}
