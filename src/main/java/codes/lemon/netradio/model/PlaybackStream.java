package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.elements.PlayBin;

import java.net.URI;
import java.util.Objects;

/**
 * An object that supports audio playback.
 * Playback can be stopped and restarted.
 * Live tag updates provided by the audio source can be monitored through ObservableMetadata.
 * This implementation makes use of the Gstreamer library for audio processing.
 */
class PlaybackStream implements Playback {
    private final GStreamerStream stream;

    public PlaybackStream(URI source) {
        Objects.requireNonNull(source);

        // construct a playbin capable of playing audio through the systems sound card
        PlayBin playBin = PlayBinFactory.buildPlaybackPlayBin();
        playBin.setURI(source);

        // GStreamerStream encapsulates metadata tag functionality
        stream = new GStreamerStream(playBin);
        // manually set URI in metadata to ensure it matches the source URI
        stream.getObservableMetadata().setStreamUri(source.toASCIIString());
    }

    public PlaybackStream(URI source, ObservableMetadata tags) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(tags);

        // construct a playbin capable of playing audio through the systems sound card
        PlayBin playBin = PlayBinFactory.buildPlaybackPlayBin();
        playBin.setURI(source);

        // GStreamerStream encapsulates metadata tag functionality. NOTE custom tags supplied
        stream = new GStreamerStream(playBin, tags);
        // manually set URI in metadata to ensure it matches the source URI
        stream.getObservableMetadata().setStreamUri(source.toASCIIString());

    }

    /**
     * Start playback
     */
    @Override
    public void play() {
        stream.play();
    }

    /**
     * Stop playback
     */
    @Override
    public void stop() {
        stream.stop();
    }

    /**
     * Check if playback has been stopped.
     *
     * @return true if stopped, else false
     */
    @Override
    public boolean isStopped() {
        return stream.isStopped();
    }

    /**
     * Gets the audio playback volume for the stream.
     *
     * @return current playback volume level
     */
    @Override
    public int getVolume() {
        Double volume = stream.getVolume()*100;
        return volume.intValue();
    }

    /**
     * Sets the audio playback volume for the stream.
     * Volume must be between(inclusive) MIN_VOLUME
     * and MAX_VOLUME which at current is 0 and 100 respectively.
     * @param volumeLevel Audio playback volume. {@code 0 <= volumeLevel <= 100}
     */
    @Override
    public void setVolume(int volumeLevel) {
        if (volumeLevel < MIN_VOLUME || volumeLevel > MAX_VOLUME) {
            throw new IllegalArgumentException("volumeLevel out of range");
        }
        // playbin has a volume range of 0.0 to 1.0
        stream.setVolume(volumeLevel/100.0);
    }

    /**
     * Return an ObservableMetedata instance which can be used by clients
     * to receive tag updates broadcast by the audio source.
     *
     * @return ObservableMetadata instance which receives tag updates from the audio source
     */
    @Override
    public ObservableMetadata getObservableMetadata() {
        return stream.getObservableMetadata();
    }
}
