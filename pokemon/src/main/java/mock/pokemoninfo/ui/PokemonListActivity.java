package mock.pokemoninfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mock.pokemoninfo.BuildConfig;
import mock.pokemoninfo.callbacks.ItemClickCallback;
import mock.pokemoninfo.callbacks.UICallback;
import mock.pokemoninfo.model.PokemonInfo;
import mock.pokemoninfo.utils.Constants;
import mock.pokemoninfo.adapter.CustomAdapter;
import mock.pokemoninfo.utils.InternetUtil;
import mock.pokemoninfo.viewmodel.PokemonListActivityVM;
import mock.pokemoninfo.wrapper.LinearLayoutManagerWrapper;
import mock.pokemoninfo.R;
import mock.pokemoninfo.utils.PokemonUtils;
import retrofit2.Response;

public class PokemonListActivity extends BaseActivity implements UICallback<PokemonInfo>,ItemClickCallback{

    private final String TAG = PokemonListActivity.class.getSimpleName();
    @BindView(R.id.tv_header_name) TextView tvHeader;
    @BindView(R.id.header_icon) ImageView imageView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private int scrollToPos = 0;
    private PokemonListActivityVM pokemonListActivityVM;
    private int lastViewedItem = RecyclerView.NO_POSITION;
    private int lastViewedItemForTopbar = RecyclerView.NO_POSITION;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        createViewModel();
        setView();
        setHandler();
        getPokemonList();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(lastViewedItem != RecyclerView.NO_POSITION){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);//right to left slide
            mAdapter.notifyItemChanged(lastViewedItem);
            String name = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(lastViewedItem).getName();
            String text = getText(R.string.text_last_viewed)+" "+name.toUpperCase();
            tvHeader.setText(text);
            String url = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(lastViewedItem).getDefaultFormUrl();
            Glide.with(getApplicationContext()).load(url).error(R.mipmap.ic_launcher).into(imageView);
            lastViewedItemForTopbar = lastViewedItem;
            lastViewedItem = RecyclerView.NO_POSITION;
        }
        if(null != mHandler){
            animateTopbar(Constants.MESSAGE_TOTAL_POKEMON,Constants.ANIMATION_DURATION);
        }
    }

    private void setHandler(){
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch(inputMessage.what){
                    case Constants.MESSAGE_TOTAL_POKEMON:
                    case Constants.MESSAGE_LOADED_POKEMON:
                    case Constants.MESSAGE_LAST_VIEWED_POKEMON:
                        updateTopbar(inputMessage.what);
                        break;
                    default:
                            /*
                             * Pass along other messages from the UI
                             */
                        super.handleMessage(inputMessage);
                }
            }
        };
    }

    private void createViewModel(){
        pokemonListActivityVM = new PokemonListActivityVM(getApplicationContext(),PokemonListActivity.this,PokemonListActivity.this);
        mAdapter = pokemonListActivityVM.getAdapter();
    }

    private void setView(){
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerView.setAdapter(mAdapter);
    }

    private void getPokemonList(){
        if(InternetUtil.isInternetOn(getApplicationContext())){
            isLoading = true;
            showProgressDialog();
            removeAllMessages();
            tvHeader.setText(getText(R.string.text_loading));
            pokemonListActivityVM.getPokemonList();
        }else{
            String err = getString(R.string.network_error);
            new ShowErrorDialogAndTryAgain(PokemonListActivity.this,PokemonListActivity.this)
                    .getAlert(err)
                    .show();
            Log.e(TAG,"getPokemonList() - "+err);
        }
    }

    public void showPokemon(int pos){
        String id = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(pos).getUrl();
        String key = "pokemon/";
        id = id.substring(id.indexOf(key)+key.length(),id.lastIndexOf("/"));
        final Intent intent = new Intent();
        intent.setClassName(getPackageName(),getPackageName()+".ui.PokemonActivity");
        intent.putExtra("id",id);
        intent.putExtra("pos",pos);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//left to right slide
        if(BuildConfig.DEBUG){
            Log.d(TAG,"showPokemon() - id: "+id);
        }
        lastViewedItem = pos;
    }

    private void updateTopbar(int what){
        String text = "";
        switch(what){
            case Constants.MESSAGE_TOTAL_POKEMON:
                int total = PokemonUtils.getsPokemonFeedHolder().getPokemonCount();
                text = getText(R.string.text_total_pokemon)+" "+total;
                tvHeader.setText(text);
                animateTopbar(Constants.MESSAGE_LOADED_POKEMON,Constants.ANIMATION_DURATION);
                break;
            case Constants.MESSAGE_LOADED_POKEMON:
                int loaded = PokemonUtils.getsPokemonFeedHolder().getPokemonInfoList().size();
                text = getText(R.string.text_loaded)+" "+loaded;
                tvHeader.setText(text);
                animateTopbar(Constants.MESSAGE_LAST_VIEWED_POKEMON,Constants.ANIMATION_DURATION);
                break;
            case Constants.MESSAGE_LAST_VIEWED_POKEMON:
                if(lastViewedItemForTopbar != RecyclerView.NO_POSITION){
                    String name = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(lastViewedItemForTopbar).getName();
                    text = getText(R.string.text_last_viewed)+" "+name.toUpperCase();
                    tvHeader.setText(text);
                }
                animateTopbar(Constants.MESSAGE_TOTAL_POKEMON,Constants.ANIMATION_DURATION);
                break;
            default:

        }
    }

    private void animateTopbar(int what,int duration){
        mHandler.sendMessageDelayed(getMessage(what),duration);
    }

    private Message getMessage(int what){
        return mHandler.obtainMessage(what);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            if(layoutManager.getItemCount() >= PokemonUtils.getsPokemonFeedHolder().getPokemonCount()){
                if(BuildConfig.DEBUG){
                    Log.d(TAG,"onScrolled - All Fetched !");
                }
                if(lastVisibleItemPosition+1 >= PokemonUtils.getsPokemonFeedHolder().getPokemonCount()){
                    Snackbar.make(recyclerView, "All pokemons loaded.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else if(!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0){
                if(BuildConfig.DEBUG){
                    Log.d(TAG,"onScrolled - Time to fetch new page...");
                }
                getPokemonList();
                scrollToPos = lastVisibleItemPosition;
            }
        }
    };

    @Override
    public void onItemClick(int position){
        showPokemon(position);
    }

    @Override
    public void updateView(int status ,Response<PokemonInfo> response){

        switch(status){
            case Constants.SUCCESS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(scrollToPos);
                        removeAllMessages();
                        animateTopbar(Constants.MESSAGE_LOADED_POKEMON,Constants.ANIMATION_DURATION_SHORT);

                    }
                });
                break;
            case Constants.UPDATE_VIEW_ON_ERROR:
                /**
                 * Placeholder for case when user presses OK on error dialog
                 */
                break;
            case Constants.TRY_AGAIN_ON_ERROR:
                getPokemonList();
                break;
            default:
        }
        isLoading = false;
        dismiss();
    }

    @Override
    public void onError(int status, String errMessage){
        isLoading = false;
        dismiss();
        if(status == Constants.NETWORK_FAILURE){
            removeAllMessages();
            animateTopbar(Constants.MESSAGE_LOADED_POKEMON,Constants.ANIMATION_DURATION_SHORT);
            String err = getString(R.string.network_error);
            new ShowErrorDialogAndTryAgain(PokemonListActivity.this,PokemonListActivity.this)
                    .getAlert(err)
                    .show();
            Log.e(TAG,"onError() - "+err);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        removeAllMessages();
    }

    private void removeAllMessages(){
        if(null != mHandler){
            mHandler.removeMessages(Constants.MESSAGE_TOTAL_POKEMON);
            mHandler.removeMessages(Constants.MESSAGE_LOADED_POKEMON);
            mHandler.removeMessages(Constants.MESSAGE_LAST_VIEWED_POKEMON);
        }
    }

    @Override
    protected void onDestroy(){
        if(isFinishing()){
            PokemonUtils.releasePokemonFeedHolder();
            if(null != pokemonListActivityVM){
                pokemonListActivityVM.cancel();
            }
        }
        super.onDestroy();
    }

}
