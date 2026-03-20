package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StreamingMonitor {
    int shouldPrintNow = 0;
    List<Integer> ids;
    boolean isWorking = true;
    int ticksDone = 0;
    int tickNeeded = 0;
    StreamingMonitor(List<Integer> ids, int tickNeeded) {
        this.ids = new ArrayList<>(ids);
        this.ids.sort(Comparator.naturalOrder());
        this.tickNeeded = tickNeeded;
    }

    Integer nowWriter() {
        return ids.get(shouldPrintNow);
    }

    void next() {
        shouldPrintNow++;
        if (shouldPrintNow == ids.size()) {
            shouldPrintNow = 0;
            ticksDone++;
        }

        if (ticksDone == tickNeeded) {
            isWorking = false;
        }
    }
}
