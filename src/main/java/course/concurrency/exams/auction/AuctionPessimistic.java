package course.concurrency.exams.auction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionPessimistic implements Auction {

    private final Notifier notifier;

    private final Object lock = new Object();

    private static final int THREADS = Runtime.getRuntime().availableProcessors();

    private final ExecutorService executors = Executors.newFixedThreadPool(THREADS);

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
    }

    private volatile Bid latestBid;

    public synchronized boolean propose(Bid bid) {
        if (bid.getPrice() > latestBid.getPrice()) {
            CompletableFuture.runAsync(() -> notifier.sendOutdatedMessage(latestBid), executors);
            latestBid = bid;
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
