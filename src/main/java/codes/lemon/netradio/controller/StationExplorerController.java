package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Controller for a station explorer. Allows station details to be browsed and searched by users.
 * Contains multiple tabs. Each tab contains a table filled with station details.
 * Any station entry in any table can be double clicked to initiate playback of that station.
 * This controller also provides search functionality. A TextField and Button are used to allow
 * users to search for stations. The models search functionality is used to gather results.
 * Results are displayed in a separate tab containing a table of search results. This Tab is
 * brought to the front whenever a search is performed.
 */
public class StationExplorerController implements Initializable, ModelEventHandler {


    @FXML private TabPane stationTabs;
    @FXML private Tab allStationsTab;
    @FXML private Tab favouriteStationsTab;
    @FXML private Tab mostPlayedStationsTab;
    @FXML private Tab searchResultTab;

    @FXML private TableView<StationData> allStationsTable;
    @FXML private TableView<StationData> favouriteStationsTable;
    @FXML private TableView<StationData> mostPlayedStationsTable;
    @FXML private TableView<StationData> searchResultStationsTable;
    @FXML private TextField searchField;

    private final ModelAdapter model = ModelAdapterImpl.getInstance();
    // Maps all tab IDs to the TableView instance each tab contains
    private Map<String, TableView<StationData>> tabIdToTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribeToModelEvents(this);
        // fill tables with station data from the model
        updateAllTableValues();

        // map tab IDs to the tables they contain for easy lookup later
        tabIdToTable = new HashMap<>();
        tabIdToTable.put(allStationsTab.getId(), allStationsTable);
        tabIdToTable.put(favouriteStationsTab.getId(), favouriteStationsTable);
        tabIdToTable.put(mostPlayedStationsTab.getId(), mostPlayedStationsTable);
        tabIdToTable.put(searchResultTab.getId(), searchResultStationsTable);
    }

    /**
     * Fills tables with up to date station data from the model
     */
    private void updateAllTableValues() {
        allStationsTable.getItems().setAll(stationToStationData(model.getAllStations()));
        favouriteStationsTable.getItems().setAll(stationToStationData(getFavouriteStations()));
        mostPlayedStationsTable.getItems().setAll(stationToStationData(getMostPlayedStations(10)));
        searchResultStationsTable.getItems().setAll(stationToStationData(model.getSearchResults()));
    }


    /**
     * Returns all Station instances which are marked as favourite stations
     * in the model
     * @return all stations marked as favourites
     */
    private List<Station> getFavouriteStations() {
        List<Station> favourites = new ArrayList<>();
        for (Station s : model.getAllStations()) {
            if (s.isFavourite()) {
                favourites.add(s);
            }
        }
        return favourites;
    }

    /**
     * Returns at most the top N stations with the highest "played" count.
     * Stations are ordered most played to least played.
     * If topN > stations.size() the returned list will contain stations.size() elements.
     * @param topN upper bound on number of stations to be returned.
     * @return top N most played stations
     */
    private List<Station> getMostPlayedStations(int topN) {
        List<Station> mostPlayed = model.getAllStations().stream()
                .sorted(Comparator.comparingInt(Station::getPlayCount).reversed())
                .limit(topN)
                .collect(toList());
        return mostPlayed;
    }


    /**
     * Converts Station instances from the model to StationData instances.
     * StationData instances have specially named fields to allow javaFX
     * to use those instances as value providers.
     * @param stations list of Station instances
     * @return a list of StationData instances which mirrors the list of Station
     *          instances provided.
     */
    private List<StationData> stationToStationData(List<Station> stations) {
        assert(stations != null) : "stations cannot be null";
        List<StationData> convertedStations = new ArrayList<>();
        for (Station s : stations) {
            convertedStations.add(new StationData(s.getStationID(), s.getStationName(), s.getUri(), s.getGenre(),
                                                    s.getPlayCount(), s.isFavourite(), s.getBitrate()));
        }
        return convertedStations;
    }


    /**
     * Uses the models search functionality to search for stations.
     * Result details are displayed in the search results table.
     * Opens the search results tab once the results table has been
     * populated with results.
     * @param actionEvent search
     */
    public void searchStations(ActionEvent actionEvent) {
        // Gather user input from TextField. Never returns null
        String searchTerm = searchField.getText();
        // search the model
        List<Station> results = model.findStation(searchTerm);
        // populate search results table with result data
        searchResultStationsTable.getItems().setAll(stationToStationData(results));
        // open search results tab to display the newly populated results table
        stationTabs.getSelectionModel().select(searchResultTab);
    }


    /**
     * Handle events in the model triggered when other components in the UI
     * alter the models state.
     * @param event an event in the model
     */
    @Override
    public void handleEvent(ModelAdapter.ModelEvent event) {
        switch(event) {
            case NEXT_HIGHLIGHTED_STATION_REQUESTED -> highlightNextStation();
            case PLAYBACK_STARTED -> {}
            case PLAYBACK_STOPPED -> {}
            case PREVIOUS_HIGHLIGHTED_STATION_REQUESTED -> highlightPreviousStation();
            case STATION_CHANGED -> {}
            case STATION_ADDED -> updateAllTableValues();
            case STATION_REMOVED -> updateAllTableValues();
            case STATION_EDITED -> updateAllTableValues();
            case SEARCH_RESULTS_READY -> {}
            case TAG_UPDATE -> {}
            case VOLUME_CHANGED -> {}
            case SHUTDOWN -> {}
        }
    }

    /**
     * Highlights the station which precedes the currently selected station in the
     * currently selected TableView.
     * If no station is selected, the last station in the currently selected TableView
     * is highlighted.
     */
    private void highlightPreviousStation() {
        TableView<StationData> selectedTable = tabIdToTable.get(stationTabs.getSelectionModel().getSelectedItem().getId());
        if (selectedTable != null) {
            int row = getRowSelected(selectedTable);
            if (row > 0) {
                // set previous station as currently highlighted station
                selectedTable.getSelectionModel().select(--row);
                model.setHighlightedStation(selectedTable.getSelectionModel().getSelectedItem().getIdAsInt());
            }
            else {
                // no station set. Start with the last station.
                selectedTable.getSelectionModel().select(selectedTable.getItems().size()-1);
                model.setHighlightedStation(selectedTable.getSelectionModel().getSelectedItem().getIdAsInt());
            }
        }
        else {
            // assertion error. Each tab should contain a table.
            // in the future this may change so an assertion error is not thrown.
        }

    }

    /**
     * Highlights the station which follows the currently selected station
     * in the currently selected TableView.
     * If no station is selected, the first station is selected in the currently
     * selected TableView.
     */
    private void highlightNextStation() {
        TableView<StationData> selectedTable = tabIdToTable.get(stationTabs.getSelectionModel().getSelectedItem().getId());
        if (selectedTable != null) {
            int row = getRowSelected(selectedTable); // -1 if none selected. If none selected we start at row 0
            if (row < selectedTable.getItems().size()-1) {
                // set next station as currently highlighted station
                selectedTable.getSelectionModel().select(++row);
                model.setHighlightedStation(selectedTable.getSelectionModel().getSelectedItem().getIdAsInt());
            }
        }

    }

    /**
     * Returns the row number (index) of the selected row.
     * Returns -1 if no row selected.
     * @param tableView the TableView whose selected row is returned
     * @return row number of selected row, else -1 if no row selected.
     */
    private int getRowSelected(TableView<StationData> tableView) {
        // tableView.getSelectionModel().getFocusedIndex() is not used as it returns 0 when none selected.
        List<TablePosition> rows = tableView.getSelectionModel().getSelectedCells();
        if (rows.size() == 1) {
            return rows.get(0).getRow();
        }
        return -1;
    }

}
