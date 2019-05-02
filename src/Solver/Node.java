package Solver;

import javafx.scene.control.Button;

class Node {
    private boolean inQueue;
    private int value;
    private Button tile;

    Node(int value, Button tile) {
        this.value = value;
        this.tile = tile;
        inQueue = false;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    boolean isInQueue() {
        return inQueue;
    }

    void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    void fire() {
        tile.fire();
    }
}
