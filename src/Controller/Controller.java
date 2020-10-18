package Controller;

import Model.Board;
import Model.BoardGeneratorFactory;
import Model.BoardGeneratorType;
import Model.IBoardGenerator;
import Solver.Communicator;
import Solver.Solver;
import View.BoardView;
import View.MessageWindow;

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

    private Stage fileNotFound = new MessageWindow("File not found!");
    private Stage wrongFileEncoding = new MessageWindow("Wrong file encoding!");

    private BoardView boardView;
    private Board board;

    private int width;
    private int height;
    private int mines;

    private Button exitButton = new Button("Exit");
    private Button replayButton = new Button("Replay");
    private BoardGeneratorType boardGeneratorType;
    private File mapFile = null;

    private Button solveButton = new Button("Solve");
    private Communicator botTalker;
    private Deque<Integer> revealedInfo;


    @FXML
    private void newGame() {
        ((Stage) newGameButton.getScene().getWindow()).close();
        getParams();
        boardGeneratorType = BoardGeneratorType.Simple;
        generateGame(BoardGeneratorFactory.MakeBoardGenerator(BoardGeneratorType.Simple)).show();
    }

    @FXML
    private void customGame() {
        mapFile = openMapFile(customGameMap.getValue());
        if (mapFile == null) {
            fileNotFound.show();
            return;
        }
        boardGeneratorType = BoardGeneratorType.File;
        generateCustomGame();
    }

    @Nullable
    private File openMapFile(@Nullable String fileName) {
        try {
            URL fileURL = Thread.currentThread().getContextClassLoader().getResource("./Maps/" + fileName);
            if (fileURL != null) {
                return new File(fileURL.toURI());
            }
        } catch (URISyntaxException ignored) {
        }
        return null;
    }

    private void generateCustomGame() {
        ((Stage) customGameButton.getScene().getWindow()).close();
        prepareScanner();
        generateGame(BoardGeneratorFactory.MakeBoardGenerator(BoardGeneratorType.File)).show();
    }

    private void prepareScanner() {
        try {
            Scanner sc = new Scanner(mapFile);
            getParams(sc);
            BoardGeneratorFactory.mapScanner = sc;
        } catch (FileNotFoundException ignored) {
        }
    }

    private void autoSolve(@NotNull Node[] buttons) {
        solveButton.setDisable(true);
        Deque<Integer> feedback = new ArrayDeque<>();
        botTalker = new Communicator(feedback);
        new Solver(width, height, buttons, feedback, botTalker).start();
    }

    private void resetGame() {
        if (boardGeneratorType == BoardGeneratorType.File)
            prepareScanner();
        setBoard(new Board(width, height, mines, BoardGeneratorFactory.MakeBoardGenerator(boardGeneratorType)));

        boardView.disableBoard(false);
        solveButton.setDisable(false);
    }

    private BoardView generateGame(@NotNull IBoardGenerator boardGenerator) {
        exitButton.setOnAction(event -> exit());
        replayButton.setOnAction(event -> resetGame());
        boardView = new BoardView(exitButton, replayButton, solveButton, width, height);
        setBoard(new Board(width, height, mines, boardGenerator));
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
        revealedInfo = boardView.reveal(pos);
        int numOfRevealed = revealedInfo.pop();

        if (board.checkFail(pos))
            finish(false);
        else if (board.checkSuccess(numOfRevealed))
            finish(true);

        if (botTalker != null)
            botTalker.send(revealedInfo);
    }

    private void finish(boolean victory) {
        boardView.addResult(victory);
        boardView.disableBoard(true);
        boardView.showBombs();
        revealedInfo.clear();
        revealedInfo.add(-1);
    }

    @FXML
    private void exit() {
        Platform.exit();
    }

    private void setBoard(@NotNull Board boardModel) {
        board = boardModel;

        Node[] buttons = new Node[height * width];

        for (int pos = 0; pos < height * width; ++pos) {
            Node tmp = new Node(pos);
            tmp.setOnAction((event) -> show(tmp.getPos()));
            buttons[pos] = tmp;
        }

        boardView.setBoard(buttons, board.getValues());
        solveButton.setOnAction(event -> autoSolve(buttons));
    }
}
