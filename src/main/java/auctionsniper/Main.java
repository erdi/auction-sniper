package auctionsniper;

import auctionsniper.gui.MainWindow;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main implements SniperListener {

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Event: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Event: BID; price: %s;";

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
        disconnectWhenUiCloses(connection);
        String auctionId = auctionId(itemId, connection);
        chat = connection.getChatManager().createChat(auctionId, null);
        Sniper sniper = new Sniper(this, (int bid) -> {
            try {
                chat.sendMessage(String.format(BID_COMMAND_FORMAT, bid));
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return;
        });
        AuctionMessageTranslator translator = new AuctionMessageTranslator(sniper);
        chat.addMessageListener(translator);
        chat.sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void disconnectWhenUiCloses(XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
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
