package codes.lemon.netradio.controller;

/**
 * Classes which implement this interface can subscribe to the ModelAdapter
 * to be notified when other components in the system alter the models state.
 */
interface ModelEventHandler {
    void handleEvent(ModelAdapter.ModelEvent event);
}
