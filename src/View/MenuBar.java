package View;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import org.jetbrains.annotations.NotNull;

class MenuBar extends HBox {
    MenuBar(int boardWidth, @NotNull Button replayButton, @NotNull Button solveButton, @NotNull Button exitButton) {
        Region spaceFiller1 = new Region();
        Region spaceFiller2 = new Region();
        setHgrow(spaceFiller1, Priority.ALWAYS);
        setHgrow(spaceFiller2, Priority.ALWAYS);
        setMaxWidth(BoardView.buttonSize * boardWidth);
        getChildren().addAll(replayButton, spaceFiller1, solveButton, spaceFiller2, exitButton);
    }
}
