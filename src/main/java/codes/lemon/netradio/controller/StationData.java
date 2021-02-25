package codes.lemon.netradio.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * Mirrors a Station instance from the model. This implementation has specifically named
 * fields which can be used by JavaFX PropertyValueFactories. Clients can access field names
 * using the public static fields provided.
 */
public class StationData {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String URI_FIELD = "uri";
    public static final String GENRE_FIELD = "genre";
    public static final String PLAY_COUNT_FIELD = "playCount";
    public static final String FAVOURITE_FIELD = "favourite";
    public static final String BITRATE_FIELD = "bitrate";
    public static final String FAVOURITE_PROPERTY_FIELD = "favouriteProperty";

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

    public String getGenre() {
        return genre;
    }

    public int getPlayCount() {
        return playCount;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public BooleanProperty favouriteProperty() {
        return favouriteProperty;
    }

    public int getBitrate() {
        return bitrate;
    }


}
