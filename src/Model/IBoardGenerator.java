package Model;

import Common.Common;

import java.util.ArrayDeque;
import java.util.Deque;

import org.jetbrains.annotations.NotNull;

public interface IBoardGenerator {
    default void fillUp(@NotNull int[] board, int width, int height) {
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

    @NotNull
    int[] generate(int width, int height, int mines);
}
