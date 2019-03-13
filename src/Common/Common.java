package Common;

import java.util.ArrayDeque;

public class Common {
    public static void getNeighbours(int pos,int width, int height, ArrayDeque<Integer> queue){
        int tmp = pos%width;
        if(pos>=width){
            queue.add(pos-width);
            if(tmp>0){
                queue.add(pos-width-1);
                queue.add(pos-1);
            }
            if(tmp+1<width){
                queue.add(pos-width+1);
                queue.add(pos+1);
            }
        }
        if(pos+width<height*width){
            queue.add(pos+width);
            if(tmp>0){
                queue.add(pos+width-1);
                if(pos<width)
                    queue.add(pos-1);
            }
            if(tmp+1<width){
                queue.add(pos+width+1);
                if(pos<width)
                    queue.add(pos+1);
            }
        }
    }

}
