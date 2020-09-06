package Model;

import Common.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BoardGeneratorSimple implements IBoardGenerator {
    public void generate(int[] board, int width, int height, int mines) {
        Random rng = new Random();
        List<Integer> freePositions = new ArrayList<>(height * width);
        for (int i = 0; i < height * width; ++i)
            freePositions.add(i);

        for (int i = 0; i < mines; ++i) {
            int tmp = rng.nextInt(freePositions.size());
            board[freePositions.get(tmp)] = Common.mine;
            freePositions.set(tmp, freePositions.get(freePositions.size() - 1));
            freePositions.remove(freePositions.size() - 1);
        }

        fillUp(board, width, height);
    }
}
