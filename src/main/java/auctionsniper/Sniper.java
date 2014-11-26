package auctionsniper;

import com.google.inject.Inject;

public class Sniper implements AuctionEventListener {

    private final SniperListener listener;
    private final Auction auction;

    @Inject
    public Sniper(SniperListener listener, Auction auction) {
        this.listener = listener;
        this.auction = auction;
    }

    @Override
    public void auctionClosed() {
        listener.sniperLost();
    }

    @Override
    public void currentPrice(int price, int increment) {
        auction.bid(price + increment);
        listener.sniperBidding();
    }
}
