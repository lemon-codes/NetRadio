package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.PlayBin;

import java.beans.PropertyChangeListener;

public class StreamPlayerGStreamer implements StreamPlayer{
    public static final double MAX_VOLUME = 1.0;
    public static final double MIN_VOLUME = 0.0;
    private Element pipeline;
    private double volume = MAX_VOLUME;

    public StreamPlayerGStreamer() {
        // initialise GStreamer
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
    }

    @Override
    public void setSource(String uri) {
        assert(uri != null) : "null uri supplied";

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

    @Override
    public void play() {
        if (pipeline == null) {
            throw new IllegalStateException("no stream selected for playback");
        }
        pipeline.play();
    }

    @Override
    public void stop() {
        if (pipeline == null) {
            throw new IllegalStateException("no stream selected for playback. Nothing to stop");
        }
        pipeline.stop();

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

    @Override
    public void subscribeToStreamTags(PropertyChangeListener o) {

    }


    /**
     * Each pipeline has a bus which gstreamer uses to send messages.
     * By connecting listeners to this bus we can be receive these messages
     * and take appropriate action.
     * Messages may be informative, such as stream tags (title, bitrate, etc),
     * may indicate the end of the stream, or may indicate exceptions/errors
     * within gstreamer
     * @param pipe
     */
    private void connectBusListeners(Element pipe) {
        Bus bus = pipe.getBus();

        // lambda instead of anonymous inner class
        bus.connect((Bus.EOS) source -> {
            System.out.println("We have reached the end of the stream");
            // need pipeline.stop() to ensure window closes with video
            pipe.stop();  // sets the state to NULL
            //pipeline.setState(State.NULL);
        });

        /*
        bus.connect(new Bus.EOS() {
            @Override
            public void endOfStream(GstObject gstObject) {
                System.out.println("We have reached the end of the stream");
                // need pipeline.stop() to ensure window closes with video
                pipe.stop();  // sets the state to NULL
                //pipeline.setState(State.NULL);
            }
        });
        */

        bus.connect(new Bus.ERROR() {

            @Override
            public void errorMessage(GstObject source, int i, String s) {
                System.out.println("LOLING Error: " + s + "\ti=" + i);
            }
        });

        bus.connect(new Bus.TAG() {

            @Override
            public void tagsFound(GstObject source, TagList tagList) {
                String title = null;
                try {
                    title = tagList.getString("title", 0);
                }
                catch (NullPointerException e) {
                    // title not available at this time. already null

                }
                if (title == null) {
                    System.out.println("Clyde 1");
                }
                else {
                    System.out.println("Title:" + title);
                }
                for (String tag : tagList.getTagNames()) {
                    int count = tagList.getValueCount(tag);
                    for (int i=0; i<count; i++) {
                        System.out.println("(" + tag + "," + i + "): " + tagList.getString(tag, i));

                    }
                }

            }
        });
    }
}
