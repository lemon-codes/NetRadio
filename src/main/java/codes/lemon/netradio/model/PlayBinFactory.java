package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.elements.PlayBin;

import java.io.File;
import java.net.URI;

/**
 * This Factory may be used to obtain custom PlayBin pipelines containing elements
 * required for the clients use case.
 * Playback PlayBins can be constructed for live audio playback.
 * Recording PlayBins can be constructed to record audio to disk.
 * The components a PlayBin is constructed of is entirely dependant on both the audio format
 * broadcast by the audio source and the desired output type.
 */
class PlayBinFactory {
    public enum RECORDING_FILE_TYPE { MP3, FLAC, AAC_MKV, VORBIS_OGG, OPUS_OGG }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline sends the audio data to the systems
     * soundcard for live playback.
     * @param source audio source
     * @return a PlayBin capable of decoding the supplied audio source
     */
    public static PlayBin buildPlaybackPlayBin(URI source) {
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        PlayBin pb = new PlayBin("Playback");
        pb.setURI(source);
        return pb;
    }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline encodes the audio audio data to
     * match the requested file type before writing the data to disk using
     * the provided file name.
     * @param source audio source
     * @param fileName name of output file
     * @param fileType format to store
     * @return
     */
    public static PlayBin buildRecordingPlayBin(URI source, File fileName, RECORDING_FILE_TYPE fileType) {
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        // TODO: implement recording
        return new PlayBin("Recording");

    }

}
