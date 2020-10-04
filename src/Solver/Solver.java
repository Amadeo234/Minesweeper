package Solver;

import Common.Common;

import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;

public class Solver extends Thread {
    private static final Integer unknownTile = -2;
    private final int width, height;
    private List<Integer> unknownTiles;
    private Node[] board;
    private Communicator botTalker;
    private Deque<Integer> feedback;
    private Random rng;
    private BorderList border;

    public Solver(int width, int height,@NotNull Button[] tiles,@NotNull Deque<Integer> feedback,@NotNull Communicator botTalker) {
        this.width = width;
        this.height = height;
        this.feedback = feedback;
        this.botTalker = botTalker;
        rng = new Random();
        board = new Node[width * height];
        for (int i = height * width - 1; i >= 0; --i)
            board[i] = new Node(unknownTile, tiles[i]);
        unknownTiles = new ArrayList<>(width * height);
        for (int i = 0; i < width * height; ++i)
            unknownTiles.add(i);
        border = new BorderList(board);
    }

    public void run() {
        CountDownLatch latch;
        int tmp;
        while (true) {
            latch = new CountDownLatch(1);
            botTalker.giveLatch(latch);

            int target;

            tmp = analyzedShot();
            if (tmp == -1) {
                target = randomShot();
            } else {
                target = tmp;
            }

            Platform.runLater(() -> board[target].fire());
            while (true) {
                try {
                    latch.await();
                    break;
                } catch (InterruptedException ignored) {
                }
            }

            if (feedback.peekFirst() == -1)
                return;
            while (!feedback.isEmpty()) {
                int id = feedback.pop();
                int value = feedback.pop();
                setValue(value, id);
            }

            //Slow down to see clicks (optional)
            /*
            try {
                sleep(1000);
            } catch (InterruptedException ignored) {}
            */
        }
    }

    private ArrayList<Integer> valueNeighbours = new ArrayList<>(8);

    private void setValue(int value, int id) {
        board[id].setValue(value);
        int tmp = unknownTiles.indexOf(id);
        unknownTiles.set(tmp, unknownTiles.get(unknownTiles.size() - 1));
        unknownTiles.remove(unknownTiles.size() - 1);

        if (value > 0)
            border.add(id);
        valueNeighbours.clear();
        Common.addNeighbours(valueNeighbours, id, width, height);
        for (Integer neighbour : valueNeighbours) {
            if (neighbour > 0)
                border.add(neighbour);
        }
    }

    private int randomShot() {
        return unknownTiles.get(rng.nextInt(unknownTiles.size()));
    }

    private ArrayList<Integer> neighbours = new ArrayList<>(8);

    private int analyzedShot() {
        int pos, mines, hidden;
        while (!border.isEmpty()) {
            pos = border.peekFirst();
            neighbours.clear();
            Common.addNeighbours(neighbours, pos, width, height);
            mines = hidden = 0;
            for (Integer neighbour : neighbours) {
                if (board[neighbour].getValue() == unknownTile)
                    hidden++;
                else if (board[neighbour].getValue() == Common.mine)
                    mines++;
            }


            if (board[pos].getValue() - mines == 0) {
                for (Integer neighbour : neighbours) {
                    if (board[neighbour].getValue() == unknownTile) {
                        return neighbour;
                    }
                }
            }

            if (board[pos].getValue() - mines == hidden) {
                for (Integer neighbour : neighbours) {
                    if (board[neighbour].getValue() == unknownTile) {
                        setValue(Common.mine, neighbour);
                    }
                }
            }

            border.pop();
        }
        return -1;
    }
}
