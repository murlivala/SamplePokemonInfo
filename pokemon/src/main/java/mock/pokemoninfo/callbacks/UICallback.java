package mock.pokemoninfo.callbacks;

import retrofit2.Response;

/**
 * callbacks to update ui
 */
public interface UICallback<T> {
    void updateView(int status, Response<T> response);
    void onError(int status, String errMessage);
}
