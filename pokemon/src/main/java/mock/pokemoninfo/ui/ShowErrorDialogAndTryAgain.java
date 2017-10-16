package mock.pokemoninfo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import mock.pokemoninfo.callbacks.UICallback;
import mock.pokemoninfo.utils.Constants;


public class ShowErrorDialogAndTryAgain {
    private final Context context;
    UICallback uiCallback;
    public ShowErrorDialogAndTryAgain(Context context, UICallback callback) {
        this.context = context;
        uiCallback = callback;
    }

    public AlertDialog.Builder getAlert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);

        alert.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uiCallback.updateView(Constants.TRY_AGAIN_ON_ERROR,null);
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uiCallback.updateView(Constants.UPDATE_VIEW_ON_ERROR,null);
                dialog.dismiss();
            }
        });
        return alert;
    }
}