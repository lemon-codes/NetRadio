package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import codes.lemon.netradio.model.ObservableMetadata;

/**
 * Offers basic playback functionality and volume control.
 * Displays basic playback metadata (station name, track name, volume level)
 *
 */
public class PlaybackController implements Initializable, ModelEventHandler {
    // Buttons
    @FXML private Button addStationButton;
    @FXML private Button previousStationButton;
    @FXML private Button nextStationButton;
    @FXML private Button playbackButton;

    // volume control
    @FXML private Slider volumeSlider;
    @FXML private Text volumeLevel;

    // playback display
    @FXML private Text stationName;
    @FXML private Text trackName;

    private final ModelAdapter model = ModelAdapterImpl.getInstance();
    private Station highlightedStation = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribeToModelEvents(this);
        subscribeToTagUpdates();
        updateVolumeDisplay();
        updatePlaybackDisplay();
    }

    /**
     * Subscribes to tag updates provided by the model. Tag updates include
     * real-time playback metadata. We use these real-time updates to update
     * the view, keeping the view and model consistent with one another.
     */
    private void subscribeToTagUpdates() {
        model.getObservableMetadata().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String type = evt.getPropertyName();
                switch (type) {
                    case ObservableMetadata.PROP_TITLE -> trackName.setText((String)evt.getNewValue());
                    // TODO: consider other cases
                }
            }
        });
    }

    /**
     * Initiate playback of the currently selected station.
     * @param actionEvent
     */
    public void playPressed(ActionEvent actionEvent) {

    }

    /**
     * Toggles playback. If the model is playing then we stop playback.
     * If the model is stopped then we initiate playback.
     * @param actionEvent
     */
    public void playbackButtonPressed(ActionEvent actionEvent) {
        if (model.isPlaying()) {
            model.stop();
        }
        else {
            // start playback of highlighted station if set, else last set station in model.
            if (!playHighlightedStation()) {
                model.play();
            }
        }
        // view is updated when it receives model events triggered by any of the above actions
    }

    /**
     * Plays the next station in the list. Other components in the UI
     * decide which station is chosen.
     * @param actionEvent
     */
    public void playNextStation(ActionEvent actionEvent) {
        model.requestNextHighlightedStation();
        playHighlightedStation();
    }

    /**
     * Plays the previous station in the list (positioned before the currently highlighted
     * station. Other components in the UI decide which station is chosen.
     * @param actionEvent
     */
    public void playPreviousStation(ActionEvent actionEvent) {
        model.requestPreviousHighlightedStation();
        playHighlightedStation();
    }

    /**
     * Attempts to play the highlighted station (if set).
     * @return true on success, else false.
     */
    private boolean playHighlightedStation() {
        if (highlightedStation != null) {
            model.setStation(highlightedStation.getStationID());
            model.play();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Volume slider adjusted by user. Update volume in model.
     * @param mouseEvent volume slider adjusted
     */
    public void mouseReleased(MouseEvent mouseEvent) {
        model.setVolume((int) volumeSlider.getValue());
    }

    /**
     * Volume slider is being moved by the user. Update volume in real time.
     * @param mouseEvent volume slider moved
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        model.setVolume((int) volumeSlider.getValue());
    }

    /**
     * Handle model events triggered by other components in the system.
     * @param event
     */
    @Override
    public void handleEvent(ModelAdapter.ModelEvent event) {
        switch(event) {
            case PLAYBACK_STARTED -> updatePlaybackDisplay();
            case PLAYBACK_STOPPED -> updatePlaybackDisplay();
            case STATION_CHANGED -> updatePlaybackDisplay();
            case STATION_ADDED -> {} // nothing to update
            case STATION_REMOVED -> {} // TODO: check if current station
            case STATION_EDITED -> {} // TODO: check if current station
            case SEARCH_RESULTS_READY ->{}
            case TAG_UPDATE-> {}
            case VOLUME_CHANGED -> updateVolumeDisplay();
            case SHUTDOWN -> {}
            case STATION_HIGHLIGHTED -> updateHighlightedStation();
        }
    }

    /**
     * Updates the highlighted station. Since this controller does not deal with station lists,
     * it is up to other components to set the highlighted station.
     */
    private void updateHighlightedStation() {
        this.highlightedStation = model.getHighlightedStation();
    }

    /**
     * Updates the volume level shown in the slider and the volume level text field
     * by requesting up to date values from the model
     */
    private void updateVolumeDisplay() {
        volumeSlider.setValue(model.getVolume());
        volumeLevel.setText(String.valueOf((int) volumeSlider.getValue()));
    }

    /**
     * Updates the current display to reflect the state of the model.
     * Updates the display of station name, track name and the playback
     * button.
     */
    private void updatePlaybackDisplay() {
        trackName.setText("");  // clear track name. The model will update this value when it becomes available
        updatePlaybackButtonIcon();

        if (model.isPlaying()) {
            stationName.setText(model.getCurrentStation().getStationName());
        }
        else {
            stationName.setText("");
        }
    }

    /**
     * Updates the playback button icon to reflect its action based upon the models state.
     * If the model is playing, a "stop" icon is set to allow users to stop playback.
     * If the model is not playing, a "play" icon is set to allow users to initiate playback.
     */
    private void updatePlaybackButtonIcon() {
        if (model.isPlaying()) {
            playbackButton.setStyle("-fx-shape: -stop-svg;");
        }
        else {
            playbackButton.setStyle("-fx-shape: -play-svg;");
        }
    }

    public void addStationPressed(ActionEvent actionEvent) {
        // show dialogue which allows the user to enter station details to be added to the model
        new AddStationDialog();
    }
}

