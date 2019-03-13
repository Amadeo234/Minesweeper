package Solver;

import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class Solver extends Thread {
    private ArrayList<Integer> emptyTiles;
    private Node[] board;
    private final int width,height,mines;
    private Random rng;
    private static final Integer unknown = -2;
    private static final Integer bomb = -1;
    private ArrayDeque<Integer> feedback;
    private Communicator botTalker;
    private CountDownLatch latch;

    public Solver(int width, int height, int mines, Button[] tiles, ArrayDeque<Integer> feedback,Communicator botTalker){
        this.width=width;
        this.height=height;
        this.mines=mines;
        this.feedback=feedback;
        this.botTalker=botTalker;
        rng=new Random();
        board=new Node[width*height];
        for(int i=height*width-1;i>=0;--i)
            board[i]=new Node(unknown,tiles[i]);
        emptyTiles=new ArrayList<>(width*height);
        for(int i=0;i<width*height;++i)
            emptyTiles.add(i,i);
    }

    public void run(){
        while(true){
            latch=new CountDownLatch(1);
            botTalker.giveLatch(latch);

            //first take some random shots saving info until 'big shot' (reveals meaningful amount of tiles)
            //after 'big shot' focus on revealing border tiles
            //queue with border tiles
            //random from possible border not mine tiles

            int target;
            //TODO logic
            /*
            if(feedback.isEmpty()){
                target=emptyTiles.get(rng.nextInt(emptyTiles.size()));
            }
            else if(feedback.peekFirst().equals(-1)){
                return;
            }
            else{
                while(!feedback.isEmpty()){
                    int id=feedback.pop();
                    int value=feedback.pop();
                    board[id].setValue(value);
                    emptyTiles.remove(id);
                }
            }
            */

            Platform.runLater(() -> {
                board[emptyTiles.get(rng.nextInt(emptyTiles.size()))].fire();
            });

            while(true){
                try{
                    latch.await();
                    break;
                }catch(InterruptedException ignored){}
            }

            //Slow down to see clicks (unnecessary)
            try {
                sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
