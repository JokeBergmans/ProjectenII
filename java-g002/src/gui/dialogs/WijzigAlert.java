package gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WijzigAlert extends Alert  {

    public WijzigAlert(Alert.AlertType alertType) {
        super(alertType);
        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));
        this.setTitle("Bevestig wijziging");
        this.setHeaderText("Bevestig wijziging");
        this.setContentText("Weet u zeker dat u deze sessie wilt wijzigen?");
    }


}
