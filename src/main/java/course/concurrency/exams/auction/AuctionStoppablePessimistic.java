package course.concurrency.exams.auction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;

    private final Object lock = new Object();

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private static final int THREADS = Runtime.getRuntime().availableProcessors();

    private final ExecutorService executors = Executors.newFixedThreadPool(THREADS);

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);

    public boolean propose(Bid bid) {

        if (executors.isShutdown()) return false;

        if (bid.getPrice() > latestBid.getPrice()) {
            CompletableFuture.runAsync(() -> notifier.sendOutdatedMessage(latestBid), executors);
            synchronized (lock) {
                latestBid = bid;
            }
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        executors.shutdown();
        return latestBid;
    }
}
