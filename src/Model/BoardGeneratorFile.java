package Model;

import Common.Common;
import View.MessageWindow;

import javafx.stage.Stage;

import java.util.Scanner;

import org.jetbrains.annotations.NotNull;

public class BoardGeneratorFile implements IBoardGenerator {
    private Stage wrongInput = new MessageWindow("Wrong map encoding!");
    private Scanner mapScanner;

    BoardGeneratorFile(@NotNull Scanner scanner) {
        mapScanner = scanner;
    }

    @NotNull
    public int[] generate(int width, int height, int mines) {
        int[] board = new int[width * height];

        for (int i = 0; i < mines; ++i) {
            try {
                board[mapScanner.nextInt()] = Common.mine;
            } catch (RuntimeException e) {
                wrongInput.show();
            }
        }

        fillUp(board, width, height);
        return board;
    }
}
