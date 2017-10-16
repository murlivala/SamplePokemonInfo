package mock.pokemoninfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model class for single pokemon
 */

public class Pokemon {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("sprites")
    @Expose
    private Sprites sprites;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getId() {
        return id;
    }

}