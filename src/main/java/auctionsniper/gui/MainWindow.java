package auctionsniper.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String NAME = "Main window";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";

    private static final String TITLE = "Auction Sniper";

    private final JLabel statusLabel = createStatusLabel();

    public MainWindow() throws HeadlessException {
        super(TITLE);
        setName(NAME);
        add(statusLabel);
        showStatus(STATUS_JOINING);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createStatusLabel() {
        JLabel result = new JLabel();
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status) {
        statusLabel.setText(status);
        pack();
    }
}
