package codes.lemon.netradio.model;

import java.util.List;

// MODEL
public interface RadioPlayer {
    void play();
    void stop();
    void setStation(int id);
    void setVolume(double volume);
    Station getCurrentStation();
    List<Station> getAllStations();
    boolean addStation(String URI);
    boolean removeStation(int id);
    boolean addStationToFavourites(int id);
    boolean removeStationFromFavourites(int id);

    List<Station> findStation(String searchTerm);

    //void subscribe()

}
