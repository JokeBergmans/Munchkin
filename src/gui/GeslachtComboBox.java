package gui;

import domein.Geslacht;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import taal.Taal;

public class GeslachtComboBox extends ComboBox {

    public GeslachtComboBox() {
        Callback<ListView<Geslacht>, ListCell<Geslacht>> cellFactory = new Callback<ListView<Geslacht>, ListCell<Geslacht>>() {
            @Override
            public ListCell call(ListView param) {
                return new ListCell<Geslacht>() {
                    @Override
                    protected void updateItem(Geslacht item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(Taal.geefVertaling(item.name().toLowerCase()));
                        }
                    }
                };
            }
        };
        setButtonCell(cellFactory.call(null));
        setCellFactory(cellFactory);
        getItems().addAll(Geslacht.MAN, Geslacht.VROUW);
        getSelectionModel().select(0);
    }
}
