package gui.statistieken;

import domein.DomeinController;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieType;
import gui.sessies.SessieCell;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class StatistiekOverzichtSchermController extends BorderPane {

    @FXML
    private VBox listViewBox;

    private ListView<Sessie> sessieLv;

    private ListView<Gebruiker> gebruikerLv;

    @FXML
    private ComboBox<String> keuzeBox;

    @FXML
    private Label aanwezigLbl;

    @FXML
    private Label afwezigLbl;

    @FXML
    private StackedBarChart<String, Number> barChart;

    @FXML
    private Button saveBtn;

    @FXML
    private VBox snapshotNode;


    private DomeinController dc;

    public StatistiekOverzichtSchermController(DomeinController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("statistiekoverzichtscherm.fxml"));
        this.getStylesheets().add("/gui/stylesheet.css");
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        saveBtn.setVisible(false);
        saveBtn.setDisable(true);

        sessieLv = new ListView<>();
        sessieLv.setCellFactory(lv -> new SessieCell(dc));

        sessieLv.getSelectionModel().selectedItemProperty().addListener(((obs, o, n) -> {
            if (n != null) {
                saveBtn.setVisible(true);
                saveBtn.setDisable(false);
                setBarChartData(n.getAantalAanwezigen(), n.getAantalAfwezigen());
            } else
                clearBarChart();
        }));

        saveBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File selectedDirectory = chooser.showDialog(this.getScene().getWindow());
            String naam = getFileNaam();
            if(selectedDirectory != null) {
                File file = new File(selectedDirectory + "/" + naam + ".png");
                WritableImage snapshot = snapshotNode.snapshot(new SnapshotParameters(), null);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        sessieLv.setPrefHeight(600);
        gebruikerLv = new ListView<>();
        gebruikerLv.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gebruiker item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNaam() == null) {
                    setText(null);
                } else {
                    setText(item.getNaam());
                }
            }
        });

        gebruikerLv.getSelectionModel().selectedItemProperty().addListener(((obs, o, n) -> {
            if (n != null) {
                saveBtn.setVisible(true);
                saveBtn.setDisable(false);
                setBarChartData(n.getAantalAanwezig(), n.getAantalAfwezig());
            } else
                clearBarChart();
        }));

        gebruikerLv.setPrefHeight(600);
        keuzeBox.setItems(FXCollections.observableArrayList(Arrays.asList("Gebruiker", "Sessie")));
        keuzeBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            aanwezigLbl.setVisible(false);
            afwezigLbl.setVisible(false);
            if (keuzeBox.getSelectionModel().getSelectedItem().equals("Sessie")) {
                listViewBox.getChildren().remove(gebruikerLv);
                listViewBox.getChildren().add(sessieLv);
                sessieLv.setItems(dc.geefSessies(SessieType.AFGELOPEN));
                gebruikerLv.setItems(FXCollections.emptyObservableList());
            } else {
                listViewBox.getChildren().remove(sessieLv);
                listViewBox.getChildren().add(gebruikerLv);
                gebruikerLv.setItems(dc.geefGebruikers());
                sessieLv.setItems(FXCollections.emptyObservableList());
            }
        });
        keuzeBox.getSelectionModel().select(0);
    }


    private void setBarChartData(int aantalAanwezigen, int aantalAfwezigen) {

        barChart.setVisible(true);
        barChart.getData().clear();
        barChart.layout();

        aanwezigLbl.setVisible(true);
        aanwezigLbl.setText("Aantal aanwezigheden: " + aantalAanwezigen);

        afwezigLbl.setVisible(true);
        afwezigLbl.setText("Aantal afwezigheden: " + aantalAfwezigen);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList("Inschrijvingen", "Aanwezigheden"));

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Aanwezig");
        series1.getData().add(new XYChart.Data<>("Aanwezigheden", aantalAanwezigen));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Afwezig");
        series2.getData().add(new XYChart.Data<>("Aanwezigheden", aantalAfwezigen));

        barChart.setAnimated(false);
        barChart.getData().add(series1);
        barChart.getData().add(series2);

    }

    private void clearBarChart() {
        barChart.getData().clear();
        barChart.setTitle("");
        barChart.getYAxis().setLabel("");
        barChart.setVisible(false);
    }

    private String getFileNaam() {
        String naam = "Aanwezigheden";
        if (sessieLv.getSelectionModel().getSelectedItem() != null) {
            return sessieLv.getSelectionModel().getSelectedItem().getTitel() + naam;
        }
        if (gebruikerLv.getSelectionModel().getSelectedItem() != null) {
            return  gebruikerLv.getSelectionModel().getSelectedItem().getNaam() + naam;
        }
        return naam;
    }
}
