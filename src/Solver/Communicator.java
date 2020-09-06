package Solver;

import java.util.Deque;
import java.util.concurrent.CountDownLatch;

public class Communicator {
    private Deque<Integer> info;
    private CountDownLatch latch;

    public Communicator(Deque<Integer> info) {
        this.info = info;
    }

    public void giveLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public void send(Deque<Integer> newInfo) {
        new Thread(() -> {
            if (newInfo.peekFirst() == -1)
                info.clear();
            info.addAll(newInfo);
            latch.countDown();
        }).start();
    }
}
