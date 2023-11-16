package course.concurrency.m3_shared.collections;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RestaurantService {

    private final Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private final Map<String, AtomicInteger> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        new SimpleDateFormat();
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
//        stat.computeIfAbsent(restaurantName, s -> new AtomicInteger()).getAndIncrement();
//        stat.merge(restaurantName, 1, Integer::sum);
    }

    public Set<String> printStat() {
        // your code
        return stat.entrySet().stream()
                .map(stringAtomicIntegerEntry -> stringAtomicIntegerEntry.getKey() + " - " + stringAtomicIntegerEntry.getValue().intValue())
                .collect(Collectors.toSet());
    }
}