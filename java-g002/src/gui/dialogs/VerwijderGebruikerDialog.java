package gui.dialogs;

import domein.Gebruiker;
import domein.SessieKalender;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VerwijderGebruikerDialog extends Dialog {

    public VerwijderGebruikerDialog(Gebruiker gebruiker){

        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));


        setTitle("Verwijder gebruiker");
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Label("Weet u zeker dat u deze gebruiker met de naam: " +  gebruiker.getNaam()+ " wilt verwijderen?"));
        getDialogPane().setContent(vBox);
        ButtonType bevestig = new ButtonType("Ja", ButtonBar.ButtonData.APPLY);
        ButtonType annuleer = new ButtonType("Neen", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(bevestig, annuleer);
        setResultConverter(dialogButton -> {
            if (dialogButton == bevestig) {
                return gebruiker;
            }
            return null;
        });
    }


}
