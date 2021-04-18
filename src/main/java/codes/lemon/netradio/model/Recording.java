package codes.lemon.netradio.model;

/**
 * A Recording instance represents a stations audio stream which is being recorded.
 * Recordings begin recording upon initialisation.
 * The recording can be stopped at any time but cannot be restarted.
 * Live tag updates provided by the audio source can be monitored as ObservableMetadata.
 */
public interface Recording {
    /**
     * Stop the recording - cannot be restarted
     */
    void stop();

    /**
     * Check if the recording has been stopped.
     * @return true if stopped, else false
     */
    boolean isStopped();

    /**
     * Return an ObservableMetedata instance which can be used by clients
     * to receive tag updates broadcast by the audio source.
     * @return ObservableMetadata instance which receives tag updates from the audio source
     */
    ObservableMetadata getObservableMetadata();
}
