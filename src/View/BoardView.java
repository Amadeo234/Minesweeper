package View;

import Common.Common;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Deque;
import java.util.LinkedList;

public class BoardView extends Stage {
    private GridPane gameBoard;
    private String[] values;
    private int[] rawValues;
    private final int width, height;


    public BoardView(Button[] buttons, int[] rawValues, Button exitButton, Button replayButton, Button solveButton,
                     int width, int height) throws Exception {
        super();

        this.width = width;
        this.height = height;
        this.rawValues = rawValues;
        values = new String[height * width];
        processValues();

        int buttonSideSize = 25;

        gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);
        for (int pos = 0; pos < height * width; ++pos) {
            buttons[pos].setMaxSize(buttonSideSize, buttonSideSize);
            buttons[pos].setMinSize(buttonSideSize, buttonSideSize);
            gameBoard.add(buttons[pos], pos % width, pos / width);
        }

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 15, 10, 15));
        root.setSpacing(20);
        root.getChildren().add(gameBoard);

        HBox menuButtons = new HBox();
        Region spaceFiller1 = new Region();
        Region spaceFiller2 = new Region();
        HBox.setHgrow(spaceFiller1, Priority.ALWAYS);
        HBox.setHgrow(spaceFiller2, Priority.ALWAYS);
        menuButtons.getChildren().addAll(replayButton, spaceFiller1, solveButton, spaceFiller2, exitButton);
        menuButtons.setMaxWidth(25 * width);
        root.getChildren().add(menuButtons);

        this.setTitle("Minesweeper");
        this.setScene(new Scene(root));
    }

    private void processValues() {
        for (int pos = 0; pos < height * width; ++pos) {
            if (rawValues[pos] == Common.mine)
                values[pos] = "*";
            else
                values[pos] = Integer.toString(rawValues[pos]);
        }
    }

    public Deque<Integer> reveal(int pos) {
        int numOfRevealed = 0;
        Button tmpButton;
        Deque<Integer> answer = new LinkedList<>();
        Deque<Integer> queue = new LinkedList<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            int id = queue.pop();
            tmpButton = (Button) gameBoard.getChildren().get(id);
            if (!tmpButton.isDisable()) {
                tmpButton.setText(values[id]);
                tmpButton.setDisable(true);
                if (values[id].equals("0"))
                    Common.addNeighbours(queue, id, width, height);
                answer.add(id);
                answer.add(rawValues[id]);
                ++numOfRevealed;
            }
        }
        answer.addFirst(numOfRevealed);
        return answer;
    }

    public void disableAll() {
        for (int pos = 0; pos < height * width; ++pos) {
            gameBoard.getChildren().get(pos).setDisable(true);
        }
    }

    public void showBombs() {
        for (int pos = 0; pos < height * width; ++pos) {
            if(values[pos].equals("*"))
                ((Button) gameBoard.getChildren().get(pos)).setText("*");
        }
    }
}
