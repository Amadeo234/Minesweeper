package Model;

import Common.Common;

import org.jetbrains.annotations.NotNull;

public class Board {
    private int[] board;
    private int remainingTiles;

    public Board(int width, int height, int mines,@NotNull IBoardGenerator boardGenerator) {
        board = boardGenerator.generate(width, height, mines);
        this.remainingTiles = width * height - mines;
    }

    @NotNull
    public int[] getValues() {
        return board;
    }

    public boolean checkFail(int pos) {
        return board[pos] == Common.mine;
    }

    public boolean checkSuccess(int numOfRevealed) {
        remainingTiles -= numOfRevealed;
        return remainingTiles == 0;
    }
}
