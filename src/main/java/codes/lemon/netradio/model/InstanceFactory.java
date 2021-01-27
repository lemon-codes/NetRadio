package codes.lemon.netradio.model;

// TODO: temporary solution.
public final class InstanceFactory {
    private static final RadioPlayer INSTANCE = new NetRadioPlayer();

    public static final RadioPlayer getInstance() {
        return INSTANCE;
    }
}
