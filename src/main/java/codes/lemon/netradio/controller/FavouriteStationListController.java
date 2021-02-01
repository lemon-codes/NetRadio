package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.Station;

import java.util.ArrayList;
import java.util.List;

public class FavouriteStationListController extends AbstractStationListController{
    @Override
    protected List<Station> getStationsToDisplay() {
        List<Station> favourites = new ArrayList<>();
        for (Station s: model.getAllStations()) {
            if (s.isFavourite()) {
                favourites.add(s);
            }
        }
        return favourites;
    }
}
