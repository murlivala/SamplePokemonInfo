package mock.pokemoninfo.api;

import mock.pokemoninfo.model.PokemonInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokemonListAPI {

    @GET("api/v2/pokemon")
    Call<PokemonInfo> loadChanges();

    @GET("api/v2/pokemon")
    Call<PokemonInfo> loadChanges(@Query("offset") String tags);
}
