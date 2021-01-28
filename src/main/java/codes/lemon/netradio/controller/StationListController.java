package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.InstanceFactory;
import codes.lemon.netradio.model.RadioPlayer;
import codes.lemon.netradio.model.Station;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StationListController implements Initializable {
    @FXML private TableView<StationData> defaultTableView;
    @FXML private TableColumn<StationData,String> idColumn;
    @FXML private TableColumn<StationData, String> nameColumn;
    @FXML private TableColumn<StationData, String> uriColumn;

    private final RadioPlayer radio = InstanceFactory.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // constructor parameters each refer to a field name in StationData
        idColumn.setCellValueFactory(new PropertyValueFactory<StationData, String>("stationId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        uriColumn.setCellValueFactory(new PropertyValueFactory<>("stationUri"));
        defaultTableView.getItems().setAll(getBasicStationData());
    }

    private List<StationData> getBasicStationData() {
        List<StationData> stationList = new ArrayList<>();
        for (Station s : radio.getAllStations()) {
            stationList.add(new StationData(s.getStationID(), s.getStationName(), s.getURI()));
        }
        return stationList;
    }
}

