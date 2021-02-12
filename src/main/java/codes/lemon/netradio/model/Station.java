package codes.lemon.netradio.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An immutable Station representation which contains all details of a radio station.
 */
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
    String getUri();

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
     * Returns the genre of this station.
     * @return the genre of this station
     */
    String getGenre();

    /**
     * Returns true if this channel is a favourite, else false.
     * @return true if channel is a favourite, else false.
     */
    boolean isFavourite();
}
