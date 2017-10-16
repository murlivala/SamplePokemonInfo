package mock.pokemoninfo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Semaphore;

import mock.pokemoninfo.callbacks.ServiceCallback;
import mock.pokemoninfo.model.Pokemon;
import mock.pokemoninfo.model.PokemonInfo;
import mock.pokemoninfo.service.PokemonListService;
import mock.pokemoninfo.service.PokemonService;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PokemonInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("mock.pokemoninfo", appContext.getPackageName());
    }

    @Test
    public void testPokemonListService() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                assertNotNull(response);
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void testPokemonService() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonService pokemonService = new PokemonService(new ServiceCallback<Pokemon>() {
            @Override
            public void onSuccess(int status, Response<Pokemon> response) {
                assertNotNull(response);
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonService.start();
        pokemonService.request(PokemonTestData.ID_1);
        semaphore.acquire();
    }

    @Test
    public void countPokemon() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.TOTAL_POKEMONS,pokemonInfo.getCount().intValue());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void checkItemCount() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.COUNT,pokemonInfo.getResults().size());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });

        pokemonListService.start();
        semaphore.acquire();
    }


    @Test
    public void checkFirstPokemonName() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.POKEMON_NAME_1,pokemonInfo.getResults().get(0).getName());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void checkEleventhPokemonName() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.POKEMON_NAME_11,pokemonInfo.getResults().get(10).getName());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void checkSecondPageUrl() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.SECOND_PAGE_URL,pokemonInfo.getNext());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void checkFirstPokemonUrl() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        PokemonListService pokemonListService = new PokemonListService(new ServiceCallback<PokemonInfo>() {
            @Override
            public void onSuccess(int status, Response<PokemonInfo> response) {
                PokemonInfo pokemonInfo = response.body();
                assertEquals(PokemonTestData.FIRST_POKEMON_URL,pokemonInfo.getResults().get(0).getUrl());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonListService.start();
        semaphore.acquire();
    }

    @Test
    public void testWeight() throws Exception{
        final Semaphore semaphore = new Semaphore(0);
        PokemonService pokemonService = new PokemonService(new ServiceCallback<Pokemon>() {
            @Override
            public void onSuccess(int status, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                assertEquals(PokemonTestData.WEIGHT_ID_1,pokemon.getWeight().intValue());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });
        pokemonService.start();
        pokemonService.request(PokemonTestData.ID_1);
        semaphore.acquire();
    }

    @Test
    public void testHeight() throws Exception{
        final Semaphore semaphore = new Semaphore(0);
        final PokemonService pokemonService = new PokemonService(new ServiceCallback<Pokemon>() {
            @Override
            public void onSuccess(int status, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                assertEquals(PokemonTestData.HEIGHT_ID_1,pokemon.getHeight().intValue());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });

        pokemonService.start();
        pokemonService.request(PokemonTestData.ID_1);
        semaphore.acquire();
    }

    @Test
    public void testImageUrl() throws Exception{
        final Semaphore semaphore = new Semaphore(0);
        final PokemonService pokemonService = new PokemonService(new ServiceCallback<Pokemon>() {
            @Override
            public void onSuccess(int status, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                assertEquals(PokemonTestData.POKEMON_FRONT_DEFAULT_IMAGE_URL_ID_1,pokemon.getSprites().getFrontDefault());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {
                semaphore.release();
                assertNotNull(null);
            }
        });

        pokemonService.start();
        pokemonService.request(PokemonTestData.ID_1);
        semaphore.acquire();
    }
}
