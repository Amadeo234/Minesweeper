package Model;

import Common.Common;

import java.util.ArrayDeque;
import java.util.Deque;

public interface IBoardGenerator {
    default void fillUp(int[] board, int width, int height) {
        Deque<Integer> queue = new ArrayDeque<>(8);
        for (int pos = 0; pos < height * width; ++pos) {
            if (board[pos] == Common.mine) {
                Common.addNeighbours(queue, pos, width, height);
                while (!queue.isEmpty()) {
                    int tmp = queue.pop();
                    if (board[tmp] != Common.mine)
                        ++board[tmp];
                }
            }
        }
    }

    void generate(int[] board, int width, int height, int mines);
}
