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
class RecordingStream extends AbstractStream implements Recording {
    private final AudioFormat fileFormat;
    private final File fileName;

    public RecordingStream(URI source, File fileName, AudioFormat fileFormat) {
        super(Objects.requireNonNull(source));
        this.fileFormat = Objects.requireNonNull(fileFormat);
        this.fileName = Objects.requireNonNull(fileName);

        // start playback on initialisation. Cannot be restarted.
        super.play();
    }

    /**
     * Provide super class with a PlayBin instance capable of recording to disk
     *
     * @return a PlayBin instance that is suited to the child-classes use case
     */
     @Override
    PlayBin getPlayBin() {
         switch(fileFormat) {
             case MP3: return PlayBinFactory.buildRecordingPlayBin(fileName, fileFormat);
        }

        // should never happen since we use an enum to validate types
        throw new AssertionError("Invalid file format supplied");
    }


}
