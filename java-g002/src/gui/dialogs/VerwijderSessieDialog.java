package gui.dialogs;


import domein.Sessie;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class VerwijderSessieDialog extends Dialog {

    public VerwijderSessieDialog(Sessie sessie) {

        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));

        setTitle("Verwijder sessie");
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Label("Weet u zeker dat u sessie \"" + sessie.getTitel() + "\" wilt verwijderen?"));
        getDialogPane().setContent(vBox);
        ButtonType bevestig = new ButtonType("Ja", ButtonBar.ButtonData.APPLY);
        ButtonType annuleer = new ButtonType("Neen", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(bevestig, annuleer);
        setResultConverter(dialogButton -> {
            if (dialogButton == bevestig) {
                return sessie;
            }
            return null;
        });
    }
}

