package chess.max.com.chesstime;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "MainFragment";

    TextView time1;
    TextView time2;
    Button pause;

    float pressed = 0.3f;

    String secondsShow, minutesShow;

    boolean paused = false;
    int fieldPressed = 2;  //  1 - second pressed, 0 - first pressed, 2 - nothing pressed

    int minutes1, seconds1 = 0;

    int minutes2, seconds2 = 0;

    boolean needAddMinus1 = false;
    boolean needAddMinus2 = false;

    public MainFragment() {
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.d(TAG, "onCreateView");
        if (view == null) {

            this.minutes1 = StartPage.minutes;
            minutes2 = this.minutes1;

            Log.d(TAG, "view == null");
            view = inflater.inflate(R.layout.fragment, container, false);

            time1 = (TextView) view.findViewById(R.id.time1);
            time2 = (TextView) view.findViewById(R.id.time2);
            pause = (Button) view.findViewById(R.id.pause);
            pause.setClickable(false);

            time1.setOnClickListener(this);
            time2.setOnClickListener(this);
            pause.setOnClickListener(this);
            setStartTime();

        } else {

            Log.d(TAG, "view not null");
            ((ViewGroup) view.getParent()).removeView(view);
            view = inflater.inflate(R.layout.fragment, container, false);

        }
        return view;
    }

    void count(int seconds, int minutes, int source) {
        --seconds;
        if (seconds < 0) {
            --minutes;
            if (minutes < 0) {
                if (source == 1)
                    needAddMinus1 = true;
                else    //  == 2
                    needAddMinus2 = true;
            }
            seconds = 59;
        }

        Log.d(TAG, "minutes before" + minutes + ", sec before" + seconds);

        secondsShow = Integer.toString(seconds);
        minutesShow = Integer.toString(minutes);
        if (seconds / 10 <= 0)
            secondsShow = "0" + seconds;
        if (minutes / 10 <= 0)
            minutesShow = "0" + minutes;
        if (minutes == 0 && seconds == 0) {
            if (source == 1)
                needAddMinus1 = true;
            else
                needAddMinus2 = true;
        }

        Log.d(TAG, "minutes after" + minutes + ", sec after" + seconds);

        showMinutes(source);
        set(seconds, minutes, source);
    }

    void countReverse(int seconds, int minutes, int source) {
        ++seconds;
        if (seconds >59) {
            ++minutes;
            seconds = 0;
        }

        Log.d(TAG, "minutes before" + minutes + ", sec before" + seconds);

        secondsShow = Integer.toString(seconds);
        minutesShow = Integer.toString(minutes);
        if (seconds / 10 <= 0)
            secondsShow = "0" + seconds;
        if (minutes / 10 <= 0)
            minutesShow = "0" + minutes;

        Log.d(TAG, "minutes after" + minutes + ", sec after" + seconds);

        showMinutes(source);
        set(seconds, minutes, source);
    }

    void showMinutes(int source) {
        if (source == 1) {
            if (!needAddMinus1) {
                time1.setText(minutesShow + ":" + secondsShow);
            } else {
                time1.setText("-" + minutesShow + ":" + secondsShow);
            }
        } else {
            if (!needAddMinus2)
                time2.setText(minutesShow + ":" + secondsShow);
            else
                time2.setText("-" + minutesShow + ":" + secondsShow);
        }
    }

    void set(int seconds, int minutes, int source) {
        if (source == 1) {
            seconds1 = seconds;
            minutes1 = minutes;
        } else {
            seconds2 = seconds;
            minutes2 = minutes;
        }
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable1 = new Runnable() {

        @Override
        public void run() {
            Log.d(TAG, "here run1");
            if(!needAddMinus1)
                count(seconds1, minutes1, 1);
            else
                countReverse(seconds1, minutes1, 1);
            timerHandler.postDelayed(this, 1000);
        }
    };

    Runnable timerRunnable2 = new Runnable() {

        @Override
        public void run() {
            Log.d(TAG, "here run2");
            if(!needAddMinus2)
                count(seconds2, minutes2, 2);
            else
                countReverse(seconds2, minutes2, 2);
            timerHandler.postDelayed(this, 1000);
        }
    };

    void setStartTime() {
        time1.setText(minutes1 + ":00");
        time2.setText(minutes1 + ":00");
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
        timerHandler.removeCallbacks(timerRunnable1);
        timerHandler.removeCallbacks(timerRunnable2);
        time1.setClickable(false);
        time2.setClickable(false);
        pause.setText("Старт");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.time1:

                time1.setAlpha(pressed);
                time2.setAlpha(1f);

                fieldPressed = 0;
                Log.d(TAG, "time1 CLICK");
                timerHandler.removeCallbacks(timerRunnable1);
                timerHandler.postDelayed(timerRunnable2, 0);

                time1.setClickable(false);
                time2.setClickable(true);

                pause.setClickable(true);
                break;

            case R.id.time2:

                time1.setAlpha(1f);
                time2.setAlpha(pressed);

                fieldPressed = 1;
                Log.d(TAG, "time2 CLICK");
                timerHandler.removeCallbacks(timerRunnable2);
                timerHandler.postDelayed(timerRunnable1, 0);

                time2.setClickable(false);
                time1.setClickable(true);

                pause.setClickable(true);
                break;

            case R.id.pause:
                if (pause.getText().equals(getActivity().getString(R.string.pause))) {

                    time1.setAlpha(0.5f);
                    time2.setAlpha(0.5f);

                    paused = true;
                    time1.setClickable(false);
                    time2.setClickable(false);

                    if (fieldPressed == 1)
                        timerHandler.removeCallbacks(timerRunnable1);
                    else if (fieldPressed == 0)
                        timerHandler.removeCallbacks(timerRunnable2);
                    else{
                        time1.setClickable(true);
                        time2.setClickable(true);
                    }
                    pause.setText(getActivity().getString(R.string.start));
                } else {

                    paused = false;

                    if (fieldPressed == 1) {
                        timerHandler.postDelayed(timerRunnable1, 0);
                        time1.setClickable(true);
                        time1.setAlpha(1f);
                        time2.setAlpha(pressed);
                    } else if(fieldPressed == 0){
                        timerHandler.postDelayed(timerRunnable2, 0);
                        time2.setClickable(true);
                        time1.setAlpha(pressed);
                        time2.setAlpha(1f);
                    }
                    else{
                        time1.setClickable(true);
                        time2.setClickable(true);
                        time1.setAlpha(1f);
                        time2.setAlpha(1f);
                    }
                    pause.setText(getActivity().getString(R.string.pause));
                }
        }
    }

    public static Fragment newInstance() {
        MainFragment myFragment = new MainFragment();
        return myFragment;
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "MainFragment onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainFragment onDestroy");
    }
}