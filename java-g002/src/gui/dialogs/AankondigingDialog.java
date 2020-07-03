package gui.dialogs;

import domein.Aankondiging;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class AankondigingDialog extends Dialog {
    public AankondigingDialog(Aankondiging aankondiging) {
        getDialogPane().getStylesheets().add(getClass().getResource("/gui/stylesheet.css").toExternalForm());
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(new Image("resources/icon.png"));
        setTitle("Details aankondiging");
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);

        TextArea tekst = new TextArea();
        tekst.setText(aankondiging.getTekst());
        tekst.setPrefHeight(100);
        tekst.setWrapText(true);
        tekst.setEditable(false);

        HBox tekstHbox = new HBox(new Label("Inhoud van de aankondiging:"), tekst);
        tekstHbox.setSpacing(10);
        tekstHbox.setAlignment(Pos.CENTER);

        HBox publicistHbox = new HBox(new Label("Publicist:"), new Label(aankondiging.getPublicist().getNaam()));
        publicistHbox.setSpacing(10);

        HBox datumHbox = new HBox(new Label("Publicatiedatum:"), new Label(aankondiging.getDatum().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))));
        datumHbox.setSpacing(10);

        vBox.getChildren().addAll(tekstHbox, publicistHbox, datumHbox);
        getDialogPane().setContent(vBox);
        ButtonType annuleer = new ButtonType("Sluit", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(annuleer);
    }
}
