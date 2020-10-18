package View;

import Common.Common;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;

public class BoardView extends Stage {
    private ButtonsBoard gameBoard;
    private String[] values;
    private int[] rawValues;
    private final int width, height;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private Text gamesWonText = new Text("0");
    private Text gamesLostText = new Text("0");
    private Text winRateText = new Text("0,00");
    static final int buttonSize = 25;

    public BoardView(@NotNull Button exitButton, @NotNull Button replayButton,
                     @NotNull Button solveButton, int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.values = new String[height * width];

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 15, 10, 15));
        root.setSpacing(20);
        root.getChildren().add(new StatisticsBar(width, gamesWonText, gamesLostText, winRateText));
        root.getChildren().add(gameBoard = new ButtonsBoard(width, height));
        root.getChildren().add(new MenuBar(width, replayButton, solveButton, exitButton));
        this.setTitle("Minesweeper");
        this.setScene(new Scene(root));
    }

    public void setBoard(@NotNull Button[] buttons, @NotNull int[] rawValues) {
        gameBoard.setButtons(buttons);
        this.rawValues = rawValues;
        processValues();
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
        Deque<Integer> answer = new LinkedList<>();
        Deque<Integer> queue = new LinkedList<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            int id = queue.pop();
            Button tmpButton = (Button) gameBoard.getChildren().get(id);
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

    public void disableBoard(boolean disabled) {
        gameBoard.setDisable(disabled);
    }

    public void showBombs() {
        for (int pos = 0; pos < height * width; ++pos) {
            if (values[pos].equals("*"))
                ((Button) gameBoard.getChildren().get(pos)).setText("*");
        }
    }

    public void addResult(boolean victory) {
        if (victory) {
            ++gamesWon;
            gamesWonText.setText(Integer.toString(gamesWon));
        } else {
            ++gamesLost;
            gamesLostText.setText(Integer.toString(gamesLost));
        }
        winRateText.setText(String.format("%.2f", (float) gamesWon / (gamesWon + gamesLost)));
    }
}
