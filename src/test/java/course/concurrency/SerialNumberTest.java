package course.concurrency;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SerialNumberTest {


    @Test
    void test() {

        GridThreadSerialNumber serialNumber = new GridThreadSerialNumber();
        GridThreadSerialNumber serialNumber1 = new GridThreadSerialNumber();

        Executor executor = Executors.newSingleThreadExecutor();

        int next = new Random().nextInt(199);
        new SimpleDateFormat();


        Runnable task = () -> System.out.println(serialNumber.get());
        for (int i = 0; i < 5; i++) {

            executor.execute(task);
        }

        System.out.println(serialNumber.get());
        System.out.println(serialNumber.get());
        System.out.println(serialNumber.get());
        System.out.println(serialNumber.get());

        new Thread(() -> System.out.println(serialNumber.get())).start();
//        new Thread(() -> System.out.println(serialNumber.get())).start();
//        new Thread(() -> System.out.println(serialNumber.get())).start();
//        new Thread(() -> System.out.println(serialNumber1.get())).start();
//        new Thread(() -> System.out.println(serialNumber1.get())).start();


    }


    @Test
    void test1() throws InterruptedException {
        Exchanger<Void> exchanger = new Exchanger<>();
        exchanger.exchange(null);
    }
}
