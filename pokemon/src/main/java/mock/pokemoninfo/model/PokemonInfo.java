package mock.pokemoninfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Model class for pokemon data available on each page
 */

public class PokemonInfo {
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("next")
    @Expose
    private String next;

    public Integer getCount() {
        return count;
    }

    public List<Result> getResults() {
        return results;
    }

    public String getNext() {
        return next;
    }

}
