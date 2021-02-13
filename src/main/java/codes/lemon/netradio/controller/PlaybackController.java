package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
 */
public class PlaybackController implements Initializable, ModelEventHandler {
    @FXML private Slider volumeSlider;
    @FXML private Text volumeLevel;
    @FXML private Text stationName;
    @FXML private Text trackName;

    private final ModelAdapter model = ModelAdapterImpl.getInstance();
    private Station highlightedStation = null;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribeToModelEvents(this);
        subscribeToTagUpdates();
        updateVolumeDisplay();
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
        if (highlightedStation != null) {
            model.setStation(highlightedStation.getStationID());
        }
        model.play();
    }

    /**
     * Stop playback of the currently playing station.
     * @param actionEvent
     */
    public void stopPressed(ActionEvent actionEvent) {
        model.stop();
    }

    // TODO: implement nextChannel()
    public void nextChannel(ActionEvent actionEvent) {
        model.setStation(0);
        playPressed(new ActionEvent());
    }

    // TODO: implement previousChannel()
    public void previousChannel(ActionEvent actionEvent) {
        model.setStation(1);
        playPressed(new ActionEvent());
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
            case PLAYBACK_STARTED -> updateStationNameDisplay();
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
     * Updates station name and track name text fields in the display by
     * requesting up to date values from the model.
     */
    private void updatePlaybackDisplay() {
        updateStationNameDisplay();
        resetTrackNameDisplay();
    }


    /**
     * Updates the station name text field in the display by requesting up to
     * date values from the model.
     */
    private void updateStationNameDisplay() {
        if (model.isPlaying()) {
            stationName.setText(model.getCurrentStation().getStationName());
        }
        else {
            stationName.setText("");
        }
    }

    /**
     * Updates the track name text field in the display by requesting up to
     * date values from the model.
     */
    private void resetTrackNameDisplay() {
        trackName.setText("");
    }
}

