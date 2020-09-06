package Common;

import java.util.Collection;

public class Common {
    public static final Integer mine = -1;

    public static void addNeighbours(Collection<Integer> container, int pos, int width, int height) {
        int column = pos % width;
        if (pos >= width) {
            container.add(pos - width);
            if (column > 0) {
                container.add(pos - width - 1);
                container.add(pos - 1);
            }
            if (column + 1 != width) {
                container.add(pos - width + 1);
                container.add(pos + 1);
            }
        }
        if (pos + width < height * width) {
            container.add(pos + width);
            if (column > 0) {
                container.add(pos + width - 1);
                if (pos < width) //otherwise added earlier
                    container.add(pos - 1);
            }
            if (column + 1 != width) {
                container.add(pos + width + 1);
                if (pos < width) //otherwise added earlier
                    container.add(pos + 1);
            }
        }
    }
}
