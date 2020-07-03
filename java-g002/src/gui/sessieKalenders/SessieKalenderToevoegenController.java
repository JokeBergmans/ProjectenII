package gui.sessieKalenders;

import domein.DomeinController;
import exceptions.OngeldigeSessieKalenderGegevensException;
import gui.dialogs.ExceptionAlert;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class SessieKalenderToevoegenController extends VBox {

    @FXML
    private Label jaar1;

    @FXML
    private Label streep;

    @FXML
    private Label jaar2;

    @FXML
    private DatePicker startDp;

    @FXML
    private DatePicker eindDp;

    @FXML
    private Button annuleerBtn;

    @FXML
    private Button opslaanBtn;
    private DomeinController dc;

    public SessieKalenderToevoegenController(DomeinController dc) {
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sessiekalendertoevoegen.fxml"));
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
        startDp.valueProperty().addListener((v, o, n) -> jaar1.setText(String.valueOf(n.getYear())));
        eindDp.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (startDp.getValue() != null)
                    setDisable(item.isBefore(LocalDate.now()) || item.isBefore(startDp.getValue()) || item.isAfter(startDp.getValue().plusYears(1)));
                else
                    setDisable(item.isBefore(LocalDate.now()));
            }
        });
        eindDp.valueProperty().addListener((v, o, n) -> jaar2.setText(String.valueOf(n.getYear())));
        streep.visibleProperty().bind(Bindings.isNotNull(startDp.valueProperty()).and(Bindings.isNotNull(eindDp.valueProperty())));
        opslaanBtn.disableProperty().bind(Bindings.isNull(startDp.valueProperty()).or(Bindings.isNull(eindDp.valueProperty())));

        opslaanBtn.setOnAction(e -> {
            try {
                dc.voegSessieKalenderToe(startDp.getValue(), eindDp.getValue());
                ((SessieKalenderController) getParent()).resetContent();
            } catch (OngeldigeSessieKalenderGegevensException ex) {
                new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        annuleerBtn.setOnAction(e -> ((SessieKalenderController) getParent()).resetContent());
    }


}
