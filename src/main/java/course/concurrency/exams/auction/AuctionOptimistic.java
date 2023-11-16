package course.concurrency.exams.auction;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuctionOptimistic implements Auction {

    private volatile Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final Executor executors = Executors.newCachedThreadPool();

    private Bid latestBid;

    public boolean propose(Bid bid) {
        if (bid.getPrice() > latestBid.getPrice()) {
            notifier.sendOutdatedMessage(latestBid);
            latestBid = bid;
            return true;
        }
        return false;
    }

    public synchronized Bid getLatestBid() {
        return latestBid;
    }
}
