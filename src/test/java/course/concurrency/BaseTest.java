package course.concurrency;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class BaseTest {
    private volatile List<String> servePathSpecs = new ArrayList<String>();

    @Test
    void test() {

        for (String pathSpec : servePathSpecs) {
            pathSpec.toLowerCase();
        }
    }

    @Test
    void test1() {
        String str1 = "TopJava";
        String str2 = "Java";
        String str3 = "Top" + str2;
        Thread thread = new Thread(() -> System.out.println(str1));

        System.out.println("Строка 1 равна строке 3? " + (str1 == str3));
    }
}
