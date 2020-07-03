package main;

import gui.LoginSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartUp extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        LoginSchermController scc = new LoginSchermController(stage);
        Scene scene = new Scene(scc);
        scene.getStylesheets().add("/gui/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("IT-Lab BeheerApplicatie");
        stage.getIcons().add(new Image("resources/icon.png"));
        stage.show();

    }
}
