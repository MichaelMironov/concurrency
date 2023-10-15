package course.concurrency.m2_async.cf.min_price;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceAggregator {

    private static final Logger log = LoggerFactory.getLogger(PriceAggregator.class);

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10L, 45L, 66L, 345L, 234L, 333L, 67L, 123L, 768L);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {

        final List<CompletableFuture<Double>> futures = shopIds.stream()
                .map(shopId -> sendRequest(shopId, itemId))
                .collect(Collectors.toList());

        final CompletableFuture<List<Double>> executedFutures = futures.stream().collect(Collectors.collectingAndThen(
                Collectors.toList(), completableFutures ->
                        CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new))
                                .thenApply(__ -> completableFutures)
                                .thenApply(list -> list.stream()
                                        .map(CompletableFuture::join)
                                        .collect(Collectors.toList())
                                )
        ));

        return executedFutures.join().stream()
                .filter(Objects::nonNull)
                .filter(aDouble -> aDouble != 0.0D)
                .min(Double::compareTo).orElse(Double.NaN);

    }

    private CompletableFuture<Double> sendRequest(final Long shopId, final Long itemId) {
        return CompletableFuture
                .supplyAsync(() -> priceRetriever.getPrice(itemId, shopId))
                .completeOnTimeout(Double.NaN, 2500L, TimeUnit.MILLISECONDS)
                .exceptionally(throwable -> null);

    }
}
