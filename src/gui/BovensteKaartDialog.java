package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import taal.Taal;

public class BovensteKaartDialog extends Dialog {

    public BovensteKaartDialog(String bovensteKaart) {
        setTitle(Taal.geefVertaling("bovensteKaart"));
        getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        getDialogPane().getStyleClass().add("dialog");
        ImageView image = new ImageView();
        image.setImage(new Image(bovensteKaart));
        image.setFitHeight(300);
        image.setPreserveRatio(true);
        VBox vBox = new VBox(image);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20,10,20,10));
        getDialogPane().setContent(vBox);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}
