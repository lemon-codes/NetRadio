package codes.lemon.netradio.model;

import java.util.*;

class StationManager {
    private final StationLoader storage = new StationLoader();
    // Station IDs are mapped to Station instances for efficient (T(O) = O(1)) station lookup
    private final Map<Integer,MutableStation> stations;

    public StationManager() {
        // retrieve any stations stored from previous runs.
        // Generate mapping from ID to station for O(1) lookup.
        stations = mapIDToStation(storage.getStations());
    }

    /**
     * Accepts a list of stations and returns a Map with each stations
     * ID as a key with the corresponding station instance as the value
     * for that key. This enables efficient station lookup T(O)=O(1) rather than
     * looping through every station T(O) = O(n).
     * @param stationList list of stations
     * @return mapping of station IDs -> station instances
     */
    private Map<Integer, MutableStation> mapIDToStation(List<MutableStation> stationList) {
        Map<Integer, MutableStation> stationMap = new HashMap<>();
        for (MutableStation s : stationList) {
            stationMap.put(s.getStationID(), s);
        }
        return stationMap;
    }

    /**
     * Returns the Station for the given ID. Returns null if an invalid
     * id is provided.
     * @param id a station ID
     * @return the Station with the given ID, else null.
     */
    public Station getStation(int id) {
        if (stations.containsKey(id)) {
            return stations.get(id);
        }
        return null;
    }

    /**
     * Return an immutable list of all station instances.
     * @return an immutable list of all stations
     */
    public List<Station> getAllStations() {
        List<Station> list = new LinkedList<Station>(stations.values());
        return Collections.unmodifiableList(list);
    }

    /**
     * Add a station to the player. Requires a name for the station and a
     * URI pointing to an audio source for that station.
     * The station will be assigned a unique ID.
     *
     * @param name the name of the new station
     * @param uri  points to an audio source for the new station
     * @return the ID given to the newly added station
     */
    public int addStation(String name, String uri) {
        // should never occur as NetRadioPlayer validates input
        assert(name != null) : "null station name supplied";
        assert(uri != null) : "null station uri supplied";

        int id = getUniqueID();
        // ID assignment is under our control so should always be unique
        assert (!stations.containsKey(id)) : "ID is not unique";
        Station newStation = new RadioStation(id, name, uri);

        stations.put(id, (MutableStation) newStation);
        updateDataInStorage();
        return id;
    }

    /**
     * Permanently remove a station from the player.
     *
     * @param id the unique ID of the station to be removed
     * @return true if the station was successfully removed, else false.
     */
    public boolean removeStation(int id) {
        // remove() returns null if key doesn't exist
        Station removed = stations.remove(id);
        if (removed != null) {
            updateDataInStorage();
            return true;
        }
        return false;
    }

    /**
     * Sets the playback bitrate of the given station with the given ID.
     * @param id id of the station to be updated
     * @param bitrate playback bitrate of the given station
     */
    public void setBitrate(int id, int bitrate) {
        MutableStation s = (MutableStation) getStation(id);
        if (s != null && bitrate != s.getBitrate()) {
            // only update if new value is different to prevent unnecessary disk I/O
            s.setBitrate(bitrate);
            updateDataInStorage();
        }
    }

    /**
     * Sets the `Favourite` status of a given station. When true,
     * that station is considered a favourite. When false, the
     * station is no longer considered a favourite.
     *
     * @param id     the unique ID for a station
     * @param status true to mark as a favourite, else false
     */
    public void setFavourite(int id, boolean status) {
        MutableStation s = (MutableStation) getStation(id);
        if (s != null && status != s.isFavourite()) {
            s.setFavourite(status);
            updateDataInStorage();
        }
    }

    /**
     * Sets the genre of the station with the given ID.
     * @param id station to be updated
     * @param genre the genre of the station. Not null
     */
    public void setGenre(int id, String genre) {
        Objects.requireNonNull(genre);
        MutableStation s = (MutableStation) getStation(id);
        if (s != null && !genre.equals(s.getGenre())) {
            // only update if new value is different to prevent unnecessary disk I/O
            s.setGenre(genre);
            updateDataInStorage();
        }
    }

    /**
     * Searches through station details looking for the given search term.
     * Searches are case insensitive.
     *
     * @param searchTerm the term to be searched
     * @return a list of all stations whose details contain an instance of the
     * search term. An empty list is returned if no results are found.
     */
    public List<Station> findStation(String searchTerm) {
        assert (searchTerm != null) : "null search term supplied";
        searchTerm = searchTerm.toLowerCase();
        List<Station> results = new ArrayList<>();
        for (Station s : stations.values()) {
            boolean found = false;
            if (s.getUri().toLowerCase().contains(searchTerm)) {
                found = true;
            }
            if (s.getStationName().toLowerCase().contains(searchTerm)) {
                found = true;
            }
            if (found) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Writes any changes to storage and exits.
     */
    public void shutdown() {
        updateDataInStorage();
    }

    /**
     * Write any changes to stations to storage.
     */
    private void updateDataInStorage() {
        storage.storeStations(getAllMutableStations());
    }

    /**
     * Provides access to all Station instances through their MutableStation
     * interface.
     * @return list of mutable stations
     */
    private List<MutableStation> getAllMutableStations() {
        List<MutableStation> list = new LinkedList<MutableStation>(stations.values());
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns a unique ID that is not currently in use.
     * TODO: find a more efficient solution
     * @return a new unique station ID
     */
    private int getUniqueID() {
        int id = 0;
        // find the lowest value not currently used
        // containsKey() should be O(1) since we use Integers hashCode()
        while (stations.containsKey(id)) {
            id++;
        }

        assert(id >= 0) : "Negative ID generated";
        return id;
    }

    /**
     * Mark the given station as being played. This updates any values related to
     * playback history such as playCount and lastPlayed.
     * @param stationID the id of the station to be marked as played.
     */
    public void markPlayed(int stationID) {
        MutableStation station = stations.get(stationID);
        if (station != null) {
            station.markPlayed();
        }
    }
}
