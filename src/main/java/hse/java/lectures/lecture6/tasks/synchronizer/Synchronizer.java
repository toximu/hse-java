package hse.java.lectures.lecture6.tasks.synchronizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Synchronizer {

    public static final int DEFAULT_TICKS_PER_WRITER = 10;
    private List<StreamWriter> tasks = List.of();
    private final int ticksPerWriter;

    public Synchronizer(List<StreamWriter> tasks) {
        this(tasks, DEFAULT_TICKS_PER_WRITER);
    }

    public Synchronizer(List<StreamWriter> tasks, int ticksPerWriter) {
        this.tasks = tasks;
        this.ticksPerWriter = ticksPerWriter;
         monitor = new StreamingMonitor(tasks.stream()
                                     .map(StreamWriter::getId).toList(), ticksPerWriter);
    }

    /**
     * Starts infinite writer threads and waits until each writer prints exactly ticksPerWriter ticks
     * in strict ascending id order.
     */
    private final StreamingMonitor monitor;
    public void execute() throws InterruptedException {
        // add monitor and sync
        for (StreamWriter writer : tasks) {
            writer.attachMonitor(monitor);
            Thread worker = new Thread(writer, "stream-writer-" + writer.getId());
            worker.setDaemon(true);
            worker.start();
        }

        synchronized (monitor) {
            while (monitor.isWorking) {
                monitor.wait();
            }
        }
    }

}
