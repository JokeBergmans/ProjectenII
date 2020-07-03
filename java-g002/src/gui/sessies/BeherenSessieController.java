package gui.sessies;

import domein.DomeinController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BeherenSessieController extends BorderPane {

    private DomeinController dc;

    public BeherenSessieController(DomeinController dc) {
        this.dc = dc;
        SessieGegevensSchermController sgc = new SessieGegevensSchermController(dc);
        SessieOverzichtController soc = new SessieOverzichtController(dc);
        setCenter(sgc);
        setAlignment(getCenter(), Pos.TOP_LEFT);
        setLeft(soc);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("beherensessiescherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
