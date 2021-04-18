package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.PlayBin;

import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An implementation of StreamPlayer using GStreamer as a back end
 * for audio decoding and playback.
 * This class makes use of GStreamers ability to build custom pipelines
 * on the fly with the required decoders to support playback of the current
 * audio source. This implementation is therefore able to handle any
 * stream types supported by GStreamer.
 * This implementation supports playback of one audio source at a time.
 * The volume level of the audio playback can be adjusted.
 * Clients can subscribe to be notified when stream tags arrive. Stream tags
 * contain details about streams current state, including the title of the
 * currently playing song.
 */
class StreamPlayerGStreamer implements StreamPlayer {

    private final ObservableMetadata tags = new ObservableMetadata();
    private Playback playback;
    private int volume = MAX_VOLUME;

    public StreamPlayerGStreamer() {
        // initialise GStreamer
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
    }

    /**
     * Accepts a URI pointing to an audio source. This URI will be used
     * as the source of audio for playback immediately.
     * The audio source can be of any format supported by GStreamer.
     * If a previous source is playing when this method is called, the
     * previous source is stopped and playback resumes immediately using the new
     * source. If nothing is playing when a source is set, playback will not start
     * until `play()` is called by the client.
     *
     * @param uri uri pointing to an audio source
     */
    @Override
    public void setSource(String uri) {
        assert(uri != null) : "null uri supplied";
        tags.resetAllProperties();  // reset tags from previous source
        tags.setStreamUri(uri);

        boolean resumePlay = false;
        if (playback != null && !playback.isStopped()) {
            playback.stop();
            resumePlay = true;
        }

        // build a new pipeline with the nodes required to handle the new source
        // playbin argument instructs Gstreamer to build a pipeline with the appropriate
        // docoders/decrypters etc for the given URI
        //pipeline = Gst.parseLaunch("playbin uri=" + uri);
        // TODO: clients will supply URI instance
        URI source = null;
        try {
            source = new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        playback = new PlaybackStream(source, tags);
        setVolume(volume); // restore previously set volume level

        if (resumePlay) {
            playback.play();
        }
        System.out.println("source set to " + uri);
    }

    /**
     * Begins audio playback using the currently set source.
     * If no source has been set, an IllegalStateException is thrown.
     * If the currently set source is already playing, no action is taken
     * since we are already in the desired state.
     */
    @Override
    public void play() {
        if (playback == null) {
            throw new IllegalStateException("no stream selected for playback");
        }
        playback.play();
    }

    /**
     * Stops audio playback from the currently set source.
     * If no source has been set, an IllegalStateException is thrown.
     * If the currently set source is not playing at the time stop() is called,
     * no action is taken since we are already in the desired state.
     */
    @Override
    public void stop() {
        if (playback == null) {
            throw new IllegalStateException("no stream selected for playback. Nothing to stop");
        }
        playback.stop();
        tags.resetAllProperties();
    }

    /**
     * Sets the audio playback volume for the current pipeline.
     * Volume must be in the range `MIN_VOLUME` to `MAX_VOLUME` (inclusive).
     *
     * @param volumeLevel Audio playback volume. 0.0 <= volume <= 1.0
     */
    @Override
    public void setVolume(int volumeLevel) {
        // should be checked already by NetRadioPlayer
        assert (volumeLevel >= Playback.MIN_VOLUME && volumeLevel <= Playback.MAX_VOLUME);
        volume = volumeLevel;
        if (playback != null) {
            playback.setVolume(volumeLevel);
        }
        System.out.println("volume set to: " + volume);
    }

    /**
     * Subscribe to be notified when stream tags are updated.
     *
     * @param pcl listener
     */
    @Override
    public void subscribeToStreamTags(PropertyChangeListener pcl) {
        Objects.requireNonNull(pcl);
        tags.addPropertyChangeListener(pcl);
        //tags.addPropertyChangeListener(Objects.requireNonNull(pcl));
        //listeners.add(pcl);
    }

    /**
     * Check if audio is currently being played by this.
     *
     * @return true if audio is playing, else false.
     */
    @Override
    public boolean isPlaying() {
        return (playback != null && !playback.isStopped());
    }

    /**
     * Returns an ObservableMetadata instance which contains stream metadata properties.
     * Clients can register a PropertyChangeListener with the ObservableMetadata
     * instance to be notified when any property is updated.
     * The ObservableMetadata class contains constants which a PropertyChangeListener
     * can use to identify which property has changed.
     *
     * @return an ObservableTag instance which contains up to data stream metadata.
     */
    @Override
    public ObservableMetadata getObservableMetadata() {
        return tags;
    }
}
