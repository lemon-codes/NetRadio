package codes.lemon.netradio.model;

import java.util.List;

/**
 * A radio player client. Stores station details and supports playback.
 */
public interface RadioPlayer {
    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 1;

    /**
     * Select the station to be played.
     * @param id the unique identifier for a station
     */
    void setStation(int id);

    /**
     * Initiates playback of the currently set station.
     */
    void play();

    /**
     * Stops playback of the currently playing station.
     */
    void stop();

    /**
     * Sets the audio playback volume for the current stream stream
     * and future streams. Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * @param volumeLevel Audio playback volume. 0 <= volumeLevel <= 100
     */
    void setVolume(int volumeLevel);

    /**
     * Returns details of the current station as a Station instance.
     * @return the current station
     */
    Station getCurrentStation();

    /**
     * Returns the details of all stations.
     * @return all Station instances
     */
    List<Station> getAllStations();

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station.
     * @param URI points to an audio source for the new station
     * @param name the name of the new station
     * @return true if the station was successfully added, else false.
     */
    boolean addStation(String URI, String name);

    /**
     * Permanently remove a station from the player.
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    boolean removeStation(int id);

    /**
     * Sets the `Favourite` status of a given station. When true,
     * that station is considered a favourite. When false, the
     * station is no longer considered a favourite.
     * @param id the unique ID for a station
     * @param status true to mark as a favourite, else false
     */
    void setStationFavouriteStatus(int id, boolean status);

    /**
     * Searches through station details looking for the given search term.
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     *          search term.
     */
    List<Station> findStation(String searchTerm);

    /**
     * Get the specified stations details as a Station instance
     * @param id the unique ID for a station
     * @return the Station instance which represents the given id.
     */
    Station getStation(int id);

    //void subscribe()

}
