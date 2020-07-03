package gui.sessies;

import domein.DomeinController;
import domein.SessieDTO;
import domein.SessieType;
import exceptions.OngeldigLokaalException;
import exceptions.OngeldigeSessieGegevensException;
import gui.dialogs.ExceptionAlert;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class SessieToevoegenController extends VBox {
    @FXML
    private TextField titelTextfield;

    @FXML
    private Label verantwoordelijkeLabel;

    @FXML
    private Label lblLokaal;

    @FXML
    private CheckBox autoHerrinneringCheckbox;

    @FXML
    private TextArea mededelingHerinneringTextarea;

    @FXML
    private DatePicker startdatumDatepicker;

    @FXML
    private DatePicker einddatumDatepicker;

    @FXML
    private Spinner<Integer> startUur;

    @FXML
    private Spinner<Integer> startMin;

    @FXML
    private Spinner<Integer> eindUur;

    @FXML
    private Spinner<Integer> eindMin;

    @FXML
    private TextField gastsprekerTextfield;

    @FXML
    private Button btnSessieToevoegen;

    @FXML
    private Button btnAnnuleren;

    @FXML
    private ComboBox<String> campusCombobox;

    @FXML
    private ComboBox<String> gebouwCombobox;

    @FXML
    private ComboBox<String> lokaalCombobox;

    @FXML
    private TextField aantalPlaatsenTextfield;

    @FXML
    private ComboBox<Integer> dagenCombobox;

    private DomeinController dc;

    public SessieToevoegenController(DomeinController dc) {
        this.dc = dc;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessietoevoegen.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verantwoordelijkeLabel.setText(dc.getIngelogdeGebruiker().getNaam());
        dagenCombobox.getItems().addAll(1, 2, 3, 7);

        startdatumDatepicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });

        einddatumDatepicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate start = startdatumDatepicker.getValue();
                if (start != null) {
                    setDisable(empty || date.compareTo(start) < 0);
                }
            }
        });


        startUur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        eindUur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        startMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        eindMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));

        campusCombobox.setItems(FXCollections.observableList(dc.getCampussen()));
        campusCombobox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> gebouwCombobox.setItems(FXCollections.observableList(dc.getGebouwenVanCampus(n))));
        gebouwCombobox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> lokaalCombobox.setItems(FXCollections.observableList(dc.getLokaalNamenVanGebouwInCampus(campusCombobox.getSelectionModel().getSelectedItem(), n))));

        autoHerrinneringCheckbox.selectedProperty().addListener((obv, o, n) -> {
            if (n) {
                mededelingHerinneringTextarea.setEditable(true);
                dagenCombobox.setItems(FXCollections.observableList(Arrays.asList(1, 2, 3, 7)));
                dagenCombobox.setValue((Integer) 1);
            } else {
                mededelingHerinneringTextarea.setEditable(false);
                dagenCombobox.setItems(FXCollections.emptyObservableList());
            }
        });

        btnSessieToevoegen.disableProperty().bind(
                Bindings.isEmpty(titelTextfield.textProperty())
                        .or(Bindings.isEmpty(gastsprekerTextfield.textProperty()))
                        .or(Bindings.isNull(lokaalCombobox.getSelectionModel().selectedItemProperty()))
                        .or(Bindings.isNull(startdatumDatepicker.valueProperty()))
                        .or(Bindings.isNull(einddatumDatepicker.valueProperty()))
                        .or(Bindings.isEmpty(aantalPlaatsenTextfield.textProperty()))
        );

        btnSessieToevoegen.setOnAction(e -> {

            SessieDTO sessie = new SessieDTO();
            sessie.setTitel(titelTextfield.getText());
            sessie.setGastspreker(gastsprekerTextfield.getText());
            sessie.setStatus(SessieType.AANGEMAAKT);
            sessie.setCampus(campusCombobox.getSelectionModel().getSelectedItem());
            sessie.setGebouw(gebouwCombobox.getSelectionModel().getSelectedItem());
            sessie.setLokaalNaam(lokaalCombobox.getSelectionModel().getSelectedItem());
            sessie.setStartDatum(startdatumDatepicker.getValue(), startUur.getValue(), startMin.getValue());
            sessie.setEindDatum(einddatumDatepicker.getValue(), eindUur.getValue(), eindMin.getValue());
            sessie.setHerinnering(autoHerrinneringCheckbox.isSelected());
            sessie.setAantalPlaatsen(Integer.parseInt(aantalPlaatsenTextfield.getText()));

            if (autoHerrinneringCheckbox.isSelected()) {
                sessie.setDagenOpVoorhand(dagenCombobox.getSelectionModel().getSelectedItem());
                sessie.setMededeling(mededelingHerinneringTextarea.getText());
            }
            try {
                dc.voegSessieToe(sessie);
            } catch (OngeldigeSessieGegevensException | OngeldigLokaalException ex) {
                new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
            ((BorderPane) getParent()).setCenter(new SessieGegevensSchermController(dc));

        });
        btnAnnuleren.setOnAction(e -> ((BorderPane) getParent()).setCenter(new SessieGegevensSchermController(dc)));
    }
}