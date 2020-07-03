package gui.sessieKalenders;

import domein.DomeinController;
import domein.SessieKalender;
import exceptions.OngeldigeVerwijderingException;
import gui.dialogs.ExceptionAlert;
import gui.dialogs.VerwijderKalenderDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class KalenderCell extends ListCell<SessieKalender> {

    @FXML
    private Label academiejaarLb;
    @FXML
    private HBox hBox;

    private DomeinController dc;

    public KalenderCell(DomeinController dc) {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("kalendercell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(SessieKalender kalender, boolean empty) {
        super.updateItem(kalender, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            academiejaarLb.setText(kalender.geefAcademiejaar());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
