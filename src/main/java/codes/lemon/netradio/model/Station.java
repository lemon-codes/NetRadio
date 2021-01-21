package codes.lemon.netradio.model;

import java.time.LocalDateTime;

public interface Station {
    /**
     * Returns this stations unique numerical identifier.
     * @return this stations unique numerical identifier.
     */
    int getStationID();

    /**
     * Returns this stations name
     * @return this stations name
     */
    String getStationName();

    /**
     * Returns the URI that is being used as a source to stream this Station
     * @return the source of this station
     */
    String getURI();

    /**
     * Returns the bitrate of the stream for this station if available.
     * If unavailable -1 should be returned.
     * @return the bitrate of the stream for this channel, else -1
     */
    int getBitrate();

    /**
     * Returns the date and time this station was last played. If it has
     * never been played before, null is returned.
     * @return the date this station was last played, else null.
     */
    LocalDateTime getDateLastPlayed();

    /**
     * Returns a count of the number of times this station has been played
     * in the past.
     * @return the number of times this station has been played
     */
    int getPlayCount();

    /**
     * Returns true if this channel is a favourite, else false.
     * @return true if channel is a favourite, else false.
     */
    boolean isFavourite();

    /**
     * Sets the favourite status of this station. If passed true this station will
     * be marked as a favourite. If passed false this station will no longer
     * be marked as a favourite. Attempting to favourite an already favourite channel,
     * or unfavourite a station that is not a favourite will result in no action taken
     * since the station is already in the desired state.
     * @param val true to mark as favourite, false to unmark as favourite.
     */
    void setFavourite(boolean val);

    /**
     * Mark this station as being played. This updates the values returned by
     * `getDateLastPlayed()`, `getPlayCount()` and potentially `getBitrate()`
     * if that value has not been set previously or has changed.
     */
    void markPlayed(); // updates lastPlayed, bitrate, playCount

    // getGenre
}
