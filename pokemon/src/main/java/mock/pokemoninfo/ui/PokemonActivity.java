package mock.pokemoninfo.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mock.pokemoninfo.R;
import mock.pokemoninfo.callbacks.UICallback;
import mock.pokemoninfo.model.Pokemon;
import mock.pokemoninfo.utils.Constants;
import mock.pokemoninfo.utils.InternetUtil;
import mock.pokemoninfo.utils.PokemonUtils;
import mock.pokemoninfo.viewmodel.PokemonActivityVM;
import retrofit2.Response;

public class PokemonActivity extends BaseActivity implements UICallback<Pokemon> {

    private final String TAG = PokemonActivity.class.getSimpleName();
    @BindView(R.id.icon)ImageView imageView;
    @BindView(R.id.tv_name)TextView tvName;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.tv_height) TextView tvHeight;
    @BindView(R.id.tv_weight) TextView tvWeight;
    PokemonActivityVM pokemonActivityVM;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("id");
        int pos = getIntent().getIntExtra("pos",Constants.FAILURE);
        if(Constants.FAILURE != pos){
            String value = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(pos).getName();
            tvName.setText(value);
        }
        pokemonActivityVM = new PokemonActivityVM(PokemonActivity.this);
        getPokemonAttributes(id,pos);
        String url = PokemonUtils.getsPokemonFeedHolder().getPokemonInfo(pos).getDefaultFormUrl();
        Glide.with(getApplicationContext()).load(url).into(imageView);
    }

    private void getPokemonAttributes(String id,int pos){
        if(InternetUtil.isInternetOn(getApplicationContext())){
            showProgressDialog();
            pokemonActivityVM.getPokemonAttributes(id,pos);
        }else{
            String err = getString(R.string.network_error);
            new ShowErrorDialogAndCloseActivity(PokemonActivity.this,PokemonActivity.this)
                    .getAlert(err)
                    .show();
            Log.e(TAG,"getPokemonAttributes() - "+err);
        }
    }

    private void addListeners(){
        if(null == mViewPager || null == tabLayout){
            return;
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updatePokemonImage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    private void updatePokemonImage(int position){
        String url = "";
        switch(position){
            case 0:
                url = pokemon.getSprites().getFrontDefault();
                break;
            case 1:
                url = pokemon.getSprites().getBackDefault();
                break;
            case 2:
                url = pokemon.getSprites().getFrontShiny();
                break;
            case 3:
                url = pokemon.getSprites().getBackShiny();
        }
        Glide.with(getApplicationContext()).load(url).error(R.drawable.no_image_60_60).into(imageView);
    }

    @Override
    public void updateView(int status, Response<Pokemon> response){
        switch (status){
            case Constants.SUCCESS:
                pokemon = response.body();
                Glide.with(getApplicationContext()).load(pokemon.getSprites().getFrontDefault()).error(R.drawable.no_image_60_60).into(imageView);
                String text = getText(R.string.text_weight)+" "+pokemon.getWeight();
                tvWeight.setText(text);
                text = getText(R.string.text_height)+" "+pokemon.getHeight();
                tvHeight.setText(text);
                configureFragment();
                break;
            case Constants.UPDATE_VIEW_ON_ERROR:
                finish();
            default:
        }
        dismiss();
    }

    private void configureFragment(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Constants.FRAGMENT_PAGES);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        for (int index = 0; index < Constants.FRAGMENT_PAGES; index++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(mViewPager);
        addListeners();
    }

    @Override
    public void onError(int status, String errMessage){
        dismiss();
        if(status == Constants.NETWORK_FAILURE){
            String err = getString(R.string.network_error);
            new ShowErrorDialogAndCloseActivity(PokemonActivity.this,PokemonActivity.this)
                    .getAlert(err)
                    .show();
            Log.e(TAG,"onError() - "+err);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(null != pokemonActivityVM){
            pokemonActivityVM.cancel();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int pageCount;
        public SectionsPagerAdapter(FragmentManager fm, int numOfPages) {
            super(fm);
            pageCount = numOfPages;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            String url = "";
            switch(position){
                case 0:
                    url = pokemon.getSprites().getFrontDefault();
                    break;
                case 1:
                    url = pokemon.getSprites().getBackDefault();
                    break;
                case 2:
                    url = pokemon.getSprites().getFrontShiny();
                    break;
                case 3:
                    url = pokemon.getSprites().getBackShiny();
            }
            return PokemonFragment.newInstance(url);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Style-"+(position+1);
        }
    }
}
