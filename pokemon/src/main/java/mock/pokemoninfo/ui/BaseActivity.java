package mock.pokemoninfo.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mock.pokemoninfo.R;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog(){
        if(null == mProgressDialog){
            mProgressDialog = ProgressDialog.show(this, "", null , false, true);
            mProgressDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
            mProgressDialog.setContentView(R.layout.progress_bar);
            mProgressDialog.setCancelable(true);
        }
    }

    public void dismiss(){
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }catch(IllegalArgumentException e){
            Log.e(TAG, "dismiss() - IllegalArgumentException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "dismiss() - Exception: " + e.getMessage());
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        dismiss();
    }
}
