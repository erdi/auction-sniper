package auctionsniper;

import auctionsniper.gui.MainWindow;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;

public class Main {

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "Auction";

    private MainWindow ui;
    private Chat chat;

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    static void main(String[] args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.joinAuction(connection, args[ARG_ITEM_ID]);
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format("%s@%s/", itemId, connection.getServiceName());
    }

    public void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
        String auctionId = auctionId(itemId, connection);
        chat = connection.getChatManager().createChat(auctionId, (Chat messageChat, Message message) -> {
            SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_LOST));
        });
        chat.sendMessage(new Message());
    }
}
