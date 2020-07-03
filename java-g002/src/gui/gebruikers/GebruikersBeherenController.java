package gui.gebruikers;

import domein.DomeinController;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.GebruikersStatusType;
import exceptions.OngeldigeVerwijderingException;
import gui.dialogs.ExceptionAlert;
import gui.dialogs.VerwijderGebruikerDialog;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.Optional;

public class GebruikersBeherenController extends BorderPane {

    @FXML
    private Label lblNaam;

    @FXML
    private Label lblGebruikersnaam;

    @FXML
    private Label lblType;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblProfielfoto;

    @FXML
    private Label lblpassword;

    @FXML
    private Button btnAnnuleren;

    @FXML
    private Button btnOpslaan;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<GebruikerType> cboxType;

    @FXML
    private ComboBox<GebruikersStatusType> cboxStatus;

    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtGebruikersnaam;

    @FXML
    private TextField txtZoekGebruiker;

    @FXML
    private ListView<Gebruiker> lstGebruikers;

    @FXML
    private Button btnGebruikerOpslaan;

    @FXML
    private Button btnGebruikerToevoegen;


    @FXML
    private Button btnGebruikerVerwijderen;

    @FXML
    private Button btnWijzigen;

    @FXML
    private ImageView imvProfielfoto;

    @FXML
    private HBox pwHbox;

    @FXML
    private HBox wijzigHbox;

    @FXML
    private HBox voegtoeHbox;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button clearButton;

    private DomeinController dc;

    public GebruikersBeherenController(DomeinController dc) {
        this.dc = dc;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gebruikersbeheren.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cboxStatus.getItems().addAll(GebruikersStatusType.ACTIEF, GebruikersStatusType.NIET_ACTIEF, GebruikersStatusType.GEBLOKKEERD);
        cboxStatus.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(GebruikersStatusType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setText(item.toString());
                }
            }
        });
        cboxType.getItems().addAll(GebruikerType.HOOFDVERANTWOORDELIJKE, GebruikerType.VERANTWOORDELIJKE, GebruikerType.GEBRUIKER);
        cboxType.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(GebruikerType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setText(item.toString());
                }
            }
        });
        cboxStatus.setOnAction(e -> {
        });
        cboxType.setOnAction(e -> {
        });

        lstGebruikers.setCellFactory(lv -> new GebruikerCell(dc));
        lstGebruikers.setItems(dc.geefGebruikers());

        lstGebruikers.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            dc.setGeselecteerdeGebruiker(n);
            setFields(n);
        });

        upButton.setOnAction(e -> {
            if(lstGebruikers.getItems().size() > 0) {
                if(lstGebruikers.getSelectionModel().isEmpty()) {
                    lstGebruikers.getSelectionModel().select(lstGebruikers.getItems().size() - 1);
                } else {
                    if(lstGebruikers.getSelectionModel().getSelectedIndex() != 0) {
                        lstGebruikers.getSelectionModel().select(lstGebruikers.getSelectionModel().getSelectedIndex() - 1);
                    }
                }
            }
        });

        downButton.setOnAction(e -> {
            if(lstGebruikers.getItems().size() > 0) {
                if(lstGebruikers.getSelectionModel().isEmpty()) {
                    lstGebruikers.getSelectionModel().select(0);
                } else {
                    if(lstGebruikers.getSelectionModel().getSelectedIndex() != lstGebruikers.getItems().size() - 1) {
                        lstGebruikers.getSelectionModel().select(lstGebruikers.getSelectionModel().getSelectedIndex() + 1);
                    }
                }
            }
        });

        clearButton.setOnAction(e -> {
            txtZoekGebruiker.clear();
            lstGebruikers.getSelectionModel().select(null);
        });

        txtZoekGebruiker.textProperty().addListener((obs, o, n) -> {
            lstGebruikers.setItems(
                    dc.geefGebruikerOpNaam(dc.geefGebruikers(), txtZoekGebruiker.getText()));
        });


        btnGebruikerOpslaan.disableProperty().bind(
                Bindings.isEmpty(txtNaam.textProperty())
                        .or(Bindings.isEmpty(txtGebruikersnaam.textProperty()))
                        .or(Bindings.isNull(cboxStatus.getSelectionModel().selectedItemProperty()))
                        .or(Bindings.isNull(cboxType.getSelectionModel().selectedItemProperty()))
        );

        btnGebruikerToevoegen.setOnAction(e -> changeVisibility(true));

        btnAnnuleren.setOnAction(e -> {
            txtNaam.clear();
            txtGebruikersnaam.clear();
            lstGebruikers.setDisable(false);
            changeVisibility(false);
        });

        btnWijzigen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                imvProfielfoto.setImage(image);

            } catch (IOException ex) {
                imvProfielfoto.setImage(new Image("resources/placeholder.png"));
            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        });

        btnOpslaan.setOnAction(e -> {
            try {
                dc.voegGebruikerToe(new Gebruiker(cboxType.getValue(), txtNaam.getText(), txtGebruikersnaam.getText(), cboxStatus.getValue(), password.getText(), zetOmNaarByte(imvProfielfoto.getImage())));
                txtNaam.clear();
                txtGebruikersnaam.clear();
                password.clear();
                changeVisibility(false);
            } catch (Exception ex) {
                new ExceptionAlert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        btnGebruikerVerwijderen.setOnAction(actionEvent -> verwijder(lstGebruikers.getSelectionModel().getSelectedItem()));

        btnGebruikerOpslaan.setOnAction(e -> dc.pasGebruikerAan(new Gebruiker(cboxType.getValue(), txtNaam.getText(), txtGebruikersnaam.getText(), cboxStatus.getValue(), zetOmNaarByte(imvProfielfoto.getImage()))));


    }

    private void setFields(Gebruiker gebruiker) {
        if (gebruiker != null) {
            txtNaam.setText(gebruiker.getNaam());
            txtGebruikersnaam.setText(gebruiker.getGebruikersnaam());
            cboxStatus.getSelectionModel().select(gebruiker.getStatus());
            cboxType.getSelectionModel().select(gebruiker.getType());
            imvProfielfoto.setImage(zetOmNaarImage(gebruiker.getProfielFoto()));
        }
    }

    private void changeVisibility(boolean toevoegen) {
        txtNaam.clear();
        txtGebruikersnaam.clear();
        lstGebruikers.setDisable(toevoegen);
        cboxStatus.getSelectionModel().select(null);
        cboxType.getSelectionModel().select(null);
        imvProfielfoto.setImage(null);
        voegtoeHbox.setVisible(toevoegen);
        voegtoeHbox.setManaged(toevoegen);
        pwHbox.setVisible(toevoegen);
        pwHbox.setManaged(toevoegen);
        wijzigHbox.setVisible(!toevoegen);
        wijzigHbox.setManaged(!toevoegen);
    }

    private void verwijder(Gebruiker gebruiker) {
        Dialog dialog = new VerwijderGebruikerDialog(gebruiker);
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                dc.verwijderGebruiker(gebruiker);
            } catch (OngeldigeVerwijderingException e) {
                new ExceptionAlert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
    }

    private byte[] zetOmNaarByte(Image profielfoto) {
        BufferedImage bImage = SwingFXUtils.fromFXImage(profielfoto, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", s);
            byte[] res = s.toByteArray();
            s.close();
            return res;
        } catch (IOException e) {
            new ExceptionAlert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            return null;
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
