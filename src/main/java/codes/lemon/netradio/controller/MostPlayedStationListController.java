package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;

import java.util.List;

public class MostPlayedStationListController extends AbstractStationListController{
    /**
     * Returns all previously played stations, ordered by most played.
     * @return most played stations
     */
    @Override
    protected List<Station> getStationsToDisplay() {
        // TODO: return most played. Need to load more stations first
        return model.getAllStations();
    }
}
