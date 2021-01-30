package codes.lemon.netradio.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private PlaybackController playbackController;
    @FXML private StationListController stationListController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
