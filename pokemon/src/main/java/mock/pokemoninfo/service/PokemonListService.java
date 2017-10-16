package mock.pokemoninfo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mock.pokemoninfo.callbacks.ServiceCallback;
import mock.pokemoninfo.model.PokemonInfo;
import mock.pokemoninfo.api.PokemonListAPI;
import mock.pokemoninfo.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonListService implements Callback<PokemonInfo> {
    Call<PokemonInfo> mCall;
    PokemonListAPI pokemonListAPI;
    ServiceCallback mServiceCallBack;
    public PokemonListService(ServiceCallback serviceCallback){
        mServiceCallBack = serviceCallback;
    }
    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        pokemonListAPI = retrofit.create(PokemonListAPI.class);
        request();
    }

    public void request(){
        mCall = pokemonListAPI.loadChanges();
        mCall.enqueue(PokemonListService.this);
    }

    public void request(String query){
        mCall = pokemonListAPI.loadChanges(query);
        mCall.enqueue(PokemonListService.this);
    }

    public void cancel(){
        if(null != mCall){
            mCall.cancel();
        }
    }

    @Override
    public void onResponse(Call<PokemonInfo> call, Response<PokemonInfo> response) {
        if(response.isSuccessful()) {
            mServiceCallBack.onSuccess(Constants.SUCCESS,response);
        } else {
            mServiceCallBack.onFailure(Constants.FAILURE,"Error");
        }
    }

    @Override
    public void onFailure(Call<PokemonInfo> call, Throwable t) {
        mServiceCallBack.onFailure(Constants.NETWORK_FAILURE,t.getMessage());
        t.printStackTrace();
    }
}