package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A dialog which enables the user to edit station details.
 * The stations name and audio source can be modified.
 * Other values cannot be modified since they are extracted from tags provided by
 * the audio source and values are subject to change at runtime.
 */
class EditStationDialog extends GridPane implements Initializable {
    @FXML private TextField stationName;
    @FXML private TextField sourceURI;
    @FXML private CheckBox favouriteStatus;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;


    private final ModelAdapter model = ModelAdapterImpl.getInstance();
    private final Stage stage;
    private final Station selectedStation;

    public EditStationDialog(int stationId) {
        // get current station from model which the user will then modify
        // station instances are immutable so values will be extracted and resubmitted to model
        selectedStation = Objects.requireNonNull(model.getStation(stationId));

        // stage should be initialised first to allow action handlers to access stage instance in initialise().
        stage = new Stage();
        stage.setTitle("Edit station details");

        // Load view layout defined in FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/editStationDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        // apply view layout to stage and initialise @FXML fields
        try {
            // load() initialises @FXML annotated fields and calls initialise()
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // only show view once all functionality is ready.
        stage.show();

    }


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fill text fields with current values to be edited
        stationName.setText(Objects.requireNonNull(selectedStation.getStationName()));
        sourceURI.setText(Objects.requireNonNull(selectedStation.getUri()));

        // mirror current favourite status
        favouriteStatus.setSelected(selectedStation.isFavourite());

        // add event handlers
        submitButton.setOnAction(event -> submitButtonPressed());
        cancelButton.setOnAction(event -> stage.close());
    }


    /**
     * Submit button has been pressed.
     * Replace previous copy of station with a station containing the newly
     * submitted station details.
     * Favourite status is maintained.
     */
    private void submitButtonPressed() {
        model.removeStation(selectedStation.getStationID());
        // new ID should == previous ID but that is not guaranteed or required.
        int newID = model.addStation(stationName.getText(), sourceURI.getText());

        // maintain favourite status
        model.setStationFavouriteStatus(newID, favouriteStatus.isSelected());

        // close window
        stage.close();

    }
}
