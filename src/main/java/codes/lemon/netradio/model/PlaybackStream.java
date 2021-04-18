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
class PlaybackStream extends AbstractStream implements Playback {

    public PlaybackStream(URI source) {
        super(Objects.requireNonNull(source));
    }

    public PlaybackStream(URI source, ObservableMetadata tags) {
        super(Objects.requireNonNull(source), Objects.requireNonNull(tags));

    }
    /**
     * Allow subclasses to supply PlayBin instances which have been configured
     * for a subclasses use case
     *
     * @return a PlayBin instance that is suited to the child-classes use case
     */
    @Override
    PlayBin getPlayBin() {
        return PlayBinFactory.buildPlaybackPlayBin();
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
}
