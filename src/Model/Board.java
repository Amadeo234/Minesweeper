package Model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import Common.Common;

public class Board{
    private int[] board;
    private int width,height,mines;
    private int remainingTiles;
    private static final Integer mine = -1;
    public Board(int width,int height,int mines){
        board=new int[height*width];
        this.width=width;
        this.height=height;
        this.mines=mines;
        this.remainingTiles=width*height-mines;
        for(int i=0;i<height*width;++i)
            board[i]=0;
        placeBombs();
        fillUp();
    }

    public int[] getValues(){
        return board;
    }

    public boolean checkFail(int pos){
        return board[pos]==mine;
    }

    public boolean checkSuccess(int numOfRevealed){
        remainingTiles-=numOfRevealed;
        return remainingTiles==0;
    }

    private void placeBombs(){
        Random rng = new Random();
        ArrayList<Integer> freePositions=new ArrayList<>(height*width);
        for(int i=0;i<height*width;++i)
            freePositions.add(i);

        for(int i=0;i<mines;++i){
            int tmp= rng.nextInt(freePositions.size());
            board[freePositions.get(tmp)]=mine;
            freePositions.set(tmp,freePositions.get(freePositions.size()-1));
            freePositions.remove(freePositions.size()-1);
        }
    }


    private void fillUp(){
        ArrayDeque<Integer> queue=new ArrayDeque<>(8);
        for(int pos=0;pos<height*width;++pos){
                if(board[pos]==-1){
                    Common.getNeighbours(pos,width,height,queue);
                    while(!queue.isEmpty()) {
                        int tmp = queue.pop();
                        if (board[tmp] != mine)
                            ++board[tmp];
                    }
            }
        }
    }
}
