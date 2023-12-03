package course.concurrency.exams.auction;

public class AuctionPessimistic implements Auction {

    private final Notifier notifier;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
    }

    private volatile Bid latestBid;

    public boolean propose(Bid bid) {

        if (bid.getPrice() < latestBid.getPrice())
            return false;

        notifier.sendOutdatedMessage(latestBid);
        latestBid = bid;
        return true;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
