package codes.lemon.netradio.model;

import java.time.LocalDateTime;

class RadioStation implements Station{
    private final int id;
    private final String name;
    private final String uri;
    private LocalDateTime lastPlayed = null;
    private int playCount = 0;
    private int bitrate = -1;
    private boolean favourite = false;

    public RadioStation(int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        assert (id > 0) : "negative ID supplied";
        assert(name != null) : "null supplied in name";
        assert(uri != null) : "null supplied in uri";
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
    public String getURI() {
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
        return lastPlayed;
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
     * Determines wither this station is marked as a favourite
     *
     * @return true if channel is a favourite, else false.
     */
    @Override
    public boolean isFavourite() {
        return favourite;
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
     * Mark this station as being played. This updates the values returned by
     * `getDateLastPlayed()`, `getPlayCount()` and potentially `getBitrate()`
     * if that value has not been set previously or has changed.
     */
    @Override
    public void markPlayed() {
        lastPlayed = LocalDateTime.now();
        playCount++;
    }
}
