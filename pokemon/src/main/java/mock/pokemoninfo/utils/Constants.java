package mock.pokemoninfo.utils;

public class Constants {

    //network failure
    public static final int NETWORK_FAILURE = -99;

    //service response failure
    public static final int FAILURE = -1;
    public static final int SUCCESS = 0;

    //messages to direct UI in error cases
    public static final int UPDATE_VIEW_ON_ERROR = 2;
    public static final int TRY_AGAIN_ON_ERROR = 3;

    //Top bar text animation duration
    public static final int ANIMATION_DURATION_SHORT = 100;
    public static final int ANIMATION_DURATION = 3000;

    public static final int MESSAGE_TOTAL_POKEMON = 1;
    public static final int MESSAGE_LOADED_POKEMON = 2;
    public static final int MESSAGE_LAST_VIEWED_POKEMON = 3;

    public static final int FRAGMENT_PAGES = 4;

    public static final String SERVICE_BASE_URL = "https://pokeapi.co/";
}
