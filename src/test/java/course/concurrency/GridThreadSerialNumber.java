package course.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.ThreadLocal.*;

public class GridThreadSerialNumber {
    private final AtomicInteger nextSerialNum = new AtomicInteger(-1);

    private final ThreadLocal<Integer> serialNum =
            withInitial(nextSerialNum::incrementAndGet);

    public int get() {
        return serialNum.get();
    }
}
