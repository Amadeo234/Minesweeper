package View;

import Common.Common;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayDeque;

public class BoardView extends Stage {
    private String[] values;
    private int[] rawValues;
    private GridPane gameBoard;
    private int width,height;
    private static final Integer mine = -1;
    public BoardView(Button[] buttons,int[] rawValues,Button exitButton,Button replayButton,Button solveButton, int width, int height)throws Exception {
        super();
        this.width=width;
        this.height=height;
        this.rawValues=rawValues;
        values=new String[height*width];
        processValues(rawValues);
        gameBoard=new GridPane();
        gameBoard.setAlignment(Pos.CENTER);
        for(int pos=0;pos<height*width;++pos){
                buttons[pos].setMaxSize(25,25);
                buttons[pos].setMinSize(25,25);
                gameBoard.add(buttons[pos],pos%width,pos/width);
        }
        VBox root=new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10,15,10,15));
        root.setSpacing(20);
        root.getChildren().add(gameBoard);
        HBox menuButtons = new HBox();
        Region spaceFiller1 = new Region();
        Region spaceFiller2 = new Region();
        HBox.setHgrow(spaceFiller1, Priority.ALWAYS);
        HBox.setHgrow(spaceFiller2, Priority.ALWAYS);
        menuButtons.getChildren().addAll(replayButton,spaceFiller1,solveButton,spaceFiller2,exitButton);
        menuButtons.setMaxWidth(25*width);
        root.getChildren().add(menuButtons);
        this.setTitle("Minesweeper");
        this.setScene(new Scene(root));
    }

    private void processValues(int[] rawValues){
        for(int pos=0;pos<height*width;++pos){
            if(rawValues[pos]==mine)
                values[pos]="*";
            else
                values[pos]= Integer.toString(rawValues[pos]);
        }
    }

    public ArrayDeque<Integer> reveal(int pos){
        int numOfRevealed=0;
        Button tmp;
        ArrayDeque<Integer> answer=new ArrayDeque<>();
        ArrayDeque<Integer> queue=new ArrayDeque<>();
        queue.add(pos);
        while(!queue.isEmpty()){
            int top=queue.pop();
            tmp = (Button) gameBoard.getChildren().get(top);
            if(!tmp.isDisable()) {
                tmp.setText(values[top]);
                if (values[top].equals("0"))
                    Common.getNeighbours(top , width, height, queue);
                tmp.setDisable(true);
                answer.add(top);
                answer.add(rawValues[top]);
                ++numOfRevealed;
            }
        }
        answer.addFirst(numOfRevealed);
        return answer;
    }
    
    public void disableAll(){
        for(int pos=0;pos<height*width;++pos){
            /*
            if(values[row][column].equals("*"))
                ((Button) gameBoard.getChildren().get(width*row+column)).setText("*");
            */
            gameBoard.getChildren().get(pos).setDisable(true);
        }
    }
}
