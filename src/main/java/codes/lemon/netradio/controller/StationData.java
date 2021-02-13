package codes.lemon.netradio.controller;

public class StationData {

    private final String id;
    private final String name;
    private final String uri;
    private final String genre;
    private final int playCount;
    private final boolean favourite;
    private final int bitrate;

    public StationData(int id, String name, String uri, String genre, int playCount, boolean favourite, int bitrate) {
        this.id = String.valueOf(id);
        this.name = name;
        this.uri = uri;
        this.genre = genre;
        this.playCount = playCount;
        this.favourite = favourite;
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

    public int getBitrate() { return bitrate; }






}
