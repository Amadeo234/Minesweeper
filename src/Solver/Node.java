package Solver;

import javafx.scene.control.Button;

public class Node {
    private boolean inQueue;
    private int value;
    private Button tile;
    Node(int value, Button tile){
        this.value=value;
        this.tile=tile;
        inQueue=false;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value=value;
    }

    public boolean getInQueue(){
        return inQueue;
    }

    public void fire(){
        tile.fire();
    }
}
