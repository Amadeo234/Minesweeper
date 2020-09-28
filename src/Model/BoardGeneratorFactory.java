package Model;

import java.util.Scanner;

import org.jetbrains.annotations.NotNull;

public class BoardGeneratorFactory {
    public static Scanner mapScanner;

    @NotNull
    public static IBoardGenerator MakeBoardGenerator(BoardGeneratorType boardGenType) {
        switch (boardGenType) {
            case File:
                return new BoardGeneratorFile(mapScanner);
            case Simple:
            default:
                return new BoardGeneratorSimple();
        }
    }

}
