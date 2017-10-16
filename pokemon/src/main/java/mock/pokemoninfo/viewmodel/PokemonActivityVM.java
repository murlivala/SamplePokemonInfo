package mock.pokemoninfo.viewmodel;

import mock.pokemoninfo.callbacks.ServiceCallback;
import mock.pokemoninfo.callbacks.UICallback;
import mock.pokemoninfo.model.Pokemon;
import mock.pokemoninfo.service.PokemonService;
import mock.pokemoninfo.utils.PokemonUtils;
import retrofit2.Response;

public class PokemonActivityVM implements ServiceCallback<Pokemon> {

    UICallback uiCallback;
    PokemonService pokemonService;
    int position;
    public PokemonActivityVM(UICallback callback){
        uiCallback = callback;
    }

    public void getPokemonAttributes(String id,int pos){
        position = pos;
        if(null == pokemonService){
            pokemonService = new PokemonService(PokemonActivityVM.this);
            pokemonService.start();
            pokemonService.request(id);
        }else{
            pokemonService.request(id);
        }
    }

    @Override
    public void onSuccess(int status, Response<Pokemon> response){
        if(null != uiCallback){
            Pokemon pokemon = response.body();
            String url = pokemon.getSprites().getFrontDefault();
            PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(position).setDefaultFormUrl(url);
            uiCallback.updateView(status,response);
        }
    }

    @Override
    public void onFailure(int status,String errorMessage){
        if(null != uiCallback){
            uiCallback.onError(status,errorMessage);
        }
    }

    public void cancel(){
        if(null != pokemonService){
            uiCallback = null;
            pokemonService.cancel();
        }
    }

}
