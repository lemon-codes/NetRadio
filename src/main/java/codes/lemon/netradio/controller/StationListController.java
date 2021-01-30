package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.InstanceFactory;
import codes.lemon.netradio.model.RadioPlayer;
import codes.lemon.netradio.model.Station;

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

public class StationListController implements Initializable, ModelEventHandler {
    @FXML private TableView<StationData> defaultTableView;
    @FXML private TableColumn<StationData,String> idColumn;
    @FXML private TableColumn<StationData, String> nameColumn;
    @FXML private TableColumn<StationData, String> uriColumn;

    private final ModelAdapter model = ModelAdapterImpl.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // subscribe to be notified of changes to model state caused by other controllers
        model.subscribeToModelEvents(this);
        // ensure column width expands appropriately when table is resized
        idColumn.prefWidthProperty().bind(defaultTableView.widthProperty().multiply(0.2));
        nameColumn.prefWidthProperty().bind(defaultTableView.widthProperty().multiply(0.3));
        uriColumn.prefWidthProperty().bind(defaultTableView.widthProperty().multiply(0.5));

        // fill table. Constructor parameters each refer to a field name in StationData
        // TODO: move to helper method which accepts any List<StationData>.
        idColumn.setCellValueFactory(new PropertyValueFactory<StationData, String>("stationId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        uriColumn.setCellValueFactory(new PropertyValueFactory<>("stationUri"));
        defaultTableView.getItems().setAll(getBasicStationData());
    }

    private List<StationData> getBasicStationData() {
        List<StationData> stationList = new ArrayList<>();
        for (Station s : model.getAllStations()) {
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
                List<TablePosition> rows = defaultTableView.getSelectionModel().getSelectedCells();
                assert(rows.size() == 1) : "rows.size()>1. multiple rows cannot be clicked at once";
                for (TablePosition row : rows) {
                    int rowIndex = row.getRow();
                    StationData clickedStation = stationList.get(rowIndex);
                    model.setStation(clickedStation.getStationIdAsInt());
                    model.play();
                }
            }
        }
    }


    /**
     * TODO: handle events
     * @param event
     */
    @Override
    public void handleEvent(ModelAdapter.ModelEvent event) {
        switch(event) {
            case PLAYBACK_STARTED:
            case PLAYBACK_STOPPED:
            case STATION_CHANGED:  // TODO: make station selected on list?
            case STATION_ADDED:  // TODO: update values in tables
            case STATION_REMOVED:
            case STATION_EDITED:
            case SEARCH_RESULTS_READY:
            case TAG_UPDATE:
            case VOLUME_CHANGED:
            case SHUTDOWN:
                break;
        }

    }


}

