package chess.max.com.chesstime;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;

import static chess.max.com.chesstime.MainActivity.*;

public class StartPage extends Fragment implements View.OnClickListener{

    private static String TAG = "StartPage";
    private static Button start;

    SharedPreferences prefs;
    SharedPreferences.Editor ed;

    NumberPicker np2;

    static int minutes;

    CheckBox save;

    public StartPage() {
    }

    View view;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        if(view == null) {

            prefs = getActivity().getSharedPreferences("time", MODE_PRIVATE);
            Log.d(TAG, "view == null");
            view = inflater.inflate(R.layout.start_page, container, false);

            np2 = (NumberPicker) view.findViewById(R.id.numberPicker2);

            String[] nums_minutes = new String[60];
            for(int i = 0; i < nums_minutes.length; i++)
                nums_minutes[i] = Integer.toString(i);

            np2.setDisplayedValues(nums_minutes);
            np2.setWrapSelectorWheel(false);

            np2.setMaxValue(59);
            np2.setMinValue(0);

            minutes = prefs.getInt("minutes", 5);
            np2.setValue(minutes);

            start = (Button) view.findViewById(R.id.start);
            start.setOnClickListener(this);

            save = (CheckBox) view.findViewById(R.id.save);

        }
        else {

            Log.d(TAG, "view not null");
            ((ViewGroup) view.getParent()).removeView(view);

        }

        return view;
    }

    void save(){
        prefs = getActivity().getSharedPreferences("time", MODE_PRIVATE);
        ed = prefs.edit();

        ed.putInt("minutes", minutes).apply();
        Log.d(TAG, "minutes " + np2.getValue());
        ed.apply();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.start:

                minutes = np2.getValue();
                if(save.isChecked())
                    save();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity.openMain(transaction);

                break;

        }
    }

    public static Fragment newInstance() {
        StartPage myFragment = new StartPage();
        return myFragment;
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "StartPage onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "StartPage onDestroy");
    }
}