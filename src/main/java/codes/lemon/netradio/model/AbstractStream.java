package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.elements.PlayBin;

import java.net.URI;

/**
 * This class supports basic stream functionality and takes care of asynchronous tag
 * updates by encapsulating that functionality in a PropertyChangeListener.
 * Classes which extend this abstract class may supply their own customised PlayBin instance.
 */
abstract class AbstractStream {
    protected PlayBin stream = getPlayBin();
    protected ObservableMetadata tags = new ObservableMetadata();

    public AbstractStream(URI source) {
        stream.setURI(source);
    }

    /**
     * Start the stream
     */
    public void play() {
        stream.play();
    }

    /**
     * Stop the stream
     */
    public void stop() {
        stream.stop();
    }

    /**
     * Check if the stream has been stopped.
     * @return true if stopped, else false
     */
    public boolean isStopped() {
        return !stream.isPlaying();
    }


    /**
     * Allow subclasses to supply PlayBin instances which have been configured
     * for a subclasses use case
     * @return a PlayBin instance that is suited to the child-classes use case
     */
    abstract PlayBin getPlayBin();

    /**
     * Returns an ObservableMetadata instance which contains stream metadata properties.
     * Clients can register a PropertyChangeListener with the ObservableMetadata
     * instance to be notified when any property is updated.
     * The ObservableMetadata class contains constants which a PropertyChangeListener
     * can use to identify which property has changed.
     *
     * @return an ObservableTag instance which contains up to data stream metadata.
     */
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
            // TODO: add tag support for errors

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

        // Streams stop sending TITLE tag when the previous TITLE tag expires and
        // there is no replacement yet. We reset the title when this happens.
        if (!tagList.getTagNames().contains(TagKeys.TITLE)) {
            tags.setTitle("");
        }
    }

    /**
     * Utility class which contains string constants used as keys by GStreamer to identify tag values.
     * TODO: consider using Enum with getTag() method.
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
