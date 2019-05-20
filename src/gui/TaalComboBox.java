package gui;

import javafx.scene.control.ComboBox;
import taal.Taal;

import java.util.Locale;
import java.util.ResourceBundle;

public class TaalComboBox extends ComboBox {

    public TaalComboBox(MenuSchermController msc) {
        getItems().addAll("Nederlands", "English", "Français");
        int index;
        Locale locale = Taal.getTaal();
        if (locale == null) {
            Taal.setBundle(ResourceBundle.getBundle("resources.ResourceBundle", new Locale("nl", "NL")));
            index = 0;
        } else if (locale.getLanguage().equals("nl")) {
            index = getItems().indexOf("Nederlands");
        } else if (locale.getLanguage().equals("en")) {
            index = getItems().indexOf("English");
        } else {
            index = getItems().indexOf("Français");
        }
        getSelectionModel().select(index);

        getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String geselecteerd = (String) newValue;
            ResourceBundle taal;
            if (geselecteerd.equals("Nederlands")) {
                taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("nl", "NL"));
            } else if (geselecteerd.equals("English")) {
                taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("en", "GB"));
            } else {
                taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("fr", "FR"));
            }
            Taal.setBundle(taal);
            msc.vertaal();
        });
    }
}
