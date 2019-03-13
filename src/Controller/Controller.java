package Controller;

import Model.Board;
import View.BoardView;
import View.EndWindow;
import View.Node;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayDeque;

import Solver.Solver;
import Solver.Communicator;

public class Controller {
    @FXML private TextField minesField;
    @FXML private TextField heightField;
    @FXML private TextField widthField;
    @FXML private Button newGameButton;
    private Stage gameLost = new EndWindow("You Lose");
    private Stage gameWon = new EndWindow("You Win");
    private BoardView boardView;
    private Board board;
    private int width,height,mines;

    private Solver bot;
    private Communicator botTalker;
    private ArrayDeque<Integer> answer;

    @FXML
    private void exit(){
        Platform.exit();
    }

    @FXML
    private void newGame() throws Exception{
        getParams();
        ((Stage)newGameButton.getScene().getWindow()).close();
        generateGame().show();
    }

    private void autoSolve(){
        bot.start();
    }

    private BoardView generateGame() throws Exception{
        Node[] buttons=new Node[height*width];

        for(int pos=0;pos<height*width;++pos){
                Node tmp=new Node(pos);
                tmp.setOnAction((event)->show(tmp.getPos()));
                buttons[pos]=tmp;
        }

        ArrayDeque<Integer> feedback=new ArrayDeque<>();
        botTalker=new Communicator(feedback);
        bot=new Solver(width,height,mines,buttons,feedback,botTalker);

        board = new Board(width,height,mines);
        int[] rawValues=board.getValues();

        Button exitButton=new Button("Exit");
        exitButton.setOnAction((event)->exit());

        Button solveButton=new Button("Solve");
        solveButton.setOnAction((event)->autoSolve());

        Button replayButton=new Button("Replay");


        boardView=new BoardView(buttons,rawValues,exitButton,replayButton,solveButton,width,height);

        replayButton.setOnAction((event -> {
            gameWon.close();
            gameLost.close();
            boardView.close();
            try {
                this.newGame();
            } catch (Exception ignored) {}
        }));

        return boardView;
    }

    private void getParams(){
        try{
            mines = Integer.parseInt(minesField.getText());
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
        }catch(Exception e){
            width=height=10;
            mines=21;
        }
    }

    private void show(int pos){
        answer=boardView.reveal(pos);
        int numOfRevealed=answer.pop();

        if(board.checkFail(pos)) {
            finish(gameLost);
        }else if(board.checkSuccess(numOfRevealed)){
            finish(gameWon);
        }

        botTalker.send(answer);
    }

    private void finish(Stage endStage){
        boardView.disableAll();
        endStage.show();
        answer.clear();
        answer.add(-1);
    }
}