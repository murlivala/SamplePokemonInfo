package mock.pokemoninfo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import mock.pokemoninfo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PokemonFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private final static String URL = "pokemonUrl";

    public PokemonFragment(){

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PokemonFragment newInstance(String url) {
        PokemonFragment fragment = new PokemonFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pokemon, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.icon);
        Glide.with(this).load(getArguments().getString(URL)).error(R.drawable.no_image_60_60).into(imageView);
        return rootView;
    }
}
