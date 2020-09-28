package Solver;

import java.util.Deque;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;

public class Communicator {
    private Deque<Integer> info;
    private CountDownLatch latch;

    public Communicator(@NotNull Deque<Integer> info) {
        this.info = info;
    }

    void giveLatch(@NotNull CountDownLatch latch) {
        this.latch = latch;
    }

    public void send(@NotNull Deque<Integer> newInfo) {
        new Thread(() -> {
            if (newInfo.peekFirst() == -1)
                info.clear();
            info.addAll(newInfo);
            latch.countDown();
        }).start();
    }
}
