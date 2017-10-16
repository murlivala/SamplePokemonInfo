package mock.pokemoninfo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold pokemons successfully fetched from server
 */

public class PokemonFeedHolder {
    private List<Result> pokemonInfo = new ArrayList<>();
    private int pokemonCount;
    private String nextPage;

    public PokemonFeedHolder(){
        nextPage = "";
    }
    public void setNextPage(String url){
        nextPage = url;
    }

    public String getNextPage(){
        return nextPage;
    }

    public List<Result> getPokemonInfoList(){
        return pokemonInfo;
    }

    public Result getPokemonInfo(int index){
        return pokemonInfo.get(index);
    }

    public void addPokemonInfo(Result item){
        pokemonInfo.add(item);
    }

    public void setPokemonCount(int count){
        pokemonCount = count;
    }

    public int getPokemonCount(){
        return pokemonCount;
    }

    public void clear(){
        pokemonInfo.clear();
        pokemonInfo = null;
    }
}
