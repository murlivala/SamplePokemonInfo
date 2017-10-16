package mock.pokemoninfo.utils;

import mock.pokemoninfo.model.PokemonFeedHolder;

public class PokemonUtils {
    private static PokemonFeedHolder sPokemonFeedHolder = null;
    public static PokemonFeedHolder getsPokemonFeedHolder(){
        synchronized (PokemonUtils.class){
            if(null == sPokemonFeedHolder){
                sPokemonFeedHolder = new PokemonFeedHolder();
            }
        }
        return sPokemonFeedHolder;
    }

    public static void releasePokemonFeedHolder(){
        sPokemonFeedHolder.clear();
        sPokemonFeedHolder = null;
    }
}
