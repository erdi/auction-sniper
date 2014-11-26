package auctionsniper;

import com.google.inject.Inject;
import org.jivesoftware.smack.XMPPConnection;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ConnectionFinalizingListener extends WindowAdapter {
    private final XMPPConnection connection;

    @Inject
    public ConnectionFinalizingListener(XMPPConnection connection) {
        this.connection = connection;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        connection.disconnect();
    }
}
