package codes.lemon.netradio.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Contains the most recent metadata tags and values provided by the current station.
 * This implementation conforms to the Java Bean standard and allows clients to
 * provide a PropertyChangeListener to be notified when any property is updated.
 * Clients can identify which property was updated by using the static constants provided
 * in this class.
 */
public class ObservableMetadata implements java.io.Serializable {
    /**
     * Property names for values which PropertyChangeListeners can use to listen for changes.
     */
    public static final String PROP_TITLE = "title";
    public static final String PROP_GENRE = "genre";
    public static final String PROP_ORGANISATION = "organisation";
    public static final String PROP_EXTENDED_COMMENT = "extendedComment";
    public static final String PROP_CHANNEL_MODE = "channelMode";
    public static final String PROP_HOMEPAGE = "homepage";
    public static final String PROP_AUDIO_CODEC = "audioCodec";
    public static final String PROP_ENCODER = "encoder";
    public static final String PROP_ENCODER_VERSION = "encoderVersion";
    public static final String PROP_NOMINAL_BITRATE = "nominalBitrate";
    public static final String PROP_BITRATE = "bitrate";
    public static final String PROP_CONTAINER_FORMAT = "containerFormat";
    public static final String PROP_COUNTRY = "country";
    public static final String PROP_CITY = "city";


    private String title = "";
    private String genre = "";
    private String organisation = "";
    private String extendedComment = "";
    private String channelMode = "";
    private String homepage = "";
    private String audioCodec = "";
    private String encoder = "";
    private String encoderVersion = "";
    private String nominalBitrate = "";
    private String bitrate = "";
    private String containerFormat = "";
    private String country = "";
    private String city = "";
    private final PropertyChangeSupport pcs;

    public ObservableMetadata() {
        pcs = new PropertyChangeSupport(this);
    }


    /**
     * Register a PropertyChangeListener to be notified when any properties value is
     * updated. Properties can be identified using the constants provided by this class.
     * @param pcl A PropertyChangeListener implemented to listen to properties of this class.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        Objects.requireNonNull(pcl);
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Unregister a previously registered PropertyChangeListener. The removed PropertyChangeListener
     * will no longer be notified when property values are updated.
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /*-------------------------------
    |            SETTERS            |
    -------------------------------*/

    /**
     * Get the title of the current track
     * @return track title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the genre of music played by the current station
     * @return genre of music played by the current station
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Get the name of the organisation who run the current station.
     * This tag generally contains the stations name.
     * @return the organisation behind the station
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Get the stations homepage url
     * @return stations homepage url
     */
    public String getHomepage() {
        return homepage;
    }

    public String getExtendedComment() {
        return extendedComment;
    }

    public String getChannelMode() {
        return channelMode;
    }


    public String getAudioCodec() {
        return audioCodec;
    }

    public String getEncoder() {
        return encoder;
    }

    public String getEncoderVersion() {
        return encoderVersion;
    }

    public String getNominalBitrate() {
        return nominalBitrate;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getContainerFormat() {
        return containerFormat;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    /*-------------------------------
    |            SETTERS            |
    -------------------------------*/

    public void setTitle(String title) {
        String oldValue = this.title;
        this.title = title;
        pcs.firePropertyChange(PROP_TITLE, oldValue, title);
    }

    public void setGenre(String genre) {
        String oldValue = this.genre;
        this.genre = genre;
        pcs.firePropertyChange(PROP_GENRE, oldValue, genre);
    }

    public void setOrganisation(String organisation) {
        String oldValue = this.organisation;
        this.organisation = organisation;
        pcs.firePropertyChange(PROP_ORGANISATION, oldValue, organisation);
    }

    public void setExtendedComment(String extendedComment) {
        String oldValue = this.extendedComment;
        this.extendedComment = extendedComment;
        pcs.firePropertyChange(PROP_EXTENDED_COMMENT, oldValue, extendedComment);
    }

    public void setChannelMode(String channelMode) {
        String oldValue = this.channelMode;
        this.channelMode = channelMode;
        pcs.firePropertyChange(PROP_CHANNEL_MODE, oldValue, channelMode);
    }

    public void setHomepage(String homepage) {
        String oldValue = this.homepage;
        this.homepage = homepage;
        pcs.firePropertyChange(PROP_HOMEPAGE, oldValue, homepage);
    }

    public void setAudioCodec(String audioCodec) {
        String oldValue = this.audioCodec;
        this.audioCodec = audioCodec;
        pcs.firePropertyChange(PROP_AUDIO_CODEC, oldValue, audioCodec);
    }

    public void setEncoder(String encoder) {
        String oldValue = this.encoder;
        this.encoder = encoder;
        pcs.firePropertyChange(PROP_ENCODER, oldValue, encoder);
    }

    public void setEncoderVersion(String encoderVersion) {
        String oldValue = this.encoderVersion;
        this.encoderVersion = genre;
        pcs.firePropertyChange(PROP_ENCODER_VERSION, oldValue, encoderVersion);
    }

    public void setNominalBitrate(String nominalBitrate) {
        String oldValue = this.nominalBitrate;
        this.nominalBitrate = nominalBitrate;
        pcs.firePropertyChange(PROP_NOMINAL_BITRATE, oldValue, nominalBitrate);
    }

    public void setBitrate(String bitrate) {
        String oldValue = this.bitrate;
        this.bitrate = bitrate;
        pcs.firePropertyChange(PROP_BITRATE, oldValue, bitrate);
    }

    public void setContainerFormat(String containerFormat) {
        String oldValue = this.containerFormat;
        this.containerFormat = containerFormat;
        pcs.firePropertyChange(PROP_CONTAINER_FORMAT, oldValue, containerFormat);
    }

    public void setCountry(String country) {
        String oldValue = this.country;
        this.country = country;
        pcs.firePropertyChange(PROP_COUNTRY, oldValue, country);
    }

    public void setCity(String city) {
        String oldValue = this.city;
        this.city = city;
        pcs.firePropertyChange(PROP_CITY, oldValue, city);
    }

    /**
     * Resets all properties to empty Strings.
     * All PropertyChangeListeners are notified of the updates.
     */
    public void resetAllProperties() {
        setAudioCodec("");
        setBitrate("");
        setCity("");
        setChannelMode("");
        setCountry("");
        setContainerFormat("");
        setEncoder("");
        setEncoderVersion("");
        setExtendedComment("");
        setGenre("");
        setHomepage("");
        setNominalBitrate("");
        setOrganisation("");
        setTitle("");
    }
}
