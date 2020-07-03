package gui.gebruikers;


import domein.DomeinController;
import domein.Gebruiker;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GebruikerCell extends ListCell<Gebruiker> {

    @FXML
    private ImageView imvGebruiker;
    @FXML
    private VBox vboxGebruiker;
    @FXML
    private Label lblNaam;
    @FXML
    private Label lblGebruikersnaam;
    @FXML
    private Label lblType;
    @FXML
    private Label lblStatus;

    private DomeinController dc;

    public GebruikerCell(DomeinController dc) {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gebruikercell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Gebruiker gebruiker, boolean empty) {
        super.updateItem(gebruiker, empty);
        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            lblStatus.setText(gebruiker.getStatus().toString());

            lblNaam.setText(gebruiker.getNaam());
            lblGebruikersnaam.setText(gebruiker.getGebruikersnaam());
            lblType.setText(gebruiker.getType().toString());
            imvGebruiker.setImage(zetOmNaarImage(gebruiker.getProfielFoto()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private Image zetOmNaarImage(byte[] array) {
        try {
            InputStream in = new ByteArrayInputStream(array);
            BufferedImage buf;
            buf = ImageIO.read(in);
            return SwingFXUtils.toFXImage(buf, null);
        } catch (IOException | NullPointerException e) {
            return new Image("resources/placeholder.png");
        }
    }



}
