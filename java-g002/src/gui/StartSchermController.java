package gui;

import domein.DomeinController;
import gui.gebruikers.GebruikersBeherenController;
import gui.sessieKalenders.SessieKalenderController;
import gui.sessies.BeherenSessieController;
import gui.statistieken.StatistiekOverzichtSchermController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class StartSchermController extends TabPane {

    @FXML
    private Tab sessieBeherenTab;
    @FXML
    private Tab sessieKalenderTab;
    @FXML
    private Tab gebruikersTab;
    @FXML
    private Tab SessieToevoegenTab;
    @FXML
    private Tab statistiekTab;

    private DomeinController dc;

    public StartSchermController(DomeinController dc) {

       this.dc =dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("startscherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sessieKalenderTab.setContent(new SessieKalenderController(dc, this));
        sessieBeherenTab.setContent(new BeherenSessieController(dc));
        gebruikersTab.setContent(new GebruikersBeherenController(dc));
        statistiekTab.setContent(new StatistiekOverzichtSchermController(dc));
    }
}