package gui;

import domein.DomeinController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import taal.Taal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuSchermController extends BorderPane implements Initializable {

    private DomeinController dc;
    @FXML
    private Button btnNieuwSpel;
    @FXML
    private Button btnLaden;
    @FXML
    private Button btnStoppen;

    private TaalComboBox taalCB;

    public MenuSchermController() {
        this.dc = new DomeinController();
        taalCB = new TaalComboBox(this);
        FXMLLoader f = new FXMLLoader(getClass().getResource("MenuScherm.fxml"));
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
                stop();
            }
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       vertaal();
        setBottom(taalCB);
        setAlignment(taalCB, Pos.BOTTOM_RIGHT);
        setMargin(taalCB, new Insets(0,20,20,0));
    }

    public void vertaal() {
        btnNieuwSpel.setText(Taal.geefVertaling("nieuw"));
        btnLaden.setText(Taal.geefVertaling("laad"));
        btnStoppen.setText(Taal.geefVertaling("stop"));
    }

    @FXML
    public void startNieuwSpel() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new NieuwSpelSchermController(dc, this)));
        stage.centerOnScreen();
    }

    @FXML
    public void laadSpel() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new LaadSchermController(dc, this)));
        stage.centerOnScreen();
    }

    @FXML
    public void stop() {
        Platform.exit();
    }

}
