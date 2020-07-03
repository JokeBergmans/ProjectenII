package gui.sessies;

import domein.DomeinController;
import domein.Sessie;
import domein.SessieType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;


public class SessieOverzichtController extends VBox {

    private DomeinController dc;
    @FXML
    private ComboBox<SessieType> typeCBox;
    @FXML
    private ListView<Sessie> sessieLv;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnSessieToevoegen;


    public SessieOverzichtController(DomeinController dc)  {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessieoverzicht.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sessieLv.prefHeightProperty().bind(this.prefHeightProperty());
        sessieLv.setCellFactory(lv -> new SessieCell(dc));
        sessieLv.setItems(dc.geefSessies(SessieType.AANGEMAAKT));
        sessieLv.getSelectionModel().selectedItemProperty().addListener((c, o, n) -> dc.setGeselecteerdeSessie(n));
        typeCBox.setItems(FXCollections.observableList(Arrays.asList(SessieType.values())));
        typeCBox.getSelectionModel().selectedItemProperty().addListener((c, o, n) -> sessieLv.setItems(dc.geefSessies(n)));
        typeCBox.getSelectionModel().selectedItemProperty().addListener((c, o, n) -> dc.getSessieStatusProperty().setValue(n));
        typeCBox.getSelectionModel().select(SessieType.AANGEMAAKT);
        dc.getSessieStatusProperty().addListener((v, o, n) -> typeCBox.getSelectionModel().select(n));
        btnSessieToevoegen.setOnAction(e -> {
            BorderPane parent = (BorderPane) getParent();
            parent.setCenter(new SessieToevoegenController(dc));
        });
    }

}
