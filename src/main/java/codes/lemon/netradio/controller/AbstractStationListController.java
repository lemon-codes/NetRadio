package codes.lemon.netradio.controller;

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

public abstract class AbstractStationListController implements Initializable, ModelEventHandler {
    /* Any FXML views utilising this controller must fx:ids which match the following field names */
    @FXML protected TableView<StationData> tableView;
    @FXML protected TableColumn<StationData,String> idColumn;
    @FXML protected TableColumn<StationData, String> nameColumn;
    @FXML protected TableColumn<StationData, String> uriColumn;

    protected final ModelAdapter model = ModelAdapterImpl.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // subscribe to be notified of changes to model state caused by other controllers
        model.subscribeToModelEvents(this);
        // ensure column width expands appropriately when table is resized
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        uriColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));

        // fill table. Constructor parameters each refer to a field name in StationData
        // TODO: move to helper method which accepts any List<StationData>.
        idColumn.setCellValueFactory(new PropertyValueFactory<StationData, String>("stationId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        uriColumn.setCellValueFactory(new PropertyValueFactory<>("stationUri"));
        tableView.getItems().setAll(getBasicStationData());
    }

    /**
     * Handles mouse clicks when stations are clicked.
     * If a single primary click no action is taken.
     * A double primary click starts playback for the clicked station.
     * TODO: A secondary click shows extended station details.
     * @param mouseEvent a mouse click on a station
     */
    public void stationClicked(MouseEvent mouseEvent) {
        List<StationData> stationList = tableView.getItems();
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                List<TablePosition> rows = tableView.getSelectionModel().getSelectedCells();
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

    private List<StationData> getBasicStationData() {
        List<StationData> stationList = new ArrayList<>();
        for (Station s : getStationsToDisplay()) {
            stationList.add(new StationData(s.getStationID(), s.getStationName(), s.getURI()));
        }
        return stationList;
    }

    protected abstract List<Station> getStationsToDisplay();


}

