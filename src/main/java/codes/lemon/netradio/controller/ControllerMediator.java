package codes.lemon.netradio.controller;


import java.util.Objects;

public class ControllerMediator implements Mediator{
    // Ensure a mediator is available.
    private static final ControllerMediator INSTANCE = new ControllerMediator();
    public static ControllerMediator getInstance() {
        return INSTANCE;
    }

    private PlaybackController playbackController;
    private StationListController stationListController;

    private ControllerMediator() {
        // singleton pattern to ensure controllers can safely access the same Mediator instance
    }

    public void setPlaybackController(PlaybackController playbackController) {
        this.playbackController = Objects.requireNonNull(playbackController);
    }

    public void setStationListController(StationListController stationListController) {
        this.stationListController = Objects.requireNonNull(stationListController);
    }


    /**
     * Initiates playback in the playback controller. This allows the view to respond
     * accordingly.
     */
    @Override
    public void initiatePlayback() {
        if (playbackController != null) {
            playbackController.play();
        }
        else {
            throw new IllegalStateException("playbackController not set");
        }
    }


}
