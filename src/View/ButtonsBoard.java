package View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import org.jetbrains.annotations.NotNull;

class ButtonsBoard extends GridPane {
    private final int width, height;

    ButtonsBoard(int boardWidth, int boardHeight) {
        setAlignment(Pos.CENTER);
        this.width = boardWidth;
        this.height = boardHeight;
    }

    void setButtons(@NotNull Button[] buttons)
    {
        if (buttons.length != width * height)
            throw new RuntimeException("Wrong size of board buttons!");

        getChildren().clear();
        for (int pos = 0; pos < width * height; ++pos) {
            buttons[pos].setMaxSize(BoardView.buttonSize, BoardView.buttonSize);
            buttons[pos].setMinSize(BoardView.buttonSize, BoardView.buttonSize);
            add(buttons[pos], pos % width, pos / width);
        }
    }
}
