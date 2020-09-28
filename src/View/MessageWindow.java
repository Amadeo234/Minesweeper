package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessageWindow extends Stage {
    public MessageWindow(String msg) {
        Text endMessage = new Text(msg);
        Button closeButton = new Button("Close");
        closeButton.setOnAction((event) -> this.close());

        VBox root = new VBox();
        root.setPadding(new Insets(15, 0, 10, 0));
        root.setSpacing(15);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(endMessage);
        root.getChildren().add(closeButton);

        this.setTitle(msg.toUpperCase());
        this.setScene(new Scene(root, 125, 100));
    }
}
