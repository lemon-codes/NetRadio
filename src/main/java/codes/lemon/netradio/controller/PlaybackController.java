package codes.lemon.netradio.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.text.Text;
import codes.lemon.netradio.model.RadioPlayer;
import codes.lemon.netradio.model.InstanceFactory;

public class PlaybackController {
    private final RadioPlayer radio = InstanceFactory.getInstance();
    @FXML private Text status;
    @FXML private Slider slider;
    @FXML private Text volumeLevel;
    @FXML private Text stationName;
    @FXML private Text trackName;

    public void playPressed(ActionEvent actionEvent) {
        radio.play();
        stationName.setText(radio.getCurrentStation().getStationName());
        trackName.setText("Bob The Builder: Yes we can yeee haaa");
    }

    public void stopPressed(ActionEvent actionEvent) {
        radio.stop();
        stationName.setText("");
        trackName.setText("");
    }

    public void nextChannel(ActionEvent actionEvent) {
        radio.setStation(0);
        playPressed(null);
    }

    public void previousChannel(ActionEvent actionEvent) {
        radio.setStation(1);
        playPressed(null);
    }


    public void mouseReleased(MouseEvent mouseEvent) {
        System.out.println("mouseReleased: " + (int)slider.getValue());
        volumeLevel.setText(""+ (int)slider.getValue());
        radio.setVolume((int)slider.getValue());
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        System.out.println("mouseDragged: " + (int)slider.getValue());
        volumeLevel.setText(""+ (int)slider.getValue());
        radio.setVolume((int)slider.getValue());
    }

}

