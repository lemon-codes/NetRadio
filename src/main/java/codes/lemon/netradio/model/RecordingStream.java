package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.elements.PlayBin;

import java.io.File;
import java.net.URI;
import java.util.Objects;

/**
 * A Recording instance represents a stations audio stream which is being recorded.
 * Recordings begin recording upon initialisation.
 * The recording can be stopped at any time but cannot be restarted.
 * Live tag updates provided by the audio source can be monitored as ObservableMetadata.
 * This implementation makes use of the Gstreamer library for audio processing.
 */
class RecordingStream implements Recording {
    private final GStreamerStream stream;

    public RecordingStream(URI source, File fileName, AudioFormat fileFormat) {
        Objects.requireNonNull(fileFormat);
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(source);

        // construct playbin capable of outputing the desired format to the desired file
        PlayBin playBin = PlayBinFactory.buildRecordingPlayBin(fileName, fileFormat);
        playBin.setURI(source);

        // GStreamerStream encapsulates metadata tag functionality
        stream = new GStreamerStream(playBin);

        // manually set URI in metadata to ensure it matches the source URI
        stream.getObservableMetadata().setStreamUri(source.toASCIIString());

        // start playback on initialisation. Cannot be restarted.
        stream.play();
    }

    /**
     * Stop the recording - cannot be restarted
     */
    @Override
    public void stop() {
        stream.stop();
    }

    /**
     * Check if the recording has been stopped.
     *
     * @return true if stopped, else false
     */
    @Override
    public boolean isStopped() {
        return stream.isStopped();
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
