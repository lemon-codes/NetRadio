package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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
    @FXML protected TableColumn<StationData, String> genreColumn;
    @FXML protected TableColumn<StationData, String> uriColumn;
    @FXML protected TableColumn<StationData, Boolean> favouriteColumn;

    protected final ModelAdapter model = ModelAdapterImpl.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // subscribe to be notified of changes to model state caused by other controllers
        model.subscribeToModelEvents(this);
        // ensure column width expands appropriately when table is resized
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        uriColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.35));
        genreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        favouriteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));

        // map columns to StationData field names which will be used to load values into the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        uriColumn.setCellValueFactory(new PropertyValueFactory<>("uri"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));

        // convert "favourite" boolean flags to CheckBox instances to be displayed in table
        favouriteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(favouriteColumn));

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
     * Handles mouse clicks when stations are clicked.
     * A single primary click sets the clicked station as the highlighted station.
     * A single primary click of the favourite checkbox toggles the favourite status
     * of the station on the row clicked.
     * A double primary click starts playback for the clicked station.
     * TODO: A secondary click shows extended station details.
     * @param mouseEvent a mouse click on a station
     */
    public void stationClicked(MouseEvent mouseEvent) {
        StationData clickedStation = getStationSelected();

        if (clickedStation != null) {   // ignore clicks on empty rows
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    // double click initiates playback of selected station
                    model.setStation(clickedStation.getIdAsInt());
                    model.play();
                }
                else if (mouseEvent.getClickCount() == 1) {
                    // single click highlights selected station
                    model.setHighlightedStation(clickedStation.getIdAsInt());

                    // if checkbox is clicked toggle favourite status of selected station
                    String intersectedNodeId = mouseEvent.getPickResult().getIntersectedNode().getId();
                    if (intersectedNodeId != null && intersectedNodeId.equals(favouriteColumn.getId())) {
                        int selectedRow = getRowSelected();
                        model.setStationFavouriteStatus(clickedStation.getIdAsInt(), !clickedStation.isFavourite());
                        // reset selected row after table updates. TableView may select a different row if previous
                        // row no longer exists.
                        tableView.getSelectionModel().select(selectedRow);

                        // update model with new selected table
                        StationData updatedSelectedStation = tableView.getSelectionModel().getSelectedItem();
                        if (updatedSelectedStation != null) {
                            model.setHighlightedStation(updatedSelectedStation.getIdAsInt());
                        }
                        else {
                            // no station selected in view
                            model.clearHighlightedStation();
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the row number (index) of the selected row.
     * Returns -1 if no row selected.
     * @return row number of selected row, else -1 if no row selected.
     */
    private int getRowSelected() {
        List<TablePosition> rows = tableView.getSelectionModel().getSelectedCells();
        if (rows.size() == 1) {
            return rows.get(0).getRow();
        }
        return -1;
    }

    /**
     * Returns the StationData instance representing the station selected in the table.
     * Returns null if no station is selected.
     * @return the station selected in the table, else null if none selected.
     */
    private StationData getStationSelected() {
        return tableView.getSelectionModel().getSelectedItem();
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

    /**
     * Handles key presses.
     * Currently listens for UP and DOWN arrow key presses to
     * allow the user to navigate stations in the table.
     * Listens for ENTER key which initiates playback of the selected
     * station.
     * @param keyEvent
     */
    public void keyReleased(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();
        if (key == KeyCode.UP || key == KeyCode.DOWN) {
            // highlighted station has changed, update the model. JavaFX TableView updates the view,
            model.setHighlightedStation(getStationSelected().getIdAsInt());
        }
        else if (key == KeyCode.ENTER) {
            model.setStation(getStationSelected().getIdAsInt());
            model.play();
        }
    }
}

