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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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

    /**
     * Handles mouse clicks when stations are clicked.
     * If a single primary click no action is taken.
     * A double primary click starts playback for the clicked station.
     * TODO: A secondary click shows extended station details.
     * @param mouseEvent a mouse click on a station
     */
    public void stationClicked(MouseEvent mouseEvent) {
        List<StationData> stationList = defaultTableView.getItems();
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                System.out.println("double click on station");
                List<TablePosition> rows = defaultTableView.getSelectionModel().getSelectedCells();
                assert(rows.size() == 1) : "rows.size()>1. multiple rows cannot be clicked at once";
                for (TablePosition row : rows) {
                    int rowIndex = row.getRow();
                    StationData clickedStation = stationList.get(rowIndex);
                    radio.setStation(clickedStation.getStationIdAsInt());
                    radio.play();
                }
            }
        }
    }
}

