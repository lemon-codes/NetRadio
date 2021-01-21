package codes.lemon.netradio.model;

import java.beans.PropertyChangeListener;


interface StreamPlayer {
    void setSource(String uri);
    void play();
    void stop();
    void setVolume(double volume);
    void subscribeToStreamTags(PropertyChangeListener o);
    // void setEquiliser()
}
