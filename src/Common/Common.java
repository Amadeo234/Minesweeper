package Common;

import java.util.Collection;

public class Common {
    public static void addNeighbours(Collection<Integer> container, int pos, int width, int height) {
        int tmp = pos % width;
        if (pos >= width) {
            container.add(pos - width);
            if (tmp > 0) {
                container.add(pos - width - 1);
                container.add(pos - 1);
            }
            if (tmp + 1 < width) {
                container.add(pos - width + 1);
                container.add(pos + 1);
            }
        }
        if (pos + width < height * width) {
            container.add(pos + width);
            if (tmp > 0) {
                container.add(pos + width - 1);
                if (pos < width)
                    container.add(pos - 1);
            }
            if (tmp + 1 < width) {
                container.add(pos + width + 1);
                if (pos < width)
                    container.add(pos + 1);
            }
        }
    }
}
