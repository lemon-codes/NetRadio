package codes.lemon.netradio.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/root.fxml"));
        primaryStage.setTitle("NetRadio");
        primaryStage.setScene(new Scene(loader.load()));
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
