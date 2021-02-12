package codes.lemon.netradio.model;

/**
 * This interface provides the package access to modify Station instances
 * without providing this access to clients. This allows the package
 * to alter Station details and provide the client access to those same
 * Station instances without making defensive copies.
 */
interface MutableStation extends Station {
    /**
     * Sets the bitrate at which the current station plays at.
     * @param bitrate bitrate of current station
     */
    void setBitrate(int bitrate);

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
     * Sets the genre for this station.
     * @param genre The genre of this station, must not be null.
     */
    void setGenre(String genre);

    /**
     * Mark this station as being played. This updates the values returned by
     * `getDateLastPlayed()`, `getPlayCount()`.
     */
    void markPlayed();

}
