package gui;

import domein.DomeinController;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.GebruikersStatusType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginSchermController extends VBox {

    @FXML
    private Label lblWachtwoord;

    @FXML
    private Label lblGebruikersnaam;

    @FXML
    private Label lblFoutmelding;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField txtGebruikersnaam;

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgITLab;

    private DomeinController dc;

    public LoginSchermController(Stage stage) throws Exception {
        dc = new DomeinController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                btnLogin.fire();
        });

        btnLogin.setOnAction(e -> {

            if (dc.controleLogin(txtGebruikersnaam.getText(), passField.getText())) {

                StartSchermController st;
                st = new StartSchermController(dc);
                Scene scene = new Scene(st);
                scene.getStylesheets().add("/gui/stylesheet.css");

                stage.setScene(scene);
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
                stage.setMaxHeight(primaryScreenBounds.getHeight());
                stage.setMinHeight(primaryScreenBounds.getHeight());
                stage.setMaximized(true);
                stage.setResizable(false);
                stage.show();

            } else {
                lblFoutmelding.setVisible(true);
            }
        });
    }

}