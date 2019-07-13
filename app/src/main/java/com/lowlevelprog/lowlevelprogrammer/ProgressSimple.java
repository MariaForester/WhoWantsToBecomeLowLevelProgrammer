package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProgressSimple extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    TextView textViewer;
    LinearLayout myLinLayout;
    HomeWatcher mHomeWatcher;
    Button[] progressButtons = new Button[10];
    boolean soundIsOff;
    int[] buttonsIDs = new int[]{
            R.id.progress_btn1_simple, R.id.progress_btn2_simple, R.id.progress_btn3_simple,
            R.id.progress_btn4_simple,
            R.id.progress_btn5_simple, R.id.progress_btn6_simple, R.id.progress_btn7_simple,
            R.id.progress_btn8_simple,
            R.id.progress_btn9_simple, R.id.progress_btn10_simple
    };

    // music
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_simple);

        myLayout = findViewById(R.id.progress_lay_simple);
        myLinLayout = findViewById(R.id.prog_linear_1_simple);

        // animated background
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // music
        soundIsOff = MainActivity.soundIsOff;
        if (soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        // Coloring buttons (unactivated)
        for (int i = 0; i < buttonsIDs.length; i++) {
            progressButtons[i] = findViewById(buttonsIDs[i]);
            progressButtons[i].getBackground().setColorFilter(new
                    LightingColorFilter(0xFF292973, 0x00000000));
            progressButtons[i].setText(R.string.question_signs);
            progressButtons[i].invalidate();
        }

        // На каждом шаге отображение текущего счета
        List<Integer> listOfScores = SimpleMode.listForProgress;
        for (int i = 9; i > 9 - listOfScores.size(); i--) {
            progressButtons[i] = findViewById(buttonsIDs[i]);
            progressButtons[i].getBackground().setColorFilter(new
                    LightingColorFilter(0xFFFFBC00, 0x00000000));
            String str = "\u20BF" + listOfScores.get(9 - i);
            progressButtons[i].setText(str);
            progressButtons[i].invalidate();
        }

        textViewer = findViewById(R.id.progress_counter_simple);
        // Timer
        new CountDownTimer(5000, 1000) {

            public void onTick(long l) {
                textViewer.setText(String.valueOf(l / 1000));
            }

            public void onFinish() {
                textViewer.setText("-");
                finish();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    @Override   // disabling back button during the splash screen
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).getView();
        return false;
    }
}