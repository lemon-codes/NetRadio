package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;

import javafx.event.ActionEvent;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class StationTableController implements Initializable, ModelEventHandler {
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
        idColumn.setCellValueFactory(new PropertyValueFactory<StationData, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        uriColumn.setCellValueFactory(new PropertyValueFactory<>("uri"));
        //tableView.getItems().setAll(getBasicStationData());
    }

    /**
     * Handles mouse clicks when stations are clicked.
     * A single primary click sets the clicked station as the highlighted station.
     * A double primary click starts playback for the clicked station.
     * TODO: A secondary click shows extended station details.
     * @param mouseEvent a mouse click on a station
     */
    public void stationClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                StationData clickedStation = getStationSelected();
                if (clickedStation != null) {
                    model.setStation(clickedStation.getIdAsInt());
                    model.play();
                }
            }
            else if (mouseEvent.getClickCount() == 1) {
                StationData clickedStation = getStationSelected();
                if (clickedStation != null) {
                    model.setHighlightedStation(clickedStation.getIdAsInt());
                }
            }
        }
    }

    /**
     * Returns the StationData instance representing the station selected in the table.
     * Returns null if no station is selected.
     * @return the station selected in the table, else null if none selected.
     */
    private StationData getStationSelected() {
        List<StationData> stationDataList = tableView.getItems();
        List<TablePosition> rows = tableView.getSelectionModel().getSelectedCells();
        if (rows.size() == 1) {
            int rowIndex = rows.get(0).getRow();
            return stationDataList.get(rowIndex);
        }
        else {
            // no station selected
            assert(rows.size() == 0) : "rows.size() > 1. multiple rows cannot be clicked at once";
            return null;
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

    /**
     * Set the stations who's values are displayed in the tableview.
     * @param stations list of stations to display
     */
    public void setStationsOnDisplay(List<Station> stations) {
        Objects.requireNonNull(stations);
        List<StationData> tableEntries = stationToStationData(stations);
        tableView.getItems().setAll(tableEntries);
    }

    /**
     * Convert Station instances from the model to StationData instances that can be processed
     * by JavaFX property value providers.
     * StationData and Station instances have a 1 to 1 relationship.
     * @param stations list of Station instances
     * @return list of StationData instances with the same values as the Station instances
     */
    private static List<StationData> stationToStationData(List<Station> stations) {
        assert(stations != null) : "stations cannot be null";
        List<StationData> convertedStations = new ArrayList<>();
        for (Station s : stations) {
            convertedStations.add(new StationData(s.getStationID(), s.getStationName(), s.getUri(), s.getGenre(),
                                                    s.getPlayCount(), s.isFavourite(), s.getBitrate()));
        }
        return convertedStations;
    }

    /**
     * Get the station instance from the model corresponding to the provided
     * StationData instance.
     * @param stationData a StationData instance representing a Station from the model
     * @return Station instance from the model
     */
    private Station getStationFromModel(StationData stationData) {
        assert(stationData != null) : "stationData cannot be null";
        return model.getStation(stationData.getIdAsInt());
    }

    /**
     * Mark selected station as a favourite in the model.
     * If the station is already a favourite, it's favourite status is removed.
     * @param actionEvent
     */
    public void toggleFavouriteStatus(ActionEvent actionEvent) {
        StationData selectedStationData = getStationSelected();
        if (selectedStationData != null) {
            Station selectedStation = getStationFromModel(selectedStationData);
            model.setStationFavouriteStatus(selectedStation.getStationID(), !selectedStation.isFavourite());
        }
    }

    /**
     * Show all details available from the model about the selected station.
     * @param actionEvent
     */
    public void showDetails(ActionEvent actionEvent) {
        // TODO: construct fxml view to display station details
    }

    /**
     * Remove the selected station from the model
     * @param actionEvent
     */
    public void removeStation(ActionEvent actionEvent) {
        StationData selectedStationData = getStationSelected();
        if (selectedStationData != null) {
            model.removeStation(selectedStationData.getIdAsInt());
        }
    }
}

