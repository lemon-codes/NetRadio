package codes.lemon.netradio.model;

import java.util.ArrayList;
import java.util.List;

class StationLoader {
    public List<Station> getStations() {
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

    public void saveStations(List<Station> stations) {

    }
}
