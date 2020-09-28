package Controller;

import Model.Board;
import Model.BoardGeneratorFactory;
import Model.BoardGeneratorType;
import Model.IBoardGenerator;
import Solver.Communicator;
import Solver.Solver;
import View.BoardView;
import View.MessageWindow;
import View.Node;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Controller {
    @FXML
    private TextField minesField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField widthField;
    @FXML
    private Button newGameButton;
    @FXML
    private Button customGameButton;
    @FXML
    private ChoiceBox<String> customGameMap;
    private Thread replayGame;

    private Stage gameLost = new MessageWindow("You Lose!");
    private Stage gameWon = new MessageWindow("You Win!");
    private Stage fileNotFound = new MessageWindow("File not found!");
    private Stage wrongFileEncoding = new MessageWindow("Wrong file encoding!");

    private BoardView boardView;
    private Board board;

    private int width;
    private int height;
    private int mines;

    private Button exitButton = new Button("Exit");
    private Button replayButton = new Button("Replay");

    private Button solveButton = new Button("Solve");
    private Communicator botTalker;
    private Deque<Integer> revealedInfo;

    @FXML
    private void newGame() {
        replayGame = new Thread(this::newGame);
        getParams();
        ((Stage) newGameButton.getScene().getWindow()).close();
        generateGame(BoardGeneratorFactory.MakeBoardGenerator(BoardGeneratorType.Simple)).show();
    }

    @FXML
    private void customGame() {
        replayGame = new Thread(this::customGame);
        File mapFile = openMapFile(customGameMap.getValue());
        if (mapFile == null) {
            fileNotFound.show();
            return;
        }

        try {
            Scanner sc = new Scanner(mapFile);
            getParams(sc);
            BoardGeneratorFactory.mapScanner = sc;
        } catch (FileNotFoundException ignored) {
        }

        ((Stage) customGameButton.getScene().getWindow()).close();
        generateGame(BoardGeneratorFactory.MakeBoardGenerator(BoardGeneratorType.File)).show();
    }

    @Nullable
    private File openMapFile(@Nullable String fileName) {
        try {
            URL fileURL = Thread.currentThread().getContextClassLoader().getResource("./Maps/" + fileName);
            if (fileURL != null)
            {
                return new File(fileURL.toURI());
            }
        } catch (URISyntaxException ignored) {
        }
        return null;
    }

    private void autoSolve(@NotNull Node[] buttons) {
        Deque<Integer> feedback = new ArrayDeque<>();
        botTalker = new Communicator(feedback);
        new Solver(width, height, buttons, feedback, botTalker).start();
    }

    private void resetGame() {
        gameWon.close();
        gameLost.close();
        boardView.close();
        replayGame.run();
    }

    private BoardView generateGame(@NotNull IBoardGenerator boardGenerator) {
        board = new Board(width, height, mines, boardGenerator);
        int[] rawValues = board.getValues();

        Node[] buttons = new Node[height * width];

        for (int pos = 0; pos < height * width; ++pos) {
            Node tmp = new Node(pos);
            tmp.setOnAction((event) -> show(tmp.getPos()));
            buttons[pos] = tmp;
        }

        exitButton.setOnAction(event -> exit());
        replayButton.setOnAction(event -> resetGame());
        solveButton.setOnAction(event -> autoSolve(buttons));

        boardView = new BoardView(buttons, rawValues, exitButton, replayButton, solveButton, width, height);

        return boardView;
    }

    private void getParams() {
        try {
            mines = Integer.parseInt(minesField.getText());
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
        } catch (NumberFormatException ignore) {
            //TODO handle the exception
        }
    }

    private void getParams(@NotNull Scanner sc) {
        try {
            mines = sc.nextInt();
            width = sc.nextInt();
            height = sc.nextInt();
        } catch (RuntimeException e) {
            wrongFileEncoding.show();
        }
    }

    private void show(int pos) {
        if (!solveButton.isDisable())
            solveButton.setDisable(true);

        revealedInfo = boardView.reveal(pos);
        int numOfRevealed = revealedInfo.pop();

        if (board.checkFail(pos))
            finish(gameLost);
        else if (board.checkSuccess(numOfRevealed))
            finish(gameWon);

        if (botTalker != null)
            botTalker.send(revealedInfo);
    }

    private void finish(@NotNull Stage endStage) {
        boardView.disableAll();
        boardView.showBombs();
        revealedInfo.clear();
        revealedInfo.add(-1);
        endStage.show();
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}
