package View;

import javafx.scene.control.Button;

public class Node extends Button {
    private final int pos;
    public Node(int pos){
        super();
        this.setFocusTraversable(false);
        this.pos=pos;
    }

    public int getPos(){
        return pos;
    }
}
