package auctionsniper;

import auctionsniper.gui.MainWindow;
import auctionsniper.injection.SniperModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;

public class Main {

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Event: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Event: BID; price: %s;";

    public static final String AUCTION_RESOURCE = "Auction";

    private final AuctionMessageTranslator translator;
    private final Auction auction;
    private final Chat chat;

    static void main(String[] args) throws Exception {
        SniperModule module = new SniperModule(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD], args[ARG_ITEM_ID], AUCTION_RESOURCE);
        Guice.createInjector(module).getInstance(Main.class);
    }

    @Inject
    public Main(MainWindow ui, Chat chat, Auction auction, AuctionMessageTranslator translator, ConnectionFinalizingListener connectionFinalizer) throws Exception {
        this.chat = chat;
        this.auction = auction;
        this.translator = translator;

        ui.addWindowListener(connectionFinalizer);

        joinAuction();
    }


    public void joinAuction() throws XMPPException {
        chat.addMessageListener(translator);
        auction.join();
    }
}
