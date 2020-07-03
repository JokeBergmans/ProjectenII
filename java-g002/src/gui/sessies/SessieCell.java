package gui.sessies;

import domein.DomeinController;
import domein.Sessie;
import domein.SessieType;
import exceptions.OngeldigeVerwijderingException;
import gui.dialogs.ExceptionAlert;
import gui.dialogs.VerwijderSessieDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

public class SessieCell extends ListCell<Sessie> {

    @FXML
    private Label titelLabel;
    @FXML
    private HBox titelHbox;
    @FXML
    private VBox contentVbox;
    @FXML
    private Label verantwoordelijkeLabel;
    @FXML
    private Label startDatumLabel;
    @FXML
    private Label startUurLabel;
    @FXML
    private Label eindDatumLabel;
    @FXML
    private Label eindUurLabel;
    private HBox openHbox;
    private HBox geslotenHbox;

    private Button deleteBtn;

    private DomeinController dc;

    public SessieCell(DomeinController dc) {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessiecell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        openHbox = new HBox();
        openHbox.getChildren().add(new Label("Open plaatsen: "));
        geslotenHbox = new HBox();
        geslotenHbox.getChildren().add(new Label("Aanwezigen: "));
        deleteBtn = new Button("X");

    }

    @Override
    protected void updateItem(Sessie sessie, boolean empty) {
        super.updateItem(sessie, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            if (sessie.getStatus() == SessieType.AANGEMAAKT || sessie.getStatus() == SessieType.AFGELOPEN) {
                if (!titelHbox.getChildren().contains(deleteBtn))
                    titelHbox.getChildren().add(deleteBtn);
                deleteBtn.setOnAction(actionEvent -> {
                    try {
                        verwijder(sessie);
                    } catch (OngeldigeVerwijderingException e) {
                        new ExceptionAlert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                    }
                });
            } else {
                titelHbox.getChildren().remove(deleteBtn);
            }
            titelLabel.setText(sessie.getTitel());
            verantwoordelijkeLabel.setText(sessie.getNaamVerantwoordelijke());
            startDatumLabel.setText(sessie.getStartDatum().toString());
            String startmin = sessie.getStartMin().toString().length() == 1 ? "0" + sessie.getStartMin().toString() : sessie.getStartMin().toString();
            startUurLabel.setText(sessie.getStartUur() + ":" + startmin);
            String eindmin = sessie.getEindMin().toString().length() == 1 ? "0" + sessie.getEindMin().toString() : sessie.getEindMin().toString();
            eindDatumLabel.setText(sessie.getEindDatum().toString());
            eindUurLabel.setText(sessie.getEindUur() + ":" + eindmin);
            contentVbox.getChildren().removeAll(geslotenHbox, openHbox);
            if (sessie.getStatus() == SessieType.OPEN) {
                if (openHbox.getChildren().size() > 1)
                    openHbox.getChildren().remove(1);
                openHbox.getChildren().add(new Label(Integer.toString(sessie.getOpenPlaatsen())));
                contentVbox.getChildren().add(openHbox);
            } else {
                if (geslotenHbox.getChildren().size() > 1)
                    geslotenHbox.getChildren().remove(1);
                geslotenHbox.getChildren().add(new Label(Integer.toString(sessie.getAantalAanwezigen())));
                contentVbox.getChildren().add(geslotenHbox);
            }
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void verwijder(Sessie sessie) throws OngeldigeVerwijderingException {
        Dialog dialog = new VerwijderSessieDialog(sessie);
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            dc.verwijderSessie(sessie);
            dc.setGeselecteerdeSessie(null);
        }
    }
}
