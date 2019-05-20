package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import taal.Taal;

public class ResultaatDialog extends Dialog {

    public ResultaatDialog(VBox info) {
        setTitle(Taal.geefVertaling("resultaat"));
        getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        getDialogPane().getStyleClass().add("dialog");
        info.getStyleClass().add("resultaat");
        info.setMinWidth(200);
        info.setSpacing(20);
        info.setAlignment(Pos.CENTER);
        info.setPadding(new Insets(20,10,20,10));
        getDialogPane().setContent(info);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}
