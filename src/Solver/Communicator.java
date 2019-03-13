package Solver;

import java.util.ArrayDeque;
import java.util.concurrent.CountDownLatch;

public class Communicator {
    private ArrayDeque<Integer> info;
    private CountDownLatch latch;
    public Communicator(ArrayDeque<Integer> info){
        this.info=info;
    }

    public void giveLatch(CountDownLatch latch){
        this.latch=latch;
    }

    public void send(ArrayDeque<Integer> newInfo){
        new Thread(() -> {
            if(newInfo.peekFirst()==-1)
                info.clear();
            info.addAll(newInfo);
            latch.countDown();
        }).start();
    }
}
