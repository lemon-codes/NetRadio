package codes.lemon.netradio.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StationData {
/*
    private final StringProperty stationId = new SimpleStringProperty();
    private final StringProperty stationName = new SimpleStringProperty();
    private final StringProperty stationUri = new SimpleStringProperty();

    public StationData(int id, String name, String uri) {
        stationId.setValue(String.valueOf(id));
        stationName.setValue(name);
        stationUri.setValue(uri);
    }

 */
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
