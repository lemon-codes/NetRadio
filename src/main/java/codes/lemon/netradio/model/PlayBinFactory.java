package codes.lemon.netradio.model;

import org.freedesktop.gstreamer.*;
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
    //public enum AUDIO_FORMAT { MP3 }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline sends the audio data to the systems
     * soundcard for live playback.
     * @return a PlayBin capable of decoding the supplied audio source
     */
    public static PlayBin buildPlaybackPlayBin() {
        // always check if Gstreamer is initialised since other components could uninitialise
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        return new PlayBin("Playback");
    }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline encodes the audio audio data to
     * match the requested audio format before writing the data to disk using
     * the provided file name.
     * @param fileName name of output file
     * @param fileFormat audio format of output file
     * @return a PlayBin which stores the audio on disk.
     */
    public static PlayBin buildRecordingPlayBin(File fileName, AudioFormat fileFormat) {
        // always check if Gstreamer is initialised since other components could uninitialise
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }

        PlayBin pb = new PlayBin("Recording");
        switch (fileFormat) {
            case MP3 -> pb.setAudioSink(buildMP3DiskSink());
        }
        return pb;
    }


    /**
     * Builds a pipeline which can be used as an audio sink.
     * This implementation records playback to disk as an mp3 file.
     * // TODO: works perfectly
     * @return an audio sink which writes data to disk in mp3 format
     */
    private static Bin buildMP3DiskSink() {
        /*
           source -> playbin -> queue -> audioconvert -> lamemp3enc -> id3v2mux -> fileSink
         */
        Bin multipleAudioSinkBin = new Bin();

        Element diskQueue = ElementFactory.make("queue", "diskQueue");
        Element audioConverter = ElementFactory.make("audioconvert", "audioConverter");
        Element mp3Encoder = ElementFactory.make("lamemp3enc", "mp3Encoder");
        // Adds an ID3v2 header to the beginning of MP3 files using taglib
        Element mp3MetadataFormatter = ElementFactory.make("id3v2mux", "mp3MetadataFormatter");
        Element diskSink = ElementFactory.make("filesink", "diskSink");

        // TODO: use client supplied out file
        diskSink.set("location", "./output.mp3");

        multipleAudioSinkBin.add(diskQueue);
        multipleAudioSinkBin.add(audioConverter);
        multipleAudioSinkBin.add(mp3Encoder);
        multipleAudioSinkBin.add(mp3MetadataFormatter);
        multipleAudioSinkBin.add(diskSink);

        GhostPad gPad = new GhostPad("sink", PadDirection.SINK);
        gPad.setTarget(diskQueue.getSinkPads().get(0));
        multipleAudioSinkBin.addPad(gPad);

        // diskQueue -> audioconvert -> mp3encode -> mp3formatter -> diskSink
        diskQueue.link(audioConverter);
        audioConverter.link(mp3Encoder);
        mp3Encoder.link(mp3MetadataFormatter);
        mp3MetadataFormatter.link(diskSink);
        return multipleAudioSinkBin;
    }

    /**
     * Builds a pipeline which can decode the provided audio source.
     * Once decoded/processed this pipeline will branch and duplicate the
     * audio data. One branch feeds the data into the systems sound card
     * for live audio playback. The other branch encodes the audio data
     * in the requested audio format and writes it to disk under the given
     * file name.
     * @param fileName name of output file
     * @param fileFormat format of output file
     * @return a PlayBin which simultaneously plays audio while storing the audio on disk
     */
    public static PlayBin buildRecordingPlaybackPlayBin(File fileName, AudioFormat fileFormat) {
        // always check if Gstreamer is initialised since other components could uninitialise
        if (!Gst.isInitialized()) {
            Gst.init();
            System.out.println("Gst initialised");
        }
        // TODO: implement recording
        PlayBin pb = new PlayBin("Recording");
        switch(fileFormat) {
            case MP3 -> pb.setAudioSink(buildMP3AudioDiskSink());
        }
        return pb;
    }


    /**
     * Builds a pipeline which can be used as an audio sink.
     * This implementation plays audio through the soundcard whilst
     * simultaneously recording playback to disk as an mp3 file.
     * // TODO: works perfectly
     * @return an audio sink which simultaneously passes data to the sound card for
     *          live playback and stores a copy of the audio on disk in mp3 format
     */
    private static Bin buildMP3AudioDiskSink() {
        /*
                                    |-> queue -> autoaudiosink
           source -> playbin -> tee-|
                                    |-> queue -> audioconvert -> lamemp3enc -> id3v2mux -> fileSink

         */
        Bin multipleAudioSinkBin = new Bin();

        Element tee = ElementFactory.make("tee", "audioDuplicator");
        Element playbackQueue = ElementFactory.make("queue", "playbackQueue");
        Element diskQueue = ElementFactory.make("queue", "diskQueue");
        Element playbackSink = ElementFactory.make("autoaudiosink", "playbackSink");
        Element audioConverter = ElementFactory.make("audioconvert", "audioConverter");
        Element mp3Encoder = ElementFactory.make("lamemp3enc", "mp3Encoder");

        // Adds an ID3v2 header to the beginning of MP3 files using taglib
        Element mp3MetadataFormatter = ElementFactory.make("id3v2mux", "mp3MetadataFormatter");
        Element diskSink = ElementFactory.make("filesink", "diskSink");
        diskSink.set("location", "./output.mp3");

        multipleAudioSinkBin.add(tee);
        multipleAudioSinkBin.add(playbackQueue);
        multipleAudioSinkBin.add(playbackSink);
        multipleAudioSinkBin.add(diskQueue);
        multipleAudioSinkBin.add(audioConverter);
        multipleAudioSinkBin.add(mp3Encoder);
        multipleAudioSinkBin.add(mp3MetadataFormatter);
        multipleAudioSinkBin.add(diskSink);

        GhostPad gPad = new GhostPad("sink", PadDirection.SINK);
        gPad.setTarget(tee.getSinkPads().get(0));
        multipleAudioSinkBin.addPad(gPad);

        // duplicate the audio to form two pipelines
        tee.link(playbackQueue);
        tee.link(diskQueue);

        // playbackQueue -> playbackSink
        playbackQueue.link(playbackSink);

        // diskQueue -> audioconvert -> mp3encode -> mp3formatter -> diskSink
        diskQueue.link(audioConverter);
        audioConverter.link(mp3Encoder);
        mp3Encoder.link(mp3MetadataFormatter);
        mp3MetadataFormatter.link(diskSink);
        multipleAudioSinkBin.debugToDotFile(Bin.DebugGraphDetails.SHOW_ALL, "/tmp/debug-pipeline.DOT");
        return multipleAudioSinkBin;
    }

}
