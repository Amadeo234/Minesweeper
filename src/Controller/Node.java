package Controller;

import javafx.scene.control.Button;

class Node extends Button {
    private final int pos;

    Node(int pos) {
        super();
        this.setFocusTraversable(false);
        this.pos = pos;
    }

    int getPos() {
        return pos;
    }

}
