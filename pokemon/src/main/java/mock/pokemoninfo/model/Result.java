package mock.pokemoninfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;

    private String defaultFormUrl;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultFormUrl(String url){
        defaultFormUrl = url;
    }

    public String getDefaultFormUrl(){
        return defaultFormUrl;
    }

}
