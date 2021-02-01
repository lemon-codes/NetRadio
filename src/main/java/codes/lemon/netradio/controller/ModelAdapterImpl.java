package codes.lemon.netradio.controller;

import codes.lemon.netradio.model.InstanceFactory;
import codes.lemon.netradio.model.RadioPlayer;
import codes.lemon.netradio.model.Station;

import java.util.*;

class ModelAdapterImpl implements ModelAdapter{
    // Static implementation of singleton pattern
    // allow all controllers to safely access the same instance
    private static final ModelAdapter INSTANCE = new ModelAdapterImpl();

    /**
     * Return a singleton instance of this.
     * @return the only instance
     */
    public static ModelAdapter getInstance() {
        return INSTANCE;
    }

    // Instance implementation follows
    private final RadioPlayer model = InstanceFactory.getInstance();
    private final Set<ModelEventHandler> eventHandlers = new HashSet<>();
    private List<Station> searchResults = new ArrayList<>();


    private ModelAdapterImpl() {
        // Singleton
    }

    /**
     * Subscribe to be notified when other clients/controllers perform operations on the
     * model. Any time an operation is performed on the model an event is sent to all
     * subscribers containing the event type. Subscribers can then react appropriately,
     * updating any values in the view. This enables all clients to remain synchronised
     * with the model.
     *
     * @param handler a ModelEventHandler instance not already subscribed.
     */
    @Override
    public void subscribeToModelEvents(ModelEventHandler handler) {
        handler = Objects.requireNonNull(handler);
        // backed by a HashSet, won't allow duplicates
        eventHandlers.add(handler);
    }

    /**
     * Unsubscribe from model events.
     *
     * @param handler a ModelEventHandler instance which has previously subscribed.
     */
    @Override
    public void unsubscribeFromModelEvents(ModelEventHandler handler) {
        eventHandlers.remove(handler);
    }

    /**
     * Select the station to be played.
     * Triggers ModelEvent.STATION_CHANGED
     *
     * @param id the unique identifier for a station
     */
    @Override
    public void setStation(int id) {
        model.setStation(id);
        notifySubscribers(ModelEvent.STATION_CHANGED);
    }

    /**
     * Initiates playback of the currently set station.
     * Triggers ModelEvent.PLAYBACK_STARTED
     */
    @Override
    public void play() {
        model.play();
        notifySubscribers(ModelEvent.PLAYBACK_STARTED);
    }

    /**
     * Stops playback of the currently playing station.
     * Triggers ModelEvent.PLAYBACK_STOPPED
     */
    @Override
    public void stop() {
        model.stop();
        notifySubscribers(ModelEvent.PLAYBACK_STOPPED);
    }

    /**
     * Sets the audio playback volume for the current stream stream
     * and future streams. Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * Triggers ModelEvent.VOLUME_CHANGED
     *
     * @param volumeLevel Audio playback volume. 0 <= volumeLevel <= 100
     */
    @Override
    public void setVolume(int volumeLevel) {
        model.setVolume(volumeLevel);
        notifySubscribers(ModelEvent.VOLUME_CHANGED);
    }

    /**
     * Get the audio playback volume.
     * Volume is between MIN_VOLUME and MAX_VOLUME which at current
     * is 0 and 100 respectively.
     * @return Audio playback volume. 0 <= volumeLevel <= 100
     */
    @Override
    public int getVolume() {
        return model.getVolume();
    }

    /**
     * Returns details of the current station as a StationData instance.
     * StationData instances are
     *
     * @return the current station
     */
    @Override
    public Station getCurrentStation() {
        return model.getCurrentStation();
    }

    /**
     * Returns the details of all stations.
     *
     * @return all Station instances
     */
    @Override
    public List<Station> getAllStations() {
        return model.getAllStations();
    }

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station. The station will be
     * given a unique ID.
     * Triggers ModelEvent.STATION_ADDED
     *
     * @param name the name of the new station
     * @param uri  points to an audio source for the new station
     * @return the ID assigned to the newly added station
     */
    @Override
    public int addStation(String name, String uri) {
        int newId = model.addStation(name, uri);
        notifySubscribers(ModelEvent.STATION_ADDED);
        return newId;
    }

    /**
     * Permanently remove a station from the player.
     * Triggers ModelEvent.STATION_REMOVED
     *
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    @Override
    public boolean removeStation(int id) {
        boolean removed = model.removeStation(id);
        if (removed) {
            notifySubscribers(ModelEvent.STATION_REMOVED);
        }
        return removed;
    }

    /**
     * Sets the `Favourite` status of a given station. When true,
     * that station is considered a favourite. When false, the
     * station is no longer considered a favourite.
     * Triggers ModelEvent.STATION_EDITED
     *
     * @param id     the unique ID for a station
     * @param status true to mark as a favourite, else false
     */
    @Override
    public void setStationFavouriteStatus(int id, boolean status) {
        model.setStationFavouriteStatus(id, status);
        notifySubscribers(ModelEvent.STATION_EDITED);
    }

    /**
     * Searches through station details looking for the given search term.
     * Triggers ModelEvent.SEARCH_RESULTS_READY
     *
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     * search term. An empty list if no results.
     */
    @Override
    public List<Station> findStation(String searchTerm) {
        searchResults = model.findStation(searchTerm);
        notifySubscribers(ModelEvent.SEARCH_RESULTS_READY);
        return searchResults;
    }

    /**
     * Gets the result of the last search. This method is designed to enable
     * multiple clients/controllers to share the results of one clients search.
     * Clients subscribed to ModelEvents will be notified by a SEARCH_RESULTS_READY
     * event when a new result is ready, it can then be access using this method.
     *
     * @return the latest search results, else an empty list if none.
     */
    @Override
    public List<Station> getSearchResults() {
        return searchResults;
    }

    /**
     * Get the specified stations details as a Station instance
     *
     * @param id the unique ID for a station
     * @return the Station instance which represents the given id.
     */
    @Override
    public Station getStation(int id) {
        return model.getStation(id);
    }

    /**
     * Check if audio is currently being played by the radio player.
     *
     * @return true if audio is playing, else false.
     */
    @Override
    public boolean isPlaying() {
        return model.isPlaying();
    }

    /**
     * Finishes up and frees resources.
     */
    @Override
    public void shutdown() {
        model.shutdown();
        notifySubscribers(ModelEvent.SHUTDOWN);
    }

    /**
     * Notify all subscribers that a Model Event has been triggered.
     * @param event the event to be triggered
     */
    private void notifySubscribers(ModelEvent event) {
        for (ModelEventHandler subscriber : eventHandlers) {
            subscriber.handleEvent(event);
        }
    }
}