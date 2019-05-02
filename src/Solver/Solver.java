package Solver;

import Common.Common;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.*;
import java.util.concurrent.CountDownLatch;


public class Solver extends Thread {
    private ArrayList<Integer> emptyTiles;
    private Node[] board;
    private final int width, height;
    private Random rng;
    private static final Integer unknownTile = -2;
    private static final Integer mine = -1;
    private ArrayDeque<Integer> feedback;
    private BorderList border;
    private Communicator botTalker;

    public Solver(int width, int height, int mines, Button[] tiles, ArrayDeque<Integer> feedback, Communicator botTalker) {
        this.width = width;
        this.height = height;
        this.feedback = feedback;
        this.botTalker = botTalker;
        rng = new Random();
        board = new Node[width * height];
        for (int i = height * width - 1; i >= 0; --i)
            board[i] = new Node(unknownTile, tiles[i]);
        emptyTiles = new ArrayList<>(width * height);
        for (int i = 0; i < width * height; ++i)
            emptyTiles.add(i, i);
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

            if (feedback.peekFirst().equals(-1))
                return;
            while (!feedback.isEmpty()) {
                int id = feedback.pop();
                int value = feedback.pop();
                setValue(value, id);
            }

            //Slow down to see clicks (obsolete)
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
        int tmp = emptyTiles.indexOf(id);
        emptyTiles.set(tmp, emptyTiles.get(emptyTiles.size() - 1));
        emptyTiles.remove(emptyTiles.size() - 1);

        if (value > 0)
            border.add(id);
        valueNeighbours.clear();
        Common.addNeighbours(valueNeighbours, id, width, height);
        for (Integer neighbour : valueNeighbours) {
            if (value > 0)
                border.add(neighbour);
        }
    }

    private int randomShot() {
        return emptyTiles.get(rng.nextInt(emptyTiles.size()));
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
                else if (board[neighbour].getValue() == mine)
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
                        setValue(mine, neighbour);
                    }
                }
            }

            border.pop();
        }
        return -1;
    }
}
