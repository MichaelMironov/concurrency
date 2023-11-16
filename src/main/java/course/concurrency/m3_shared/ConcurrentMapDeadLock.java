package course.concurrency.m3_shared;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapDeadLock {

    public static void main(String[] args) {

        int key = 1;


        ConcurrentHashMap<Object, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(1, 1);
        concurrentHashMap.computeIfPresent(key, (o, integer) -> integer + 1);
        concurrentHashMap.computeIfPresent(key, (o, integer) -> integer + 1);

        System.out.println(concurrentHashMap);

    }
}
