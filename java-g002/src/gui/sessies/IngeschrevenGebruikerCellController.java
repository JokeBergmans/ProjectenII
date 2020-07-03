package gui.sessies;

import domein.DomeinController;
import domein.GebruikerAanwezigheidStatus;
import domein.Inschrijving;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class IngeschrevenGebruikerCellController extends ListCell<Inschrijving> {

    @FXML
    private Label naamLbl;

    @FXML
    private Label statusLbl;
    @FXML
    private Label datumLbl;

    private DomeinController dc;

    @FXML
    private HBox hBox;

    public IngeschrevenGebruikerCellController(DomeinController dc) {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ingeschrevengebruikercell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Inschrijving inschrijving, boolean empty) {
        super.updateItem(inschrijving, empty);
        hBox.getChildren().removeAll(naamLbl, datumLbl, statusLbl);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            naamLbl.setText(inschrijving.getGebruiker().getNaam());
            datumLbl.setText(inschrijving.getDatum().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            statusLbl.setText(inschrijving.getStatus().toString());
            hBox.getChildren().addAll(naamLbl, datumLbl, statusLbl);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
