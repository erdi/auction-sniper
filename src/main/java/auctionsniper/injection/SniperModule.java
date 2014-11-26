package auctionsniper.injection;

import auctionsniper.*;
import auctionsniper.gui.MainWindow;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SniperModule extends AbstractModule {
    private final String hostname;
    private final String username;
    private final String password;
    private final String resource;
    private final String itemId;

    private MainWindow ui;

    public SniperModule(String hostname, String username, String password, String itemId, String resource) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.itemId = itemId;
        this.resource = resource;
    }

    @Override
    protected void configure() {
        bind(SniperListener.class).to(SniperStateDisplayer.class);
        bind(AuctionEventListener.class).to(Sniper.class);
        bind(Auction.class).to(XmppAuction.class);
    }

    @Provides
    @Singleton
    public XMPPConnection openConnection() throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, resource);

        return connection;
    }

    @Provides
    @Singleton
    public Chat startChat(XMPPConnection connection) {
        String auctionId = String.format("%s@%s/", itemId, connection.getServiceName());
        return connection.getChatManager().createChat(auctionId, null);
    }

    @Provides
    @Singleton
    public MainWindow provideMainWindow() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
        return ui;
    }
}
