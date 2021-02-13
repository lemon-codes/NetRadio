package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.ObservableMetadata;
import codes.lemon.netradio.model.Station;

import java.util.List;

/**
 * This interface acts as an adapter for the model. It allows all controllers to subscribe
 * to model events which are fired any time a controller interacts with the model.
 * Each controller can then perform any required operations to synchronise state with the
 * model. This enables a form of passive communication for Controllers. Controllers can
 * communicate with each other without knowing about the existence or implementation of
 * other Controllers. This ensures a loose coupling between Controllers while enabling them
 * to synchronise with one another.
 * This interface offers access to model data as StationData instances. JavaFX requires field
 * names to match valuePropertyFactory IDs to add and extract values from the view.
 * Using instances from the model would create an intrinsic coupling between the view and
 * model field names. StationData takes care of this and offers access to the field names
 * it used as public static fields.
 */
// TODO: Replace all Station return types with StationData
public interface ModelAdapter {
    enum ModelEvent {
        PLAYBACK_STARTED,
        PLAYBACK_STOPPED,
        STATION_CHANGED,
        STATION_ADDED,
        STATION_REMOVED,
        STATION_EDITED,
        SEARCH_RESULTS_READY,
        TAG_UPDATE,
        VOLUME_CHANGED,
        SHUTDOWN,
        STATION_HIGHLIGHTED
    }

    /**
     * Subscribe to be notified when other clients/controllers perform operations on the
     * model. Any time an operation is performed on the model an event is sent to all
     * subscribers containing the event type. Subscribers can then react appropriately,
     * updating any values in the view. This enables all clients to remain synchronised
     * with the model.
     * @param eventHandler a ModelEventHandler instance not already subscribed.
     */
    void subscribeToModelEvents(ModelEventHandler eventHandler);

    /**
     * Unsubscribe from model events.
     * @param eventHandler a ModelEventHandler instance which has previously subscribed.
     */
    void unsubscribeFromModelEvents(ModelEventHandler eventHandler);

    /**
     * Select the station to be played.
     * Triggers ModelEvent.STATION_CHANGED
     * @param id the unique identifier for a station
     */
    void setStation(int id);

    /**
     * Initiates playback of the currently set station.
     * Triggers ModelEvent.PLAYBACK_STARTED
     */
    void play();

    /**
     * Stops playback of the currently playing station.
     * Triggers ModelEvent.PLAYBACK_STOPPED
     */
    void stop();

    /**
     * Sets the audio playback volume for the current stream stream
     * and future streams. Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * Triggers ModelEvent.VOLUME_CHANGED
     * @param volumeLevel Audio playback volume. 0 <= volumeLevel <= 100
     */
    void setVolume(int volumeLevel);

    /**
     * Get the audio playback volume.
     * Volume is between MIN_VOLUME and MAX_VOLUME which at current
     * is 0 and 100 respectively.
     * @return Audio playback volume. 0 <= volumeLevel <= 100
     */
    int getVolume();

    /**
     * Returns details of the current station as a StationData instance.
     * StationData instances are
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
     * URI pointing to an audio source for that station. The station will be
     * given a unique ID.
     * Triggers ModelEvent.STATION_ADDED
     *
     * @param name the name of the new station
     * @param uri points to an audio source for the new station
     * @return the ID assigned to the newly added station
     */
    int addStation(String name, String uri);

    /**
     * Permanently remove a station from the player.
     * Triggers ModelEvent.STATION_REMOVED
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    boolean removeStation(int id);

    /**
     * Sets the `Favourite` status of a given station. When true,
     * that station is considered a favourite. When false, the
     * station is no longer considered a favourite.
     * Triggers ModelEvent.STATION_EDITED
     * @param id the unique ID for a station
     * @param status true to mark as a favourite, else false
     */
    void setStationFavouriteStatus(int id, boolean status);

    /**
     * Searches through station details looking for the given search term.
     * Triggers ModelEvent.SEARCH_RESULTS_READY
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     *          search term.
     */
    List<Station> findStation(String searchTerm);

    /**
     * Gets the result of the last search. This method is designed to enable
     * multiple clients/controllers to share the results of one clients search.
     * Clients subscribed to ModelEvents will be notified by a SEARCH_RESULTS_READY
     * event when a new result is ready, it can then be access using this method.
     * @return the latest search results
     */
    List<Station> getSearchResults();

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
     * Returns the most recently highlighted station.
     * @return the highlighted station.
     */
    Station getHighlightedStation();


    /**
     * Sets a station as being highlighted. This has no effect on the model.
     * Calls to this method trigger a HIGHLIGHTED_STATION_CHANGED event which
     * can be listened for by any client. The station instance can be retrieved by a
     * call to `getHighlightedStation()`.
     * @param highlightedStationId ID of station which is highlighted
     */
    void setHighlightedStation(int highlightedStationId);

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