package mock.pokemoninfo.viewmodel;

import android.content.Context;
import android.util.Log;

import java.util.List;

import mock.pokemoninfo.BuildConfig;
import mock.pokemoninfo.adapter.CustomAdapter;
import mock.pokemoninfo.callbacks.ItemClickCallback;
import mock.pokemoninfo.callbacks.ServiceCallback;
import mock.pokemoninfo.callbacks.UICallback;
import mock.pokemoninfo.model.PokemonInfo;
import mock.pokemoninfo.model.Result;
import mock.pokemoninfo.service.PokemonListService;
import mock.pokemoninfo.utils.PokemonUtils;
import retrofit2.Response;

public class PokemonListActivityVM implements ServiceCallback<PokemonInfo>,ItemClickCallback {
    private final String TAG = PokemonListActivityVM.class.getSimpleName();
    private CustomAdapter mAdapter;
    private PokemonListService pokemonListService;
    private UICallback uiCallback;
    private ItemClickCallback itemClickCallback;
    public PokemonListActivityVM(Context context, UICallback uiCallback,ItemClickCallback itemClickCallback){
        mAdapter = new CustomAdapter(context, PokemonListActivityVM.this);
        this.itemClickCallback = itemClickCallback;
        this.uiCallback = uiCallback;
    }

    public void processResponse(final int status,final Response<PokemonInfo> response){
        Thread t = new Thread(){
            public void run(){
                processResponseInThread(status,response);
            }
        };
        t.start();
    }

    private void processResponseInThread(final int status,Response<PokemonInfo> response){
        if(null != uiCallback){
            final PokemonInfo pokemonInfo = response.body();
            List<Result> results = pokemonInfo.getResults();
            PokemonUtils.getsPokemonFeedHolder().setNextPage(pokemonInfo.getNext());
            PokemonUtils.getsPokemonFeedHolder().setPokemonCount(pokemonInfo.getCount());
            for(int i=0;i<results.size();i++){
                PokemonUtils.getsPokemonFeedHolder().addPokemonInfo(results.get(i));
            }
            if(BuildConfig.DEBUG){
                Log.d(TAG,"####=>"+pokemonInfo.getCount());
                Log.d(TAG,"####=>"+pokemonInfo.getNext());
                Log.d(TAG,"#### Total item fetched:"+PokemonUtils.getsPokemonFeedHolder().getPokemonInfoList().size());
            }
            uiCallback.updateView(status,response);
        }
    }
    public void onSuccess(int status, Response<PokemonInfo> response){
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(),status);
        processResponse(status,response);
    }

    public void onFailure(int status,String errorMessage){
        if(null != uiCallback){
            uiCallback.onError(status,errorMessage);
        }
    }

    public CustomAdapter getAdapter(){
        return mAdapter;
    }

    @Override
    public void onItemClick(int position){
        if(BuildConfig.DEBUG){
            Log.d(TAG,"onItemClick - pos:"+position);
            Log.d(TAG,"onItemClick - name:"+ PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(position).getName());
        }
        if(null != itemClickCallback){
            itemClickCallback.onItemClick(position);
        }
    }

    public void getPokemonList(){
        if(null == pokemonListService){
            pokemonListService = new PokemonListService(this);
            pokemonListService.start();
        }else{
            String query = PokemonUtils.getsPokemonFeedHolder().getNextPage();
            query = query.substring(query.indexOf("=")+1);
            pokemonListService.request(query);
            Log.d(TAG,"getPokemonList() - query: "+query);
        }
    }

    public void cancel(){
        if(null != pokemonListService){
            uiCallback = null;
            pokemonListService.cancel();
        }
    }

}
