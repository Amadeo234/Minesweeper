package Solver;

import java.util.ArrayDeque;

public class BorderList extends ArrayDeque<Integer> {
    private Node[] board;
    BorderList(Node[] board){
        this.board=board;
    }
    public boolean add(Integer i){
        if(board[i].isInQueue())
            return false;
        if(super.add(i)){
            board[i].setInQueue(true);
            return true;
        }
        return false;
    }
    public Integer pop(){
        int id=super.pop();
        board[id].setInQueue(false);
        return id;
    }
}
