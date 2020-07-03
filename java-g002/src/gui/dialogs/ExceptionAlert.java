package gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ExceptionAlert extends Alert {
    public ExceptionAlert(AlertType alertType, String message) {
        super(alertType);
        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));
        setTitle("Fout");
        setHeaderText("Er is iets fout gelopen:");
        setContentText(message);
    }
}
