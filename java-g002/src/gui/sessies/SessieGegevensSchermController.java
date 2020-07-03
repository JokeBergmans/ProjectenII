package gui.sessies;

import domein.*;
import exceptions.*;
import gui.dialogs.AankondigingDialog;
import gui.dialogs.ExceptionAlert;
import gui.dialogs.NieuweAankondigingDialog;
import gui.dialogs.WijzigAlert;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SessieGegevensSchermController extends BorderPane {

    @FXML
    private TextField titelTextfield;

    @FXML
    private Label verantwoordelijkeLabel;

    @FXML
    private TextField gastsprekerTextfield;

    @FXML
    private ComboBox<SessieType> statusCombobox;

    @FXML
    private ComboBox<String> campusCombobox;

    @FXML
    private ComboBox<String> gebouwCombobox;

    @FXML
    private ComboBox<String> lokaalCombobox;

    @FXML
    private Label aantalPlaatsenLabel;

    @FXML
    private CheckBox autoHerrinneringCheckbox;

    @FXML
    private ComboBox<Integer> dagenCombobox;

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
    private ListView<Inschrijving> ingeschrevenGebruikersListview;

    @FXML
    private ListView<Aankondiging> geplaatsteAankondigingenListview;

    @FXML
    private Button aankondigingButton;

    @FXML
    private Button wijzigButton;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private TextField zoekField;

    @FXML
    private Button clearButton;

    private DomeinController dc;

    public SessieGegevensSchermController(DomeinController dc) {

        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/sessies/sessiegegevensscherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        upButton.setOnAction(e -> {
            if(ingeschrevenGebruikersListview.getItems().size() > 0) {
                if(ingeschrevenGebruikersListview.getSelectionModel().isEmpty()) {
                    ingeschrevenGebruikersListview.getSelectionModel().select(ingeschrevenGebruikersListview.getItems().size() - 1);
                } else {
                    if(ingeschrevenGebruikersListview.getSelectionModel().getSelectedIndex() != 0) {
                        ingeschrevenGebruikersListview.getSelectionModel().select(ingeschrevenGebruikersListview.getSelectionModel().getSelectedIndex() - 1);
                    }
                }
            }
        });

        downButton.setOnAction(e -> {
            if(ingeschrevenGebruikersListview.getItems().size() > 0) {
                if(ingeschrevenGebruikersListview.getSelectionModel().isEmpty()) {
                    ingeschrevenGebruikersListview.getSelectionModel().select(0);
                } else {
                    if(ingeschrevenGebruikersListview.getSelectionModel().getSelectedIndex() != ingeschrevenGebruikersListview.getItems().size() - 1) {
                        ingeschrevenGebruikersListview.getSelectionModel().select(ingeschrevenGebruikersListview.getSelectionModel().getSelectedIndex() + 1);
                    }
                }
            }
        });

        clearButton.setOnAction(e -> {
            zoekField.clear();
            ingeschrevenGebruikersListview.getSelectionModel().select(null);
        });

        zoekField.textProperty().addListener((obs, o, n) -> {
            ingeschrevenGebruikersListview.setItems(dc.geefInschrijvingenOpNaam(zoekField.getText()));
        });

        changeDisable(true);
        ingeschrevenGebruikersListview.setCellFactory(lv -> new IngeschrevenGebruikerCellController(dc));
        dc.getSessieStatusProperty().addListener((c, o, n) -> {
            if (n == null)
                changeDisable(true);
            else {
                if (n != SessieType.AANGEMAAKT) {
                    titelTextfield.setDisable(true);
                    gastsprekerTextfield.setDisable(true);
                    startUur.setDisable(true);
                    startMin.setDisable(true);
                    eindUur.setDisable(true);
                    eindMin.setDisable(true);
                    startdatumDatepicker.setDisable(true);
                    einddatumDatepicker.setDisable(true);
                    campusCombobox.setDisable(true);
                    gebouwCombobox.setDisable(true);
                    lokaalCombobox.setDisable(true);
                } else if (dc.getSessieProperty().getValue() != null)
                    changeDisable(false);
            }
        });

        dc.getSessieProperty().addListener((c, o, n) -> setFields(n));

        startUur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        eindUur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        startMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        eindMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));

        campusCombobox.selectionModelProperty().addListener(e -> {
            String campus = campusCombobox.getSelectionModel().getSelectedItem();
            if (campus != null) {
                gebouwCombobox.setItems(FXCollections.observableList(dc.getGebouwenVanCampus(campus)));
            } else
                gebouwCombobox.setItems(FXCollections.emptyObservableList());
        });

        autoHerrinneringCheckbox.selectedProperty().addListener((obv, o, n) -> {
            if (n) {
                mededelingHerinneringTextarea.setEditable(true);
                dagenCombobox.setItems(FXCollections.observableList(Arrays.asList(1, 2, 3, 7)));
            } else {
                mededelingHerinneringTextarea.setEditable(false);
                dagenCombobox.setItems(FXCollections.emptyObservableList());
            }
        });

        gebouwCombobox.selectionModelProperty().addListener(e -> {
            String campus = campusCombobox.getSelectionModel().getSelectedItem();
            String gebouw = gebouwCombobox.getSelectionModel().getSelectedItem();
            lokaalCombobox.setItems(FXCollections.observableList(dc.getLokaalNamenVanGebouwInCampus(campus, gebouw)));
            lokaalCombobox.getSelectionModel().clearSelection();
            lokaalCombobox.setValue(null);
        });

        startdatumDatepicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        startdatumDatepicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null && einddatumDatepicker.getValue() != null && newValue.isAfter(einddatumDatepicker.getValue())) {
                einddatumDatepicker.setValue(newValue);
            }
        });

        einddatumDatepicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate start = startdatumDatepicker.getValue();
                if (start != null) {
                    setDisable(empty || date.isBefore(start));
                }
            }
        });

        autoHerrinneringCheckbox.selectedProperty().addListener(e -> {
            if (autoHerrinneringCheckbox.isSelected()) {
                dagenCombobox.getSelectionModel().select((Integer) 1);
            } else {
                dagenCombobox.getSelectionModel().clearSelection();
            }
        });

        geplaatsteAankondigingenListview.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Aankondiging aankondiging, boolean empty) {
                super.updateItem(aankondiging, empty);
                if (empty) {
                    setText(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setText(aankondiging.toString());
                }
            }
        });

        geplaatsteAankondigingenListview.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                new AankondigingDialog(geplaatsteAankondigingenListview.getSelectionModel().getSelectedItem()).showAndWait();
        });

        wijzigButton.disableProperty().bind(
                Bindings.isEmpty(titelTextfield.textProperty())
                        .or(Bindings.isEmpty(gastsprekerTextfield.textProperty()))
                        .or(Bindings.isNull(lokaalCombobox.getSelectionModel().selectedItemProperty()))
                        .or(Bindings.isNull(startdatumDatepicker.valueProperty()))
                        .or(Bindings.isNull(einddatumDatepicker.valueProperty()))
        );

        wijzigButton.setOnAction(e -> {

            WijzigAlert wijzigAlert = new WijzigAlert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = wijzigAlert.showAndWait();

            if (result.orElse(null) == ButtonType.OK) {
                try {
                    SessieDTO sessieDTO = new SessieDTO();
                    sessieDTO.setTitel(titelTextfield.getText());
                    sessieDTO.setGastspreker(gastsprekerTextfield.getText());
                    sessieDTO.setStatus(statusCombobox.getSelectionModel().getSelectedItem());
                    sessieDTO.setCampus(campusCombobox.getSelectionModel().getSelectedItem());
                    sessieDTO.setGebouw(gebouwCombobox.getSelectionModel().getSelectedItem());
                    sessieDTO.setLokaalNaam(lokaalCombobox.getSelectionModel().getSelectedItem());
                    sessieDTO.setStartDatum(startdatumDatepicker.getValue(), startUur.getValue(), startMin.getValue());
                    sessieDTO.setEindDatum(einddatumDatepicker.getValue(), eindUur.getValue(), eindMin.getValue());
                    sessieDTO.setAantalPlaatsen(dc.getSessieProperty().getValue().getMaxPlaatsen());
                    sessieDTO.setHerinnering(autoHerrinneringCheckbox.isSelected());

                    if (autoHerrinneringCheckbox.isSelected()) {
                        sessieDTO.setDagenOpVoorhand(dagenCombobox.getSelectionModel().getSelectedItem());
                        if (mededelingHerinneringTextarea != null && mededelingHerinneringTextarea.getText() != null) {
                            if (!(mededelingHerinneringTextarea.getText().equals("") || mededelingHerinneringTextarea.getText().isEmpty() || mededelingHerinneringTextarea.getText().isBlank())) {
                                sessieDTO.setMededeling(mededelingHerinneringTextarea.getText());
                            } else {
                                sessieDTO.setMededeling("");
                            }
                        }
                    }
                    dc.pasSessieAan(sessieDTO);

                    Sessie s = dc.getSessieProperty().getValue();
                    setFields(s);
                    dc.getSessieStatusProperty().setValue(statusCombobox.getValue());

                } catch (OngeldigeSessieGegevensException | OngeldigLokaalException ex) {
                    new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            }
        });

        aankondigingButton.setOnAction(e -> {
            Dialog dialog = new NieuweAankondigingDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(tekst -> {
                try {
                    dc.voegAankondigingToe(tekst);
                } catch (OngeldigeAankondigingTekstException ex) {
                    new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });
        });
    }

    private void setFields(Sessie sessie) {
        if (sessie != null) {
            if (dc.getSessieStatusProperty().getValue() == SessieType.AANGEMAAKT)
                changeDisable(false);
            else
                statusCombobox.setDisable(false);
            titelTextfield.setText(sessie.getTitel());
            verantwoordelijkeLabel.setText(sessie.getNaamVerantwoordelijke());
            gastsprekerTextfield.setText(sessie.getGastSpreker());

            startUur.getValueFactory().setValue(sessie.getStartUur());
            startMin.getValueFactory().setValue(sessie.getStartMin());
            eindUur.getValueFactory().setValue(sessie.getEindUur());
            eindMin.getValueFactory().setValue(sessie.getEindMin());

            aantalPlaatsenLabel.setText(Integer.toString(sessie.getMaxPlaatsen()));

            setStatusComboboxItems(dc.getSessieStatusProperty().getValue());
            statusCombobox.setValue(sessie.getStatus());

            campusCombobox.setItems(FXCollections.observableList(dc.getCampussen()));
            campusCombobox.getSelectionModel().select(sessie.getCampus());

            gebouwCombobox.setItems(FXCollections.observableList(dc.getGebouwenVanCampus(sessie.getCampus())));
            gebouwCombobox.getSelectionModel().select(sessie.getGebouw());

            lokaalCombobox.setItems(FXCollections.observableList(dc.getLokaalNamenVanGebouwInCampus(sessie.getCampus(), sessie.getGebouw())));
            lokaalCombobox.getSelectionModel().select(sessie.getLokaalNaam());

            autoHerrinneringCheckbox.setSelected(sessie.getStuurHerrinering());

            int dagen = sessie.getDagenOpVoorhand();
            if (dagen == 0) {
                dagenCombobox.getSelectionModel().clearSelection();
            } else {
                dagenCombobox.getSelectionModel().select((Integer) dagen);

            }
            startdatumDatepicker.setValue(sessie.getStartDatum());
            einddatumDatepicker.setValue(sessie.getEindDatum());
            mededelingHerinneringTextarea.setText(sessie.getMededeling());

            ingeschrevenGebruikersListview.setItems(dc.geefInschrijvingenHuidigeSessie());

            geplaatsteAankondigingenListview.setItems(sessie.getGeplaatsteAankondigingen());
            if (sessie.getStatus() != SessieType.AFGELOPEN)
                aankondigingButton.setDisable(false);

        } else {
            changeDisable(true);
            titelTextfield.setText("");
            gastsprekerTextfield.setText("");
            verantwoordelijkeLabel.setText("");
            startUur.getValueFactory().setValue(0);
            startMin.getValueFactory().setValue(0);
            eindUur.getValueFactory().setValue(0);
            eindMin.getValueFactory().setValue(0);
            startdatumDatepicker.setValue(null);
            einddatumDatepicker.setValue(null);
            statusCombobox.setItems(FXCollections.emptyObservableList());
            campusCombobox.setItems(FXCollections.emptyObservableList());
            gebouwCombobox.setItems(FXCollections.emptyObservableList());
            lokaalCombobox.setItems(FXCollections.emptyObservableList());
            mededelingHerinneringTextarea.setText("");
            aantalPlaatsenLabel.setText("");
            ingeschrevenGebruikersListview.setItems(FXCollections.emptyObservableList());
            geplaatsteAankondigingenListview.setItems(FXCollections.emptyObservableList());
        }
    }

    private void setStatusComboboxItems(SessieType s) {
        ArrayList<SessieType> statusTypes = new ArrayList<>(Arrays.asList(SessieType.values()));
        int i = statusTypes.indexOf(s);
        statusTypes = new ArrayList<>(statusTypes.subList(i, statusTypes.size()));
        statusCombobox.setItems(FXCollections.observableArrayList(statusTypes));
    }

    private void changeDisable(boolean disabled) {
        titelTextfield.setDisable(disabled);
        gastsprekerTextfield.setDisable(disabled);
        startUur.setDisable(disabled);
        startMin.setDisable(disabled);
        eindUur.setDisable(disabled);
        eindMin.setDisable(disabled);
        statusCombobox.setDisable(disabled);
        startdatumDatepicker.setDisable(disabled);
        einddatumDatepicker.setDisable(disabled);
        aankondigingButton.setDisable(disabled);
        autoHerrinneringCheckbox.setDisable(disabled);
        campusCombobox.setDisable(disabled);
        gebouwCombobox.setDisable(disabled);
        lokaalCombobox.setDisable(disabled);
        mededelingHerinneringTextarea.setDisable(disabled);
        dagenCombobox.setDisable(disabled);
    }
}