package codes.lemon.netradio.model;

/**
 * An object that supports audio playback.
 * Playback can be stopped and restarted.
 * Live tag updates provided by the audio source can be monitored through ObservableMetadata.
 */
interface Playback {
    int MIN_VOLUME = 0;
    int MAX_VOLUME = 100;

    /**
     * Start playback
     */
    void play();

    /**
     * Stop playback
     */
    void stop();

    /**
     * Check if playback has been stopped.
     * @return true if stopped, else false
     */
    boolean isStopped();

    /**
     * Gets the audio playback volume for the stream.
     * @return current playback volume level
     */
    int getVolume();

    /**
     * Sets the audio playback volume for the stream.
     * Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * @param volumeLevel Audio playback volume. {@code 0 <= volumeLevel <= 100}
     */
    void setVolume(int volumeLevel);



    /**
     * Return an ObservableMetedata instance which can be used by clients
     * to receive tag updates broadcast by the audio source.
     * @return ObservableMetadata instance which receives tag updates from the audio source
     */
    ObservableMetadata getObservableMetadata();
}
