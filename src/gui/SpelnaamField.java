package gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import util.Validatie;

public class SpelnaamField extends TextField {

    public SpelnaamField(OpslaanDialog od) {
        getStyleClass().add("field-fout");
        textProperty().addListener((options, oldValue, newValue) -> {
            if (!controleerInput()) {
                getStyleClass().clear();
                getStyleClass().addAll("text-field", "text-input");
                getStyleClass().add("field-fout");
            } else {
                getStyleClass().clear();
                getStyleClass().addAll("text-field", "text-input");
            }
            od.controleerButton();
        });
    }

    public boolean controleerInput() {
        return Validatie.controleerSpelnaam(getText());
    }
}
