package gui;

import domein.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import taal.Taal;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SpelSchermController extends ScrollPane implements Initializable {
    private DomeinController dc;
    private MenuSchermController msc;

    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hBoxHandKaarten;
    @FXML
    private HBox hBoxGedragenKaarten;
    @FXML
    private VBox vBoxInfo;
    @FXML
    private Label lblGedragen;
    @FXML
    private Label lblHand;
    @FXML
    private Button btnSpeelBeurt;
    @FXML
    private Button btnOpslaan;
    @FXML
    private Button btnStoppen;

    public SpelSchermController(DomeinController dc, MenuSchermController msc) {
        this.dc = dc;
        this.msc = msc;
        FXMLLoader f = new FXMLLoader(getClass().getResource("SpelScherm.fxml"));
        f.setRoot(this);
        f.setController(this);
        try {
            f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ESCAPE) {
                stopSpel();
            }
        });
        getStyleClass().add("spel");
        setHbarPolicy(ScrollBarPolicy.NEVER);
        borderPane.getBottom().getStyleClass().add("bottom");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblGedragen.setText(Taal.geefVertaling("gedragenKaarten"));
        lblHand.setText(Taal.geefVertaling("handKaarten"));
        btnSpeelBeurt.setText(Taal.geefVertaling("speelBeurt"));
        btnOpslaan.setText(Taal.geefVertaling("opslaan"));
        btnStoppen.setText(Taal.geefVertaling("stopSpel"));
        toonOverzicht(dc.geefOverzichtSpelers());
        vulSpelerboxes();
    }

    @FXML
    public void stopSpel() {
        if (!dc.isOpgeslagen()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Taal.geefVertaling("waarschuwing"));
            alert.setHeaderText(null);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert");
            alert.setContentText(Taal.geefVertaling("waarschuwingStoppen"));
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
            ButtonType nee = new ButtonType(Taal.geefVertaling("nee"));
            ButtonType ja = new ButtonType(Taal.geefVertaling("ja"));
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ja, nee);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ja) {
                opslaan();
            }
        }
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(msc.getScene());
        stage.centerOnScreen();
    }

    @FXML
    public void opslaan() {
        Dialog dialog = new OpslaanDialog(dc);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            dc.zetSpelnaam(result.get());
            dc.slaSpelOp();
            Dialog opgeslagen = new Dialog();
            opgeslagen.setTitle(null);
            opgeslagen.setHeaderText(null);
            opgeslagen.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            opgeslagen.getDialogPane().getStyleClass().add("alert");
            HBox content = new HBox(new Label(Taal.geefVertaling("opgeslagen")));
            content.setAlignment(Pos.CENTER);
            content.setPrefWidth(200);
            opgeslagen.getDialogPane().setContent(content);
            ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            opgeslagen.getDialogPane().getButtonTypes().add(ok);
            stage = (Stage) opgeslagen.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
            opgeslagen.showAndWait();
        }

    }

    @FXML
    public void speelBeurt() {
        toonBovensteKaart();
        if (dc.isMonsterKaart()) {
            //TODO: UC4 voorbereiden gevecht
        } else if (dc.heeftDirectEffect()) {
            dc.voerEffectUit();
            pasInfoAan();
            toonResultaat();
        } else {
            dc.voegKaartBijHand();
            pasKaartenAan();
        }
        Optional<Boolean> keuze = toonBeheerVraag();
        while((keuze.isPresent() && keuze.get()) || dc.teveelKaarten()) {
            //TODO: UC7 beheer kaarten
            keuze = toonBeheerVraag();
        }
        dc.beeindigBeurt();
        vulSpelerboxes();
    }

    private void toonBovensteKaart() {
        Dialog dialog = new BovensteKaartDialog(dc.geefAfbeeldingBovensteKaart());
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
        dialog.showAndWait();
    }

    private void toonResultaat() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(geefLabels(dc.geefResultaat()));
        Dialog dialog = new ResultaatDialog(vBox);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
        dialog.showAndWait();
    }

    private void toonOverzicht(String[][] overzicht) {
        Dialog dialog = new OverzichtDialog(overzicht);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
        dialog.showAndWait();
    }

    private Optional<Boolean> toonBeheerVraag() {
        Dialog beheren = new Dialog();
        beheren.setTitle(null);
        beheren.setHeaderText(null);
        beheren.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        beheren.getDialogPane().getStyleClass().add("alert");
        HBox content = new HBox(new Label(Taal.geefVertaling("verderBeheren")));
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(350);
        beheren.getDialogPane().setContent(content);
        ButtonType ja = new ButtonType(Taal.geefVertaling("ja"), ButtonBar.ButtonData.OK_DONE);
        ButtonType nee = new ButtonType(Taal.geefVertaling("nee"), ButtonBar.ButtonData.CANCEL_CLOSE);
        beheren.getDialogPane().getButtonTypes().addAll(ja, nee);
        beheren.getDialogPane().lookupButton(nee).setDisable(dc.teveelKaarten());
        beheren.setResultConverter((Callback) param -> {
            ButtonType type = (ButtonType) param;
            return type.getButtonData() == ButtonBar.ButtonData.OK_DONE;
        });
        return beheren.showAndWait();
    }

    private void vulSpelerboxes() {
        maakSpelerboxesLeeg();
        String[][] spelers = dc.geefSpelers();
        List<VBox> spelerBoxes = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < spelers.length; i++) {
            String[] s = spelers[i];
            if (s[0].equals(dc.geefHuidigeSpeler())) {
                index = i;
                vBoxInfo.getChildren().addAll(geefLabels(s));
                zetAfbeeldingen(dc.geefAfbeeldingenHandKaarten(), hBoxHandKaarten, 190);
                String[] gedragenKaarten = dc.geefAfbeeldingenGedragenKaarten(s[0]);
                zetAfbeeldingen(gedragenKaarten, hBoxGedragenKaarten, 150);
            }
        }
        String[][] deel1 = Arrays.copyOfRange(spelers,index + 1, spelers.length);
        String[][] deel2 = Arrays.copyOfRange(spelers, 0, index);
        voegSpelerBoxesToe(deel1, spelerBoxes);
        voegSpelerBoxesToe(deel2, spelerBoxes);

        ((VBox) borderPane.getLeft()).getChildren().add(spelerBoxes.get(spelerBoxes.size() - 1));
        ((VBox) spelerBoxes.get(0).getChildren().get(0)).setAlignment(Pos.CENTER_RIGHT);
        ((HBox) spelerBoxes.get(0).getChildren().get(1)).setAlignment(Pos.CENTER_RIGHT);
        ((VBox) borderPane.getRight()).getChildren().add(spelerBoxes.get(0));
        switch (spelerBoxes.size()) {
            case 3:
                ((HBox) borderPane.getTop()).getChildren().add(spelerBoxes.get(1));
                break;
            case 4:
                ((HBox) borderPane.getTop()).getChildren().addAll(spelerBoxes.get(2), spelerBoxes.get(1));
                break;
            case 5:
                ((HBox) borderPane.getTop()).getChildren().addAll(spelerBoxes.get(3), spelerBoxes.get(2), spelerBoxes.get(1));
                break;
        }
    }

    private void voegSpelerBoxesToe(String[][] spelers, List<VBox> spelerBoxes) {
        for (int i = 0; i < spelers.length; i++) {
            VBox vBox = maakSpelerBox(spelers[i]);
            vBox.setSpacing(10);
            spelerBoxes.add(vBox);
        }
    }

    private void maakSpelerboxesLeeg() {
        ((VBox) borderPane.getLeft()).getChildren().clear();
        ((HBox) borderPane.getTop()).getChildren().clear();
        ((VBox) borderPane.getRight()).getChildren().clear();
        vBoxInfo.getChildren().clear();
        hBoxGedragenKaarten.getChildren().clear();
        hBoxHandKaarten.getChildren().clear();
    }

    private void zetAfbeeldingen(String[] kaarten, HBox hBox, int height) {
        for (String k : kaarten) {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            imageView.setImage(new Image(k));
            hBox.getChildren().add(imageView);
        }
    }

    private VBox maakSpelerBox(String[] speler) {
        VBox infoBox = new VBox();
        HBox kaartBox = new HBox();
        infoBox.getChildren().addAll(geefLabels(speler));
        String[] kaarten = dc.geefAfbeeldingenGedragenKaarten(speler[0]);
        kaartBox.setSpacing(10);
        zetAfbeeldingen(kaarten, kaartBox, 100);
        VBox vBox = new VBox(infoBox, kaartBox);
        vBox.getStyleClass().add("spelerBox");
        return vBox;
    }

    private List<Label> geefLabels(String[] speler) {
        List<Label> labels = new ArrayList<>();
        labels.add(new Label(Taal.geefVertaling("naam") + ": " + speler[0]));
        labels.add(new Label(Taal.geefVertaling("geslacht") + ": " + Taal.geefVertaling(speler[1].toLowerCase())));
        labels.add(new Label(Taal.geefVertaling("niveau") + ": " + speler[2]));
        labels.add(new Label(Taal.geefVertaling("ras") + ": " + Taal.geefVertaling(speler[3].toLowerCase())));
        return labels;
    }

    private void pasKaartenAan() {
        hBoxHandKaarten.getChildren().clear();
        zetAfbeeldingen(dc.geefAfbeeldingenHandKaarten(), hBoxHandKaarten, 150);
    }

    private void pasInfoAan() {
        vBoxInfo.getChildren().clear();
        vBoxInfo.getChildren().addAll(geefLabels(dc.geefResultaat()));
    }
}
