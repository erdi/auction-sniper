package auctionsniper.fixtures

import auctionsniper.Main
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

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

    void hasReceivedJoinRequestFromSniper() {
        messageListener.receivesMessage()
    }

    void announceClosed() {
        auctionChat.sendMessage(new Message())
    }

    void close() {
        connection.disconnect()
    }

    private static class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1)

        void processMessage(Chat chat, Message message) {
            messages.add(message)
        }

        void receivesMessage() {
            assert messages.poll(5, TimeUnit.SECONDS) != null
        }
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
}
