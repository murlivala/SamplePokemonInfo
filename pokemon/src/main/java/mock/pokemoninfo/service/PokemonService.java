package mock.pokemoninfo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mock.pokemoninfo.callbacks.ServiceCallback;
import mock.pokemoninfo.model.Pokemon;
import mock.pokemoninfo.api.PokemonAPI;
import mock.pokemoninfo.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonService implements Callback<Pokemon> {

    Call<Pokemon> mCall;
    PokemonAPI pokemonAPI;
    ServiceCallback serviceCallback;
    public PokemonService(ServiceCallback callback){
        serviceCallback = callback;
    }
    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        pokemonAPI = retrofit.create(PokemonAPI.class);

    }

    public void request(String id){
        mCall = pokemonAPI.loadSinglePokemonInfo(id);
        mCall.enqueue(this);
    }

    public void cancel(){
        if(null != mCall){
            mCall.cancel();
        }
    }

    @Override
    public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
        if(response.isSuccessful()) {
            serviceCallback.onSuccess(Constants.SUCCESS,response);
        } else {
            serviceCallback.onFailure(Constants.FAILURE,"Error");
        }
    }

    @Override
    public void onFailure(Call<Pokemon> call, Throwable t) {
        serviceCallback.onFailure(Constants.NETWORK_FAILURE,t.getMessage());
        t.printStackTrace();
    }
}