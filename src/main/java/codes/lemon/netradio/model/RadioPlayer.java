package codes.lemon.netradio.model;

import java.util.List;

/**
 * A radio player client. Stores station details and supports playback.
 * Clients can subscribe to real-time metadata updates (provided whenever
 * playback is occurring). Allows users to favourite, remove and add stations.
 */
public interface RadioPlayer {
    int MIN_VOLUME = 0;
    int MAX_VOLUME = 100;

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
     * Sets the audio playback volume for the current station
     * and future stations. Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * @param volumeLevel Audio playback volume. {@code 0 <= volumeLevel <= 100}
     */
    void setVolume(int volumeLevel);

    /**
     * Get the audio playback volume.
     * Volume is between MIN_VOLUME and MAX_VOLUME which at current
     * is 0 and 100 respectively.
     * @return Audio playback volume. {@code 0 <= volumeLevel <= 100}
     */
    int getVolume();

    /**
     * Returns details of the current station as a Station instance.
     * @return the current station
     */
    Station getCurrentStation();

    /**
     * Returns the details of all stations in an immutable list
     * @return all Station instances in an immutable list
     */
    List<Station> getAllStations();

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station. The station will be
     * given a unique ID.
     *
     * @param name the name of the new station
     * @param uri points to an audio source for the new station
     * @return the ID assigned to the newly added station
     */
    int addStation(String name, String uri);

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

    /**
     * Check if audio is currently being played by the radio player.
     * @return true if audio is playing, else false.
     */
    boolean isPlaying();

    /**
     * Returns an ObservableMetadata instance which contains stream metadata properties.
     * Clients can register a PropertyChangeListener with the ObservableMetadata
     * instance to be notified when any property is updated.
     * The ObservableMetadata class contains constants which a PropertyChangeListener
     * can use to identify which property has changed.
     * @return an ObservableTag instance which contains up to data stream metadata.
     */
    ObservableMetadata getObservableMetadata();

    /**
     * Finishes up and frees resources.
     */
    void shutdown();



}
