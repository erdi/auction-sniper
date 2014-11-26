package auctionsniper;

import auctionsniper.gui.MainWindow;
import auctionsniper.injection.SniperModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;

public class Main implements SniperListener {

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Event: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Event: BID; price: %s;";

    public static final String AUCTION_RESOURCE = "Auction";
    private final ConnectionFinalizingListener connectionFinalizer;
    private final AuctionMessageTranslator translator;

    private MainWindow ui;
    private final Chat chat;

    static void main(String[] args) throws Exception {
        SniperModule module = new SniperModule(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD], args[ARG_ITEM_ID], AUCTION_RESOURCE);
        Guice.createInjector(module).getInstance(Main.class);
    }

    @Inject
    public Main(Chat chat, AuctionMessageTranslator translator, ConnectionFinalizingListener connectionFinalizer) throws Exception {
        this.chat = chat;
        this.connectionFinalizer = connectionFinalizer;
        this.translator = translator;
        startUserInterface();
        joinAuction();
    }

    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
        ui.addWindowListener(connectionFinalizer);
    }

    public void joinAuction() throws XMPPException {
        chat.addMessageListener(translator);
        chat.sendMessage(JOIN_COMMAND_FORMAT);
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
