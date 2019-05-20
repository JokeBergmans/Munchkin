package gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import util.Validatie;

public class NaamField extends TextField {


    public NaamField(NieuwSpelSchermController nssc) {
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
            nssc.controleerButton();
        });
    }

    public boolean controleerInput() {
        return Validatie.controleerSpelernaam(getText());
    }
}
