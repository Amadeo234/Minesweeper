package Model;

import Common.Common;

public class Board {
    private int[] board;
    private int remainingTiles;

    public Board(int width, int height, int mines) {
        board = new int[height * width];
        this.remainingTiles = width * height - mines;
        IBoardGenerator boardGenerator = new BoardGeneratorSimple();
        boardGenerator.generate(board, width, height, mines);
    }

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
