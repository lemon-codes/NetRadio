package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.PlayBin;

import java.beans.PropertyChangeListener;
import java.util.Objects;

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
    private Element pipeline;
    private double volume = MAX_VOLUME;

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
     * @param uri uri pointing to an audio source
     */
    @Override
    public void setSource(String uri) {
        assert(uri != null) : "null uri supplied";
        tags.resetAllProperties();  // reset tags from previous source
        tags.setStreamUri(uri);

        boolean resumePlay = false;
        if (pipeline != null && pipeline.isPlaying()) {
            pipeline.stop();
            resumePlay = true;
        }

        // build a new pipeline with the nodes required to handle the new source
        // playbin argument instructs Gstreamer to build a pipeline with the appropriate
        // docoders/decrypters etc for the given URI
        pipeline = Gst.parseLaunch("playbin uri=" + uri);
        setVolume(volume); // restore previously set volume level

        // each pipeline has a bus. Connect listeners to new pipelines bus
        connectBusListeners(pipeline);
        //resumePlay = true;
        if (resumePlay) {
            pipeline.play();
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
        if (pipeline == null) {
            throw new IllegalStateException("no stream selected for playback");
        }
        pipeline.play();
    }

    /**
     * Stops audio playback from the currently set source.
     * If no source has been set, an IllegalStateException is thrown.
     * If the currently set source is not playing at the time stop() is called,
     * no action is taken since we are already in the desired state.
     */
    @Override
    public void stop() {
        if (pipeline == null) {
            throw new IllegalStateException("no stream selected for playback. Nothing to stop");
        }
        pipeline.stop();
        tags.resetAllProperties();
    }

    /**
     * Sets the audio playback volume for the current pipeline.
     * Volume must be in the range `MIN_VOLUME` to `MAX_VOLUME` (inclusive).
     * @param volumeLevel Audio playback volume. 0.0 <= volume <= 1.0
     */
    @Override
    public void setVolume(double volumeLevel) {

        if (volumeLevel < MIN_VOLUME || volumeLevel > MAX_VOLUME) {
            throw new IllegalArgumentException("volume must be in the range " +
                                                MIN_VOLUME + " to " + MAX_VOLUME +
                                                "(inclusive)");
        }
        volume = volumeLevel;
        if (pipeline instanceof PlayBin) {
            // should always be true since we initialise pipeline with the playbin argument
            PlayBin pb = (PlayBin)pipeline;
            pb.setVolume(volume);
            System.out.println("volume set to: " + volume);
        }
    }

    /**
     * Subscribe to be notified when stream tags are updated.
     * @param o observer
     */
    @Override
    public void subscribeToStreamTags(PropertyChangeListener o) {
        tags.addPropertyChangeListener(Objects.requireNonNull(o));
    }

    /**
     * Check if audio is currently being played by this.
     *
     * @return true if audio is playing, else false.
     */
    @Override
    public boolean isPlaying() {
        return (pipeline != null && pipeline.isPlaying());
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


    /**
     * Each pipeline has a bus which gstreamer uses to send messages.
     * By connecting listeners to this bus we can be receive these messages
     * and take appropriate action.
     * Messages may be informative, such as stream tags (title, bitrate, etc),
     * may indicate the end of the stream, or may indicate exceptions/errors
     * within gstreamer.
     * Tag messages are used to update the ObservableMetadata instances
     * @param pipe
     */
    private void connectBusListeners(Element pipe) {
        Bus bus = pipe.getBus();

        // lambda instead of anonymous inner class
        bus.connect((Bus.EOS) source -> {
            System.out.println("We have reached the end of the stream");
            // need pipeline.stop() to ensure window closes with video
            pipe.stop();  // sets the state to NULL
        });

        bus.connect(new Bus.ERROR() {

            @Override
            public void errorMessage(GstObject source, int i, String s) {
                System.out.println("Error: " + s + "\ti=" + i);
            }
        });

        // update the ObservabeTags instance with the latest tag properties as they arrive off the Bus.
        bus.connect(new Bus.TAG() {

            @Override
            public void tagsFound(GstObject source, TagList tagList) {
                updateTags(tagList);
            }
        });
    }

    /**
     * Updates the current ObservableTag instance with the supplied tag properties.
     * ObservableTag will update registered listeners.
     * @param tagList a list of new tags
     */
    private void updateTags(TagList tagList) {
        System.out.println("\n---------------------------------------------------");
        for (String key : tagList.getTagNames()) {
            System.out.println(key + " : " + tagList.getString(key, 0));
            switch (key) {
                case TagKeys.AUDIO_CODEC -> tags.setAudioCodec(tagList.getString(key, 0));
                case TagKeys.BITRATE -> tags.setBitrate(tagList.getNumber(key, 0).intValue());
                case TagKeys.CHANNEL_MODE -> tags.setChannelMode(tagList.getString(key, 0));
                case TagKeys.CITY -> tags.setCity(tagList.getString(key, 0));
                case TagKeys.CONTAINER_FORMAT -> tags.setContainerFormat(tagList.getString(key, 0));
                case TagKeys.COUNTRY -> tags.setCountry(tagList.getString(key, 0));
                case TagKeys.ENCODER -> tags.setEncoder(tagList.getString(key, 0));
                case TagKeys.ENCODER_VERSION -> tags.setEncoderVersion(tagList.getString(key, 0));
                case TagKeys.EXTENDED_COMMENT -> tags.setExtendedComment(tagList.getString(key, 0));
                case TagKeys.GENRE -> tags.setGenre(tagList.getString(key, 0));
                case TagKeys.HOMEPAGE -> tags.setHomepage(tagList.getString(key, 0));
                case TagKeys.NOMINAL_BITRATE -> tags.setNominalBitrate(tagList.getString(key, 0));
                case TagKeys.ORGANISATION -> tags.setOrganisation(tagList.getString(key, 0));
                case TagKeys.TITLE -> tags.setTitle(tagList.getString(key, 0));
                default -> System.out.println("INFO: Unhandled tag key -> " + key);
            }
        }
    }

    /**
     * Utility class which contains string constants used as keys by GStreamer to identify tag values.
     */
    private static class TagKeys {
        // Utility class used over EnumMap as the values should never change or require modification at run time
        private static final String TITLE = "title"; // track title
        private static final String GENRE = "genre"; // station genre
        private static final String ORGANISATION = "organization"; // station name
        private static final String EXTENDED_COMMENT = "extended-comment"; // server details
        private static final String CHANNEL_MODE = "channel-mode"; // eg joint-stereo
        //private static final String HAS_CRC = "has-crc";
        private static final String HOMEPAGE = "homepage"; // station web page
        private static final String AUDIO_CODEC = "audio-codec";
        private static final String ENCODER = "encoder";
        private static final String ENCODER_VERSION = "encoder_version";
        private static final String NOMINAL_BITRATE = "nominal-bitrate";
        private static final String BITRATE = "bitrate";
        //private static final String MINIMUM_BITRATE = "minimum-bitrate";
        //private static final String MAXIMUM_BITRATE = "maximum-bitrate";
        private static final String CONTAINER_FORMAT = "container-format";
        private static final String COUNTRY = "geo-location-country";
        private static final String CITY = "geo-location-city";

    }
}
