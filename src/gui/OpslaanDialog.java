package gui;

import domein.DomeinController;
import domein.Spel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import taal.Taal;

public class OpslaanDialog extends Dialog {

    private SpelnaamField naamField;
    private Node opslaanButton;
    private DomeinController dc;

    public OpslaanDialog(DomeinController dc) {
        this.dc = dc;
        setTitle(Taal.geefVertaling("opslaan"));
        getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        getDialogPane().getStyleClass().add("dialog");
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        naamField = new SpelnaamField(this);
        Label info = new Label(Taal.geefVertaling("foutSpelnaam"));
        info.setText(info.getText().replaceAll(", ", ",\n"));
        info.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().addAll(new Label(Taal.geefVertaling("naamSpel")), naamField, info);
        getDialogPane().setContent(vBox);
        ButtonType bevestig = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(bevestig);
        opslaanButton = getDialogPane().lookupButton(bevestig);
        opslaanButton.setDisable(true);
        setResultConverter(dialogButton -> {
            if (dialogButton == bevestig) {
                return naamField.getText();
            }
            return null;
        });
    }

    public void controleerButton() {
        boolean disabled = false;
        for (String[] s : dc.geefSpellen()) {
            if (naamField.getText().equals(s[0])) {
                disabled = true;
            }
        }
        opslaanButton.setDisable(disabled || !naamField.controleerInput());
    }

}

