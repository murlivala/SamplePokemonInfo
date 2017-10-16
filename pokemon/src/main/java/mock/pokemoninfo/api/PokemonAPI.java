package mock.pokemoninfo.api;

import mock.pokemoninfo.model.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonAPI {

    @GET("api/v2/pokemon/{id}")
    Call<Pokemon> loadSinglePokemonInfo(@Path("id") String id);
}
