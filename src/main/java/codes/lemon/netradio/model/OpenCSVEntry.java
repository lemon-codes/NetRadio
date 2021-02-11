package codes.lemon.netradio.model;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;

/**
 * Stores unsanitised details from a csv entry. If we bound these annotations
 * directly to a Station implementation, because OpenCSV instantiates object fields
 * with no validation it could result in Station instances being published with an
 * invalid state (eg if the CSV file was manually modified or corrupted).
 * This also encapsulates OpenCSV functionality to the StationLoader class, including
 * any dependencies between fields and column names.
 */
public class OpenCSVEntry {
    @CsvBindByName(column = "ID", required = true)
    private int id;

    @CsvBindByName (column = "Names", required = true)
    private String name;

    @CsvBindByName (column = "URI", required = true)
    private String uri;

    @CsvBindByName (column = "Genre")
    private String genre;

    @CsvBindByName (column = "PlayCount", required = true)
    private int playCount;

    @CsvBindByName (column = "Bitrate", required = true)
    private int bitrate;



    @CsvBindByName (column = "Favourite", required = true)
    private boolean favourite;

    // last played is not required since it it has no default value
    @CsvBindByName (column = "LastPlayed")
    private LocalDateTime lastPlayed;

    // public constructor with no parameters required to enable OpenCSV to construct instances
    public OpenCSVEntry() {};

    public OpenCSVEntry(int id, String name, String uri, int playCount, int bitrate,
                        boolean favourite, String genre, LocalDateTime lastPlayed) {
        // no bounds checks since Station instances only contain valid data
        // OpenCSV does not use this constructor. We cannot check field data before initialisation
        // for instances constructed by OpenCSV
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.playCount = playCount;
        this.bitrate = bitrate;
        this.favourite = favourite;
        this.genre = genre;
        this.lastPlayed = lastPlayed;

    }

    // GETTERS
    public int getId() { return id; }
    public String getName() { return name; }
    public String getUri() { return uri; }
    public int getPlayCount() { return playCount; }
    public int getBitrate() { return bitrate; }
    public boolean getFavourite() { return favourite; }
    public LocalDateTime getLastPlayed() { return lastPlayed; }
    public String getGenre() { return genre; }
}