package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

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
    @FXML private Tab searchResultTab;
    @FXML private TableView<StationData> allStationsTable;
    @FXML private TableView<StationData> favouriteStationsTable;
    @FXML private TableView<StationData> mostPlayedStationsTable;
    @FXML private TableView<StationData> searchResultStationsTable;
    @FXML private TextField searchField;

    private static final ModelAdapter model = ModelAdapterImpl.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribeToModelEvents(this);
        // fill tables with station data from the model
        allStationsTable.getItems().setAll(stationToStationData(model.getAllStations()));
        favouriteStationsTable.getItems().setAll(stationToStationData(getFavouriteStations()));
        mostPlayedStationsTable.getItems().setAll(stationToStationData(getMostPlayedStations()));
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
     * Returns the 10 stations with the highest "played" count.
     * Stations are ordered most played to least played.
     * @return top 10 most played stations
     */
    private List<Station> getMostPlayedStations() {
        // sort all stations by playCount
        Comparator<Station> playCountComparator = Comparator.comparing(Station::getPlayCount);
        SortedSet<Station> sortedStations = new TreeSet<>(playCountComparator);
        sortedStations.addAll(model.getAllStations());

        // place top 10 (or less if stations.size < 10) most played in list to return
        List<Station> mostPlayedStations = new ArrayList<>();
        int stationCount = Math.min(sortedStations.size(), 10);
        for (int i=0; i<stationCount; i++) {
            mostPlayedStations.add(i, sortedStations.last());
            sortedStations.remove(mostPlayedStations.get(i));
        }
        return mostPlayedStations;
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
            convertedStations.add(new StationData(s.getStationID(), s.getStationName(), s.getURI()));
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
        // TODO: consider handling events
    }



}
