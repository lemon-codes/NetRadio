package codes.lemon.netradio.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

// TODO: Reconsider overiding of equals and hashcode to only use ID.
//       Feels like a fragile way of doing things.

public class NetRadioPlayer implements RadioPlayer{
    //public static final int MIN_VOLUME = 0;
    //public static final int MAX_VOLUME = 100;
    private final StreamPlayer playback = new StreamPlayerGStreamer();
    private List<Station> stations = new LinkedList<>();  // TODO: consider alternatives, including sorted list
    private Station currentStation;
    private int currentID = 0; //TODO: replace with functioning ID generator

    public NetRadioPlayer() {
        // fill stations
    }

    /**
     * Select the station to be played.
     * @param id the unique identifier for a station
     */
    @Override
    public void setStation(int id) {
        for (Station station : stations) {
            if (station.getStationID() == id) {
                playback.setSource(station.getURI());
                currentStation = station;
            }
        }
    }

    /**
     * Initiates playback of the currently set station.
     */
    @Override
    public void play() {
        if (currentStation != null) {
            playback.play();
        }
    }

    /**
     * Stops playback of the currently playing station.
     */
    @Override
    public void stop() {
        if (currentStation != null) {
            playback.stop();
        }

    }

    /**
     * Sets the audio playback volume for the current stream stream
     * and future streams. Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * @param volumeLevel Audio playback volume. 0 <= volumeLevel <= 100
     */
    @Override
    public void setVolume(int volumeLevel) {
        if (volumeLevel < MIN_VOLUME || volumeLevel > MAX_VOLUME) {
            throw new IllegalArgumentException("volumeLevel out of range");
        }
        // StreamPlayer has a volume range of 0.0 to 1.0
        playback.setVolume(volumeLevel/100.0);

    }

    /**
     * Returns details of the current station as a Station instance.
     *
     * @return the current station
     */
    @Override
    public Station getCurrentStation() {
        return currentStation;
    }

    /**
     * Returns the details of all stations.
     *
     * @return all Station instances
     */
    @Override
    public List<Station> getAllStations() {
        return stations;
    }

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station.
     *
     * @param uri  points to an audio source for the new station
     * @param name the name of the new station
     * @return true if the station was successfully added, else false.
     */
    @Override
    public boolean addStation(String uri, String name) {
        uri = Objects.requireNonNull(uri);
        name = Objects.requireNonNull(name);
        Station newStation = new RadioStation(currentID, name, uri);
        if (stations.contains(newStation)) {
            // station with this ID already exists
            return false;
        }
        stations.add(newStation);
        return true;
    }

    /**
     * Permanently remove a station from the player.
     *
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    @Override
    public boolean removeStation(int id) {
        Iterator<Station> it = stations.iterator();
        while (it.hasNext()) {
            Station s = it.next();
            if (s.getStationID() == id) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the `Favourite` status of a given station. When true,
     * that station is considered a favourite. When false, the
     * station is no longer considered a favourite.
     *
     * @param id     the unique ID for a station
     * @param status true to mark as a favourite, else false
     */
    @Override
    public void setStationFavouriteStatus(int id, boolean status) {
        Station s = getStation(id);
        if (s != null) {
            s.setFavourite(status);
        }

    }

    /**
     * Searches through station details looking for the given search term.
     *
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     * search term.
     */
    @Override
    public List<Station> findStation(String searchTerm) {
        return null;
        // TODO: Decide exactly which fields to include in search
    }

    /**
     * Get the specified stations details as a Station instance
     *
     * @param id the unique ID for a station
     * @return the Station instance which represents the given id,
     *         else null.
     */
    @Override
    public Station getStation(int id) {
        for (Station s : stations) {
            if (s.getStationID() == id) {
                return s;
            }
        }
        return null;
    }
}
