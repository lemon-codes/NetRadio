package codes.lemon.netradio.model;

import java.beans.PropertyChangeListener;

/**
 * A basic client for playing audio streams over the internet.
 */
interface StreamPlayer {
    public static final double MIN_VOLUME = 0.0;
    public static final double MAX_VOLUME = 1.0;

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
    // void setEquiliser()
}
