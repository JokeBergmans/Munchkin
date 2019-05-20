package gui;

import domein.DomeinController;
import domein.Geslacht;
import exceptions.FouteSpelerGegevensException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import taal.Taal;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NieuwSpelSchermController extends BorderPane implements Initializable {

    private DomeinController dc;
    private MenuSchermController msc;

    @FXML
    private Label lblTitel;
    @FXML
    private Label lblAantal;
    @FXML
    private ComboBox cbAantal;
    @FXML
    private VBox vBoxSpelers;
    @FXML
    private Label lblInfo;
    @FXML
    private Button btnAnnuleer;
    @FXML
    private Button btnBevestig;


    public NieuwSpelSchermController(DomeinController dc, MenuSchermController msc) {
        this.dc = dc;
        this.msc = msc;
        FXMLLoader f = new FXMLLoader(getClass().getResource("NieuwSpelScherm.fxml"));
        f.setRoot(this);
        f.setController(this);
        try {
            f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ESCAPE){
                annuleren();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblTitel.setText(Taal.geefVertaling("nieuw"));
        lblAantal.setText(Taal.geefVertaling("aantalSpelers") + ":");
        btnBevestig.setText("OK");
        btnAnnuleer.setText(Taal.geefVertaling("annuleren"));
        btnBevestig.setDisable(true);
        cbAantal.getItems().addAll(3, 4, 5, 6);
        cbAantal.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            maakSpelerBoxes((int) newValue);
            lblInfo.setText(Taal.geefVertaling("foutNaam"));
        });
    }

    private void maakSpelerBoxes(int aantal) {
        vBoxSpelers.getChildren().clear();
        for (int i = 0; i < aantal; i++) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Label speler = new Label(Taal.geefVertaling("speler") + " " + (i + 1) + ":");
            speler.setStyle("-fx-font-size: 14px");
            hBox.getChildren().addAll(speler, new NaamField(this), new GeslachtComboBox());
            vBoxSpelers.getChildren().add(hBox);
        }
    }

    public void controleerButton() {
        boolean disabled = false;
        for (Node n : vBoxSpelers.getChildren()) {
            NaamField field = (NaamField) ((HBox) n).getChildren().get(1);
            if (!field.controleerInput()) {
                disabled = true;
            }
        }
        btnBevestig.setDisable(disabled || controleerUniekeNamen());
        if (!btnBevestig.isDisabled()) {
            lblInfo.setText("");
        } else {
            lblInfo.setText(Taal.geefVertaling("foutNaam"));
        }
    }

    private boolean controleerUniekeNamen() {
        List<String> namen = new ArrayList<>();
        for (Node n : vBoxSpelers.getChildren()) {
            NaamField field = (NaamField) ((HBox) n).getChildren().get(1);
            namen.add(field.getText());
        }
        boolean dubbels = false;
        for (int i = 0; i < namen.size(); i++) {
            for (int j = i + 1; j < namen.size(); j++) {
                if (j != i && namen.get(i).equals(namen.get(j))) {
                    dubbels = true;
                }
            }
        }
        return dubbels;
    }

    @FXML
    public void annuleren() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(msc.getScene());
        stage.centerOnScreen();
    }

    @FXML
    public void bevestigen() {
        dc.maakSpel((int) cbAantal.getValue());
        for (Node n : vBoxSpelers.getChildren()) {
            HBox hBox = (HBox) n;
            String naam = ((NaamField) hBox.getChildren().get(1)).getText();
            Geslacht geslacht = (Geslacht) ((GeslachtComboBox) hBox.getChildren().get(2)).getValue();
            try {
                dc.maakSpeler(naam, geslacht.toString().toLowerCase().charAt(0));
            } catch (FouteSpelerGegevensException fsge) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(Taal.geefVertaling("ongeldig"));
                alert.setContentText(Taal.geefVertaling("fouteNaam"));
                alert.showAndWait();
            }
        }
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new SpelSchermController(dc, msc)));
        stage.centerOnScreen();
    }
}
