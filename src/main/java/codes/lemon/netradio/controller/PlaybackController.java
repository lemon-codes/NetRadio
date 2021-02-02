package codes.lemon.netradio.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaybackController implements Initializable, ModelEventHandler {
    private final ModelAdapter model = ModelAdapterImpl.getInstance();

    @FXML private Slider slider;
    @FXML private Text volumeLevel;
    @FXML private Text stationName;
    @FXML private Text trackName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribeToModelEvents(this);
        updateVolumeDisplay();
    }

    public void playPressed(ActionEvent actionEvent) {
        model.play();
    }

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

    public void mouseReleased(MouseEvent mouseEvent) {
        model.setVolume((int)slider.getValue());
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        model.setVolume((int)slider.getValue());
    }

    @Override
    public void handleEvent(ModelAdapter.ModelEvent event) {
        switch(event) {
            case PLAYBACK_STARTED:  updatePlaybackDisplay();
                                    break;
            case PLAYBACK_STOPPED:  updatePlaybackDisplay();
                                    break;
            case STATION_CHANGED:   updatePlaybackDisplay();
                                    break;
            case STATION_ADDED:     break; // nothing to update
            case STATION_REMOVED:   break; // TODO: check if current station
            case STATION_EDITED:    break; // TODO: check if current station
            case SEARCH_RESULTS_READY: break;
            case TAG_UPDATE:        updateTrackNameDisplay();
            case VOLUME_CHANGED:    updateVolumeDisplay();
                                    break;
            case SHUTDOWN:          break;
        }

    }

    /**
     * Updates the volume level shown in the slider and the volume level text field
     * by requesting up to date values from the model
     */
    private void updateVolumeDisplay() {
        slider.setValue((double) model.getVolume());
        volumeLevel.setText(String.valueOf((int)slider.getValue()));
    }

    /**
     * Updates station name and track name text fields in the display by
     * requesting up to date values from the model.
     */
    private void updatePlaybackDisplay() {
        updateStationNameDisplay();
        updateTrackNameDisplay();
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
    // TODO: implement tag support in model. Update track name in real time
    private void updateTrackNameDisplay() {
        if (model.isPlaying()) {
            trackName.setText("Bob The Builder: Yes we can yeee haaa");
        }
        else {
            trackName.setText("");
        }
    }
}

