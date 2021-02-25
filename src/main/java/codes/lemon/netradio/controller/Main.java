package codes.lemon.netradio.controller;

import codes.lemon.netradio.controller.ModelAdapterImpl;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/root.fxml"));
        primaryStage.setTitle("NetRadio");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // shut down model on Application exit.
        ModelAdapterImpl.getInstance().shutdown();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
