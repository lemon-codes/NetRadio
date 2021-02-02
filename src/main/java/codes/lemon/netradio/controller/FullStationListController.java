package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;

import java.util.List;

public class FullStationListController extends AbstractStationListController{

    @Override
    protected List<Station> getStationsToDisplay() {
        return model.getAllStations();
    }
}