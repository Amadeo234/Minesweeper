package Solver;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;

public class BorderList extends LinkedList<Integer> {
    private Node[] board;

    BorderList(@NotNull Node[] board) {
        this.board = board;
    }

    public boolean add(Integer i) {
        if (i == null)
            return false;
        if (board[i].isInQueue())
            return false;
        if (super.add(i)) {
            board[i].setInQueue(true);
            return true;
        }
        return false;
    }

    @NotNull
    public Integer pop() throws NoSuchElementException{
        int id = super.pop();
        board[id].setInQueue(false);
        return id;
    }
}
