package codes.lemon.netradio.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;


/**
 * A radio player client. Stores station details and supports playback.
 * Clients can subscribe to real-time metadata updates (provided whenever
 * playback is occurring). Allows users to favourite, remove and add stations.
 */
public class NetRadioPlayer implements RadioPlayer{

    private final StreamPlayer playback = new StreamPlayerGStreamer();
    private final StationManager stations = new StationManager();  // loads stations from last run
    private Station currentStation;
    private int volume = RadioPlayer.MAX_VOLUME;

    public NetRadioPlayer() {
        setVolume(RadioPlayer.MAX_VOLUME);
        // Subscribe to stream metadata
        subscribeToTagUpdates();
    }

    /**
     * Subscribes to tag updates provided by the model. Tag updates include
     * real-time playback metadata. We use these real-time updates to update
     * the view, keeping the view and model consistent with one another.
     */
    private void subscribeToTagUpdates() {
        playback.getObservableMetadata().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // only update station details if they are derived from real-time playback
                // we do not want to reset station details when tags are reset on station change or playback stop.
                if (currentStation != null && getObservableMetadata().getStreamUri().equals(currentStation.getUri())
                        && playback.isPlaying()) {
                    String type = evt.getPropertyName();
                    switch (type) {
                        case ObservableMetadata.PROP_GENRE -> stations.setGenre(currentStation.getStationID(), (String) evt.getNewValue());
                        case ObservableMetadata.PROP_BITRATE -> stations.setBitrate(currentStation.getStationID(), (int) evt.getNewValue());
                        // TODO: consider other cases
                    }
                }
            }
        });
    }

    /**
     * Select the station to be played.
     * @param id the unique identifier for a station
     */
    @Override
    public void setStation(int id) {
        Station s = stations.getStation(id);
        if (s != null) {
            playback.setSource(s.getUri());
            currentStation = s;
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
            currentStation.markPlayed();
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
        volume = volumeLevel;
    }

    /**
     * Get the audio playback volume.
     * Volume is between MIN_VOLUME and MAX_VOLUME which at current
     * is 0 and 100 respectively.
     * @return Audio playback volume. 0 <= volumeLevel <= 100
     */
    public int getVolume() {
        return volume;
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
     * Returns the details of all stations as an immutable list.
     *
     * @return all Station instances in an immutable list
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
     * Check if audio is currently being played by the radio player.
     *
     * @return true if audio is playing, else false.
     */
    @Override
    public boolean isPlaying() {
        return playback.isPlaying();
    }

    /**
     * Returns an ObservableMetadata instance which contains stream metadata properties.
     * Clients can register a PropertyChangeListener with the ObservableMetadata
     * instance to be notified when any property is updated.
     * The ObservableMetadata class contains constants which a PropertyChangeListener
     * can use to identify which property has changed.
     *
     * @return an ObservableTag instance which contains up to data stream metadata.
     */
    @Override
    public ObservableMetadata getObservableMetadata() {
        return playback.getObservableMetadata();
    }

    /**
     * Finishes up and frees resources
     */
    public void shutdown() {
        stop();
        stations.shutdown();
    }
}
