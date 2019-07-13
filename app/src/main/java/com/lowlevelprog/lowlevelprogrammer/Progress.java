package com.lowlevelprog.lowlevelprogrammer;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Progress extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    TextView textViewer;
    LinearLayout myLinLayout;
    Button[] progressButtons = new Button[10];
    HomeWatcher mHomeWatcher;
    boolean soundIsOff;
    int[] buttonsIDs = new int[]{
            R.id.progress_btn1, R.id.progress_btn2, R.id.progress_btn3, R.id.progress_btn4,
            R.id.progress_btn5, R.id.progress_btn6, R.id.progress_btn7, R.id.progress_btn8,
            R.id.progress_btn9, R.id.progress_btn10
    };

    // определяем строковый массив
    List<Integer> scoreValues = new ArrayList<>(
            Arrays.asList(34000, 24000, 19000, 14000, 9000, 7000, 5000, 3000, 2000, 1000));

    // music
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        myLayout = findViewById(R.id.progress_lay);
        myLinLayout = findViewById(R.id.prog_linear_1);

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
            progressButtons[i].invalidate();
        }

        // For buttons with correct answers
        int myScore = LocalGamePlayMode.score;
        int indexRecolor = scoreValues.indexOf(myScore);
        for (int i = 9; i >= indexRecolor; i--) {
            progressButtons[i] = findViewById(buttonsIDs[i]);
            progressButtons[i].getBackground().setColorFilter(new
                    LightingColorFilter(0xFFFFBC00, 0x00000000));
            progressButtons[i].invalidate();
        }
        // find in list of score value (by value) and recolor all until the index

        textViewer = findViewById(R.id.progress_counter);
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
        music.setClass(this,MusicService.class);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
