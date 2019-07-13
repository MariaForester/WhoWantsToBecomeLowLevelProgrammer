package com.lowlevelprog.lowlevelprogrammer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.DialogInterface;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.line.LineTextView;

public class MainActivity extends AppCompatActivity {

    LineTextView hTextView;
    Button button_local, button_multiplayer;
    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    HomeWatcher mHomeWatcher;
    static boolean soundIsOff = false;

    // variables for music
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
        setContentView(R.layout.activity_main);

        myLayout = findViewById(R.id.layout_main);

        findViewById(R.id.soundRegulator).setBackgroundResource(R.drawable.volume_up);

        // Animated background
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // binding music service
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        //pausing when needed
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

        // Upper text - The Title
        hTextView = findViewById(R.id.tv_htext); // block on above
        hTextView.setLineColor(R.color.textColor);
        hTextView.setAnimationListener(new SimpleAnimationListener(this));
        hTextView.animateText(getString(R.string.app_full_name));

        // Clickable buttons
        button_local = findViewById(R.id.but_loc_game); // open one of game modes
        button_multiplayer = findViewById(R.id.but_multip_game);
        button_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameMode(v.getId());
            }
        });
        button_multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameMode(v.getId());
            }
        });
    }

    // Opening the game mode according to the button clicked
    public void openGameMode(int identifier) {
        Intent intent;
        if (identifier == R.id.but_loc_game){
            intent = new Intent(this, LocalGame.class);
        }
        else
            intent = new Intent(this, MultiplayerGame.class);
        startActivity(intent);
    }

    // Animation on the title
    class SimpleAnimationListener implements AnimationListener {

        private Context context;

        SimpleAnimationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onAnimationEnd(HTextView hTextView) {
            hTextView.animateText(getString(R.string.app_full_name));
        }

    }

    // An opportunity to close the app on back button pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно хотите выйти?");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).setNegativeButton("0", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Showing the alert dialogue if there is no connection
    @Override
    protected void onResume() {
        super.onResume();

        // concerning connection
        if (!isConnected(MainActivity.this)) {
            buildDialog(MainActivity.this).show();
        }

        // concerning music playing
        if (mServ != null && !soundIsOff) {
            mServ.resumeMusic();
        }

    }

    // when the screen is idle or power off
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
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);
    }

    // Checking if there is an Internet connection
    public boolean isConnected(Context context) throws NullPointerException {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null &&
                    wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    // The alert Dialogue itself. Gives an opportunity to connect the device and restart the app
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(false);
        builder.setTitle("Нет доступа к Интернету");
        builder.setMessage("Подключите ваше устройство для продолжения");
        builder.setPositiveButton("Выход", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("Продолжить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, Splash_Intro.class);
                finish();
                startActivity(i);
            }
        });
        return builder;
    }

    // включить и выключить звук по кнопке
    public void soundOff(View view) {
        if (!soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
            findViewById(R.id.soundRegulator).setBackgroundResource(R.drawable.volume_down);
            soundIsOff = true;
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
            findViewById(R.id.soundRegulator).setBackgroundResource(R.drawable.volume_up);
            soundIsOff = false;
        }
    }
}