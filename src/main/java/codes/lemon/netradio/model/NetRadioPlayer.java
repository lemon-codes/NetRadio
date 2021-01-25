package codes.lemon.netradio.model;

import java.util.*;

// TODO: Reconsider overiding of equals and hashcode to only use ID.
//       Feels like a fragile way of doing things.

public class NetRadioPlayer implements RadioPlayer{

    private final StreamPlayer playback = new StreamPlayerGStreamer();
    private final StationManager stations = new StationManager();  // loads stations from last run
    private Station currentStation;

    public NetRadioPlayer() {
    }

    /**
     * Select the station to be played.
     * @param id the unique identifier for a station
     */
    @Override
    public void setStation(int id) {
        Station s = stations.getStation(id);
        if (s != null) {
            playback.setSource(s.getURI());
            currentStation = s;
            currentStation.markPlayed();
        }
        else {
            throw new IllegalArgumentException("invalid ID supplied");
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
        return stations.getAllStations();
    }

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station.
     *
     * @param name the name of the new station
     * @param uri  points to an audio source for the new station
     * @return the ID given to the newly added station
     */
    @Override
    public int addStation(String name, String uri) {
        uri = Objects.requireNonNull(uri);
        name = Objects.requireNonNull(name);
        return stations.addStation(name, uri);
    }

    /**
     * Permanently remove a station from the player.
     *
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    @Override
    public boolean removeStation(int id) {
        return stations.removeStation(id);
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
        stations.setFavourite(id, status);
    }

    /**
     * Searches through station details looking for the given search term.
     * Searches are case insensitive.
     *
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     * search term. An empty list is returned if no results are found.
     */
    @Override
    public List<Station> findStation(String searchTerm) {
        searchTerm = Objects.requireNonNull(searchTerm);
        return stations.findStation(searchTerm);
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
        return stations.getStation(id);
    }

    /**
     * Finishes up and frees resources
     */
    public void shutdown() {
        stop();
        stations.shutdown();
    }


    public static void main(String[] args) throws InterruptedException {
        RadioPlayer radio = new NetRadioPlayer();
        //radio.addStation("http://stream-al.planetradio.co.uk/clyde1.mp3", "Clyde1");
        //radio.addStation("https://www.freedesktop.org/software/gstreamer-sdk/data/media/sintel_trailer-480p.webm", "freedesktop");


        for (Station s : radio.getAllStations()) {
            System.out.println(s.getStationName());
        }


        int id = -1;
        for (Station s : radio.findStation("clyde")) {
            System.out.println(s.getStationName());
            id = s.getStationID();
        }
        radio.setStation(id);
        radio.play();
        Thread.sleep(10000);
        radio.setStationFavouriteStatus(id, true);
        System.out.println(radio.getStation(id).getStationName() + " fav status: " + radio.getStation(id).isFavourite());
        radio.setStation(1);
        Thread.sleep(5000);
        radio.setVolume(20);
        Thread.sleep(5000);
        radio.setVolume(100);
        radio.addStation("test2", "http://test.test2");
        Thread.sleep(5000);
        radio.removeStation(5);
        radio.shutdown();

    }
}
