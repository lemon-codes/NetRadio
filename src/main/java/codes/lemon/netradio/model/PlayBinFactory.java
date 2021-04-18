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
    //public enum AUDIO_FORMAT { MP3, FLAC, AAC_MKV, VORBIS_OGG, OPUS_OGG }
    // TODO: implement support for other formats
    public enum AUDIO_FORMAT { MP3 }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline sends the audio data to the systems
     * soundcard for live playback.
     * @param source audio source
     * @return a PlayBin capable of decoding the supplied audio source
     */
    public static PlayBin buildPlaybackPlayBin(URI source) {
        // always check if Gstreamer is initialised since other components could uninitialise
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
     * match the requested audio format before writing the data to disk using
     * the provided file name.
     * @param source audio source
     * @param fileName name of output file
     * @param fileType audio format of output file
     * @return
     */
    public static PlayBin buildRecordingPlayBin(URI source, File fileName, AUDIO_FORMAT fileType) {
        // always check if Gstreamer is initialised since other components could uninitialise
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        // TODO: implement recording
        return new PlayBin("Recording");
    }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline will branch and duplicate the
     * audio data. One branch feeds the data into the systems sound card
     * for live audio playback. The other branch encodes the audio data
     * in the requested audio format and writes it to disk under the given
     * file name.
     * @param source audio source
     * @param fileName name of output file
     * @param fileType format of output file
     * @return
     */
    public static PlayBin buildRecordingPlaybackPlayBin(URI source, File fileName, AUDIO_FORMAT fileType) {
        // always check if Gstreamer is initialised since other components could uninitialise
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        // TODO: implement recording
        return new PlayBin("Recording");
    }

}
