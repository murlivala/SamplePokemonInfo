package mock.pokemoninfo.callbacks;

import retrofit2.Response;

/**
 * service callbacks
 */
public interface ServiceCallback<T> {
    void onSuccess(int status, Response<T> response);
    void onFailure(int status,String errorMessage);
}
