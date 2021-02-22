package codes.lemon.netradio.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A dialogue which allows users to define a new station which is added to
 * the model. The user is required to supply a station name and audio source (URL).
 */
public class AddStationDialog extends GridPane implements Initializable {
    @FXML private TextField stationName;
    @FXML private TextField sourceURI;
    @FXML private Button addStationButton;
    @FXML private Button cancelButton;

    private final ModelAdapter model = ModelAdapterImpl.getInstance();
    private final Stage stage;


    public AddStationDialog() {
        // stage should be initialised first to allow action handlers to access stage instance in initialise().
        stage = new Stage();
        stage.setTitle("Enter new station details");

        // Load view layout defined in FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/addStationDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        // apply view layout to stage and initialise @FXML fields
        try {
            // load() initialises @FXML annotated fields and calls initialise()
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // @FXML initialised fields can be accessed from here on (after loader.load())
        // Any modifications to UI components at creation time  should be done in initialise().


        // only show view once all functionality is ready.
        stage.show();
    }

    /**
     * Initialises UI components to their desired state before being displayed to the user
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // add event handlers
        addStationButton.setOnAction(event -> addStationButtonPressed());
        cancelButton.setOnAction(event -> stage.close());
    }

    /**
     * Add button has been pressed.
     * Process submitted form and add new station to model.
     */
    private void addStationButtonPressed() {
        model.addStation(stationName.getText(), sourceURI.getText());
        stage.close();
    }

}
