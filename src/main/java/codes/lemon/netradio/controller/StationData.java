package codes.lemon.netradio.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class StationData {

    private final String id;
    private final String name;
    private final String uri;
    private final String genre;
    private final int playCount;
    private final boolean favourite;
    private final int bitrate;
    private final BooleanProperty favouriteProperty;  // required for CheckBoxTableCell

    public StationData(int id, String name, String uri, String genre, int playCount, boolean favourite, int bitrate) {
        this.id = String.valueOf(id);
        this.name = name;
        this.uri = uri;
        this.genre = genre;
        this.playCount = playCount;
        this.favourite = favourite;
        this.favouriteProperty = new SimpleBooleanProperty(favourite);
        this.bitrate = bitrate;
    }

    public String getId() {
        return id;
    }

    public int getIdAsInt() {
        return Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getGenre() { return genre; }

    public int getPlayCount() { return playCount; }

    public boolean isFavourite() { return favourite; }

    public BooleanProperty favouriteProperty() { return favouriteProperty; }

    public int getBitrate() { return bitrate; }






}
