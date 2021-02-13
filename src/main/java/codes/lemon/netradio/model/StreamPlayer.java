package codes.lemon.netradio.model;

import java.beans.PropertyChangeListener;

/**
 * A basic client for playing audio streams over the internet.
 * Clients manually set the stream source. Clients can subscribe
 * to stream metadata updates.
 */
interface StreamPlayer {
    double MIN_VOLUME = 0.0;
    double MAX_VOLUME = 1.0;

    /**
     * Set the source URI where audio will be streamed from.
     * @param uri uri pointing to an audio source
     */
    void setSource(String uri);

    /**
     * Begins audio playback using the currently set source.
     */
    void play();

    /**
     * Stops the current audio playback
     */
    void stop();

    /**
     * Sets the audio playback volume for the current stream stream
     * and future streams.
     * @param volumeLevel Audio playback volume
     */
    void setVolume(double volumeLevel);

    /**
     * Subscribe to be notified when stream tags are updated.
     * @param o observer
     */
    void subscribeToStreamTags(PropertyChangeListener o);

    /**
     * Check if audio is currently being played by this.
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

    // void setEquiliser()
}
