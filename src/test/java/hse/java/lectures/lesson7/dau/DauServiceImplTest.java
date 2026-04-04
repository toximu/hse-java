package hse.java.lectures.lesson7.dau;

import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DauServiceImplTest {

    @Test
    void JustWorks() {
        DauServiceImpl service = new DauServiceImpl();
        service.postEvent(new Event(0,0));
        service.postEvent(new Event(0,1));
        service.postEvent(new Event(0,2));
        service.postEvent(new Event(1,0));
        service.goToNextDay();
        assertEquals(2, service.getAuthorDauStatistics(0));
        assertEquals(1, service.getAuthorDauStatistics(1));

    }

    @Test
    void ManyThreads() throws InterruptedException {

        ArrayList<Thread> threads = new ArrayList<>();
        DauServiceImpl service = new DauServiceImpl();
        for (int i = 0; i < 10; i++) {
            int ii = i;
            var thread = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    service.postEvent(new Event(ii,j));
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (var thr : threads) {
            thr.join();
        }
        service.goToNextDay();
        for (int i = 0; i < 10000; i++) {
            assertEquals(10, service.getAuthorDauStatistics(i));
        }

    }
}