package View;


import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

class StatisticsBar extends HBox {
    StatisticsBar(int boardWidth, @NotNull Text gamesWonText, @NotNull Text gamesLostText, @NotNull Text winRateText) {
        Region spaceFiller1 = new Region();
        Region spaceFiller2 = new Region();
        setHgrow(spaceFiller1, Priority.ALWAYS);
        setHgrow(spaceFiller2, Priority.ALWAYS);
        setMaxWidth(BoardView.buttonSize * boardWidth);
        getChildren().addAll(new Text("Won:"), gamesWonText, spaceFiller1,
                new Text("Win rate:"), winRateText, new Text("%"), spaceFiller2,
                new Text("Lost:"), gamesLostText);
    }
}
