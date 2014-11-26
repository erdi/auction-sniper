package auctionsniper;

import com.google.inject.Inject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;

public class XmppAuction implements Auction {
    private final Chat chat;

    @Inject
    public XmppAuction(Chat chat) {
        this.chat = chat;
    }

    public void bid(int bid) {
        sendMessage(format(Main.BID_COMMAND_FORMAT, bid));
    }

    @Override
    public void join() {
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
