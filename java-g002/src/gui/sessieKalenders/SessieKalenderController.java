package gui.sessieKalenders;

import domein.DomeinController;
import domein.Sessie;
import domein.SessieKalender;
import exceptions.OngeldigeSessieKalenderGegevensException;
import gui.StartSchermController;
import gui.dialogs.ExceptionAlert;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public class SessieKalenderController extends BorderPane {

    @FXML
    private ListView<SessieKalender> sessieKalenderLv;

    @FXML
    private Label jaar1;

    @FXML
    private Label streep;

    @FXML
    private Label jaar2;

    @FXML
    private DatePicker startDp;

    @FXML
    private DatePicker eindeDp;

    @FXML
    private ListView<Sessie> sessieLv;

    @FXML
    private Button toevoegenBtn;

    @FXML
    private Button opslaanBtn;

    @FXML
    private ComboBox<String> maandCb;

    private DomeinController dc;

    private StartSchermController ssc;

    public SessieKalenderController(DomeinController dc, StartSchermController ssc) {
        this.dc = dc;
        this.ssc = ssc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sessiekalender.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startDp.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.getYear() < LocalDate.now().getYear() - 1);
            }
        });
        startDp.valueProperty().addListener((v, o, n) -> {
            if (n != null)
                jaar1.setText(String.valueOf(n.getYear()));
        });
        eindeDp.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (startDp.getValue() != null)
                    setDisable(item.isBefore(LocalDate.now()) || item.isBefore(startDp.getValue()) || item.isAfter(startDp.getValue().plusYears(1)));
                else
                    setDisable(item.isBefore(LocalDate.now()));
            }
        });
        eindeDp.valueProperty().addListener((v, o, n) -> {
            if (n != null)
                jaar2.setText(String.valueOf(n.getYear()));
        });
        sessieKalenderLv.setCellFactory(lv -> new KalenderCell(dc));
        sessieKalenderLv.setItems(dc.geefSessieKalenders());
        sessieKalenderLv.getSelectionModel().selectedItemProperty().addListener((o, c, n) -> setFields(n));



        maandCb.setItems(FXCollections.observableList(Arrays.asList("Januari", "Februari", "Maart", "April", "Mei", "Juni", "Juli", "Augustus", "September", "Oktober", "November", "December")));
        maandCb.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        maandCb.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (sessieKalenderLv.getSelectionModel().getSelectedItem() != null)
                setSessies(sessieKalenderLv.getSelectionModel().getSelectedItem(), maandCb.getSelectionModel().getSelectedIndex() + 1);
        });

        Optional<SessieKalender> sessieKalender = dc.geefSessieKalenders().stream().filter(s -> s.getStart().isBefore(LocalDate.now()) && s.getEinde().isAfter(LocalDate.now())).findFirst();
        sessieKalender.ifPresent(kalender -> {
            sessieKalenderLv.getSelectionModel().select(kalender);
            setFields(kalender);
        });

        sessieLv.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Sessie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setText(item.toString());
                }
            }
        });
        sessieLv.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                dc.getSessieProperty().setValue(sessieLv.getSelectionModel().getSelectedItem());
                ssc.getSelectionModel().select(0);
            }
        });
        toevoegenBtn.setOnAction(e -> setCenter(new SessieKalenderToevoegenController(dc)));
        opslaanBtn.disableProperty().bind(Bindings.isNull(startDp.valueProperty()).or(Bindings.isNull(eindeDp.valueProperty())));
        opslaanBtn.setOnAction(e -> {
            try {
                dc.pasSessieKalenderAan(sessieKalenderLv.getSelectionModel().getSelectedItem(), startDp.getValue(), eindeDp.getValue());
            } catch (OngeldigeSessieKalenderGegevensException ex) {
                new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
    }

    private void setFields(SessieKalender sessieKalender) {
        if (sessieKalender != null) {
            jaar1.setText(String.valueOf(sessieKalender.getStart().getYear()));
            jaar2.setText(String.valueOf(sessieKalender.getEinde().getYear()));
            streep.setVisible(true);
            startDp.setDisable(false);
            startDp.setValue(sessieKalender.getStart());
            eindeDp.setDisable(false);
            eindeDp.setValue(sessieKalender.getEinde());
            setSessies(sessieKalender, maandCb.getSelectionModel().getSelectedIndex() + 1);
        } else {
            jaar1.setText("");
            streep.setVisible(false);
            jaar2.setText("");
            startDp.setValue(null);
            eindeDp.setValue(null);
            startDp.setDisable(true);
            eindeDp.setDisable(true);
            sessieLv.setItems(FXCollections.emptyObservableList());
        }
    }

    private void setSessies(SessieKalender sessieKalender, int maand) {
        sessieLv.setItems(dc.geefSessieKalenderSessies(sessieKalender, maand));

    }

    public void resetContent() {
        ssc.getSelectionModel().getSelectedItem().setContent(new SessieKalenderController(dc, ssc));
    }
}
