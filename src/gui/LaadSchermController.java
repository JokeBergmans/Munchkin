package gui;

import domein.DomeinController;
import domein.Spel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import taal.Taal;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LaadSchermController extends BorderPane implements Initializable {

    private DomeinController dc;
    private MenuSchermController msc;

    @FXML
    private Label lblTitel;
    @FXML
    private ListView lvSpellen;
    @FXML
    private Button btnAnnuleer;
    @FXML
    private Button btnBevestig;

    public LaadSchermController(DomeinController dc, MenuSchermController msc) {
        this.dc = dc;
        this.msc = msc;
        FXMLLoader f = new FXMLLoader(getClass().getResource("LaadScherm.fxml"));
        f.setRoot(this);
        f.setController(this);
        try {
            f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lvSpellen.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ESCAPE) {
                annuleren();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblTitel.setText(Taal.geefVertaling("laad"));
        btnBevestig.setText("OK");
        btnAnnuleer.setText(Taal.geefVertaling("annuleren"));
        btnBevestig.disableProperty().bind(lvSpellen.getSelectionModel().selectedItemProperty().isNull());
        lvSpellen.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toonSpellen();
    }

    private void toonSpellen() {
        String[][] spellen = dc.geefSpellen();
        for (String[] s : spellen) {
            lvSpellen.getItems().add(s[0] + " - " + Taal.geefVertaling("speler").toLowerCase() + "s: " + s[1]);
        }
    }

    @FXML
    public void annuleren() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(msc.getScene());
        stage.centerOnScreen();
    }

    @FXML
    public void bevestigen() {
        dc.laadSpel(dc.geefSpellen()[((int) lvSpellen.getSelectionModel().getSelectedIndices().get(0))][0]);
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new SpelSchermController(dc, msc)));
        stage.centerOnScreen();
    }

}
