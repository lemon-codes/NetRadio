package codes.lemon.netradio.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Stores details of a radio station.
 */
class RadioStation implements Station{
    private final int id;
    private final String name;
    private final String uri;
    private LocalDateTime lastPlayed = null;
    private int playCount = 0;
    private int bitrate = -1;
    private String genre = "";
    private boolean favourite = false;

    // TODO: Consider builder design pattern.
    public RadioStation(int id, String name, String uri) {
        this(id, name, uri, null, 0, -1, "", false);
    }

    protected RadioStation(int id, String name, String uri, LocalDateTime lastPlayed,
                           int playCount, int bitrate, String genre, boolean favourite) {
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalStateException("ID must be positive");
        }

        // fix if corrupted since these fields don't hold any significance to the stations identity
        this.playCount = Math.max(playCount, 0);
        this.bitrate = Math.max(bitrate, -1);

        this.name = Objects.requireNonNull(name);
        this.uri = Objects.requireNonNull(uri);
        this.lastPlayed = lastPlayed;  // null accepted if never played before
        this.genre = genre;
        this.favourite = favourite;
    }

    /**
     * Returns this stations unique numerical identifier.
     *
     * @return this stations unique numerical identifier.
     */
    @Override
    public int getStationID() {
        return id;
    }

    /**
     * Returns this stations name
     *
     * @return this stations name
     */
    @Override
    public String getStationName() {
        return name;
    }

    /**
     * Returns the URI that is being used as a source to stream this Station
     *
     * @return the source of this station
     */
    @Override
    public String getUri() {
        return uri;
    }

    /**
     * Returns the bitrate of the stream for this station if available.
     * If unavailable -1 should be returned.
     *
     * @return the bitrate of the stream for this channel, else -1
     */
    @Override
    public int getBitrate() {
        return bitrate;
    }

    /**
     * Returns the date and time this station was last played. If it has
     * never been played before, null is returned.
     *
     * @return the date this station was last played, else null.
     */
    @Override
    public LocalDateTime getDateLastPlayed() {
        // TODO: Fix openCSV parsing of LocalDateTime fields
        return null;
        //return lastPlayed;
    }

    /**
     * Returns a count of the number of times this station has been played
     * in the past.
     *
     * @return the number of times this station has been played
     */
    @Override
    public int getPlayCount() {
        return playCount;
    }

    /**
     * Returns the genre of this station.
     * @return the genre of this station
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Determines wither this station is marked as a favourite
     *
     * @return true if channel is a favourite, else false.
     */
    @Override
    public boolean isFavourite() {
        return favourite;
    }

    /**
     * Sets the bitrate at which the current station plays at.
     *
     * @param bitrate bitrate of current station.
     */
    @Override
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;

    }

    /**
     * Sets the favourite status of this station. If passed true this station will
     * be marked as a favourite. If passed false this station will no longer
     * be marked as a favourite. Attempting to favourite an already favourite channel,
     * or unfavourite a station that is not a favourite will result in no action taken
     * since the station is already in the desired state.
     * @param val true to mark as favourite, false to unmark as favourite.
     */
    @Override
    public void setFavourite(boolean val) {
        favourite = val;
    }


    /**
     * Sets the genre for this station.
     * @param genre The genre of this station, must not be null.
     */
    public void setGenre(String genre) {
        this.genre = Objects.requireNonNull(genre);
    }

    /**
     * Mark this station as being played. This updates the values returned by
     * `getDateLastPlayed()`, `getPlayCount()`.
     */
    @Override
    public void markPlayed() {
        lastPlayed = LocalDateTime.now();
        playCount++;
    }


    /**
     * Compares RadioStation instances for equality based upon their unique ID.
     * To ensure no two radio stations have the same ID, we base station equality
     * on ID alone. If additional fields were included, two stations could exist
     * with the same ID but different content, and would be considered unequal.
     * In this use case, we consider both stations equal if their ID matches.
     * @param o object to be compared with this
     * @return true if `this` and `o` are equal, else false.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof RadioStation) {
            RadioStation r = (RadioStation) o;
            return this.id == r.id;
        }
        return false;
    }

    /**
     * Returns a hash code value for the object.
     * To ensure we don't violate the general contract between `hashCode()` and `equals()`
     * the hashcode is now (like `equals()`) based soley on this instances unique ID.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}
