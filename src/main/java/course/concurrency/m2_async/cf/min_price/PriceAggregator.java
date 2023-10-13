package course.concurrency.m2_async.cf.min_price;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

        Set<Double> prices;
        try {
            prices = CompletableFuture.supplyAsync(() ->
                    shopIds.stream()
                            .map((id) -> priceRetriever.getPrice(itemId, id)).collect(Collectors.toSet())
            ).get(2_900L, TimeUnit.MILLISECONDS);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            return Double.NaN;
        }

//        shopIds.forEach(shop -> CompletableFuture
//                .supplyAsync(() -> priceRetriever.getPrice(shop, itemId))
//                .thenAccept(prices::add)
//                .exceptionally(ex -> {
//                    log.warn(ex, ex::getMessage);
//                    return null;
//                })
//                .join());

        return prices.stream().min(Double::compareTo).orElseThrow(RuntimeException::new);

    }
}
