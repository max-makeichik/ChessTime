package chess.max.com.chesstime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends ActionBarActivity {

    private static String TAG = "MainActivity";
    public static String TAG_START_PAGE = "StartPage";
    public static String TAG_START_TIME = "MainFragment";
    public Fragment fragment;

    public static int height;
    public static int actionBarSize = 0;
    public static Context context;

    static WeakReference<FragmentActivity> mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        if (savedInstanceState == null) {

            setContentView(R.layout.main);

            Log.d(TAG, "START PAGE - ended MA TOOLBAR");
            fragment = StartPage.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.rel, fragment, TAG_START_PAGE).commit();

            mActivity = new WeakReference<>( (FragmentActivity)this);
        }
        else
            setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    static void openMain(FragmentTransaction transaction){

        mActivity.get().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Fragment fragment;

        fragment = MainFragment.newInstance();
        transaction.replace(R.id.rel, fragment, TAG_START_TIME);
        transaction.addToBackStack(TAG_START_PAGE);
        transaction.commit();

    }

    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "HERE WAS PRESSED BACK");
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_START_TIME);
        if(null != fragment && fragment.isVisible()) {
            Log.d(TAG, "time видим");
            showLeaveTest();
        }
        else {
            Log.d(TAG, "time невидим");
            if (backStackEntryCount == 0)
                super.onBackPressed();
            else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private AlertDialog.Builder builder;
    private void showLeaveTest() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit)
                .setMessage(context.getString(R.string.exit_now))
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "закрыть time");
                        getSupportFragmentManager().popBackStack();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
