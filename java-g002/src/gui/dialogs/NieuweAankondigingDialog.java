package gui.dialogs;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NieuweAankondigingDialog extends Dialog {

    public NieuweAankondigingDialog() {
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));
        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        setTitle("Nieuwe aankondiging");
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        TextArea tekst = new TextArea();
        tekst.setMinHeight(100);
        tekst.setWrapText(true);
        vBox.getChildren().addAll(new Label("Inhoud van de aankondiging:"), tekst);
        getDialogPane().setContent(vBox);
        ButtonType bevestig = new ButtonType("Voeg toe", ButtonBar.ButtonData.APPLY);
        ButtonType annuleer = new ButtonType("Annuleer", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(bevestig, annuleer);
        setResultConverter(dialogButton -> {
            if (dialogButton == bevestig) {
                return tekst.getText();
            }
            return null;
        });
    }
}
