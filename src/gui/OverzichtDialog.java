package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import taal.Taal;

public class OverzichtDialog extends Dialog {

    public OverzichtDialog(String[][] overzicht) {
        setTitle(Taal.geefVertaling("speler") + "s");
        getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        getDialogPane().getStyleClass().add("dialog");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getStyleClass().add("gridPane");
        Font font = Font.font("System", FontWeight.BOLD, 12);
        Label naam = new Label(Taal.geefVertaling("naam"));
        naam.setFont(font);
        Label geslacht = new Label(Taal.geefVertaling("geslacht"));
        geslacht.setFont(font);
        Label niveau = new Label(Taal.geefVertaling("niveau"));
        niveau.setFont(font);
        Label ras = new Label(Taal.geefVertaling("ras"));
        ras.setFont(font);
        grid.add(naam, 1, 0);
        grid.add(geslacht, 2, 0);
        grid.add(niveau, 3, 0);
        grid.add(ras, 4, 0);
        int i = 1;
        for (String[] s : overzicht) {
            grid.add(new Label(Taal.geefVertaling("speler") + " " + i), 0, i);
            grid.add(new Label(s[0]), 1, i);
            grid.add(new Label(Taal.geefVertaling(s[1].toLowerCase())), 2, i);
            grid.add(new Label(s[2]), 3, i);
            grid.add(new Label(Taal.geefVertaling(s[3].toLowerCase())), 4, i);
            i++;
        }
        grid.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(grid);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20,10,20,10));
        getDialogPane().setContent(vBox);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}
