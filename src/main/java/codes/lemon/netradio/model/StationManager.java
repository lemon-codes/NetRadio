package codes.lemon.netradio.model;

import java.util.*;

class StationManager {
    private final StationLoader storage = new StationLoader();
    // Station IDs are mapped to Station instances for quick lookup by ID
    private final Map<Integer,Station> stations;
    private int currentID = 0; // TODO: replace with proper solution

    public StationManager() {
        stations = mapIDToStation(storage.getStations());
    }

    private Map<Integer, Station> mapIDToStation(List<Station> stationList) {
        Map<Integer, Station> stationMap = new HashMap<>();
        for (Station s : stationList) {
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

    public Collection<Station> getAllStations() {
        return stations.values();
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
        // should never occur as StationManager is not public
        assert(name != null) : "null station name supplied";
        assert(uri != null) : "null station uri supplied";

        Station newStation = new RadioStation(currentID, name, uri);

        // ID assignment is under our control so should always be unique
        assert (!stations.containsKey(currentID)) : "ID is not unique";
        stations.put(currentID, newStation);
        return currentID++;
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
        return removed != null;
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
        Station s = getStation(id);
        if (s != null) {
            s.setFavourite(status);
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
            if (s.getURI().toLowerCase().contains(searchTerm)) {
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

}
