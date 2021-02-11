package codes.lemon.netradio.controller;

public class StationData {

    private final String stationId;
    private final String stationName;
    private final String stationUri;

    public StationData(int id, String name, String uri) {
        this.stationId = String.valueOf(id);
        this.stationName = name;
        this.stationUri = uri;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getStationUri() {
        return stationUri;
    }

    public int getStationIdAsInt() {
        return Integer.parseInt(stationId);
    }
}
