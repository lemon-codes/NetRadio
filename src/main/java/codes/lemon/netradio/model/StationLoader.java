package codes.lemon.netradio.model;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A persistence mechanism for storing and retrieving station data
 * between runs.
 */
class StationLoader {
    private static final String FILE_PATH = "./.netradio-stations.csv";

    /**
     * Retrieves previously stored Station instances.
     * The Station instances returned are identical in value to those
     * that were stored, though they are different instances.
     * Multiple calls to this method will never return the same instances.
     * @return previously stored station instances, else an empty list.
     */
    public List<Station> getStations() {
        File stationsFile = new File(FILE_PATH);
        List<Station> stations = new LinkedList<>();
        List<OpenCSVEntry> rows = new LinkedList<>();

        try (FileReader in = new FileReader(stationsFile)) {
            // constructs an OpenCSVEntry instance for each CSV row.
            // column headers are mapped to fields by OpenCSV annotations in OpenCSVEntry
            rows = new CsvToBeanBuilder<OpenCSVEntry>(in).
                                            withType(OpenCSVEntry.class).build().parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // OpenCSVEntry instances are contain unsanitised data.
        // Replace with Station instances which perform validation
        for (OpenCSVEntry row : rows) {
            stations.add(buildStation(row));
        }
        return stations;
    }

    /**
     * Store station instances. The data contained in these instances will be
     * stored permanently and can be retrieved in future runs.
     * Station instances containing the same data can be retrieved by calling
     * `getStations()`.
     * @param stations A list of station instances to be stored.
     */
    public void storeStations(List<Station> stations) {
        List<OpenCSVEntry> rows = new LinkedList<>();

        // copy station fields to OpenCSVEntry instance which contains OpenCSV annotations
        for (Station s : stations) {
            rows.add(buildOpenCSVEntry(s));
        }
        File stationsFile = new File(FILE_PATH);
        try (FileWriter out = new FileWriter(stationsFile)) {
            // Map column headers to instance fields using OpenCSV annotations
            MappingStrategy<OpenCSVEntry> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(OpenCSVEntry.class);
            StatefulBeanToCsv<OpenCSVEntry> writer = new StatefulBeanToCsvBuilder<OpenCSVEntry>(out).
                    withMappingStrategy(strategy).withOrderedResults(false).build();
            // writing all rows in the one call allows OpenCSV to work concurrently
            writer.write(rows);
        }
        catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            // TODO: explore exception handling options
            e.printStackTrace();
        }
    }

    /**
     * Constructs a Station instance from a provided OpenCSVEntry instance.
     * OpenCSVEntry instances contain unsanitised data as they are populated by OpenCSV.
     * The returned Station instances only contain sanitised data.
     * @param entry a CSV entry containing all the data necessary to construct a station instance.
     * @return a station instance which contains a sanitised copy of the data contained
     *          in the OpenCSVEntry.
     */
    private Station buildStation(OpenCSVEntry entry) {
        return new RadioStation(entry.getId(), entry.getName(), entry.getUri(), entry.getLastPlayed(),
                entry.getPlayCount(), entry.getBitrate(), entry.getGenre(), entry.getFavourite());
    }

    /**
     * Constructs a OpenCSVEntry from a provided Station instance.
     * OpenCSVEntry contains the annotations which enable OpenCSV to map
     * data to column headers. An OpenCSVEntry instance enables the given
     * stations data to be stored.
     * @param s a station instance
     * @return a OpenCSVEntry instance containing the field data of the station instance.
     */
    private OpenCSVEntry buildOpenCSVEntry(Station s) {
        return new OpenCSVEntry(s.getStationID(), s.getStationName(), s.getUri(), s.getPlayCount(),
                s.getBitrate(), s.isFavourite(), s.getGenre(), s.getDateLastPlayed());
    }


    public List<Station> getStationsForDevelopment() {
        // temporary development aid
        int x = 0;
        List<Station> stations = new ArrayList<>();
        stations.add(new RadioStation(x++, "Clyde1", "http://stream-al.planetradio.co.uk/clyde1.mp3"));
        stations.add(new RadioStation(x++, "Capital FM", "http://media-ice.musicradio.com/Capital"));
        stations.add(new RadioStation(x++, "BBC Radio 1", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p"));
        stations.add(new RadioStation(x++, "StartFM", "http://eteris.startfm.lt/startfm.ogg"));
        stations.add(new RadioStation(x++, "FreeDesktop", "https://www.freedesktop.org/software/gstreamer-sdk/data/media/sintel_trailer-480p.webm"));
        return stations;
    }
}
