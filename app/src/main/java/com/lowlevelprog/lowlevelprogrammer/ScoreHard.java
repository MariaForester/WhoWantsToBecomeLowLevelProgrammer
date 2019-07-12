package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowlevelprog.lowlevelprogrammer.Model.QuestionScore;

public class ScoreHard extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    ImageView scoreHardImage;
    TextView scoreHardTextView;
    FirebaseDatabase db;
    DatabaseReference questionsScore;
    HomeWatcher mHomeWatcher;

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
        setContentView(R.layout.activity_score_hard);

        myLayout = findViewById(R.id.score_hard);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // music
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

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

        // Set up score on the screen
        scoreHardTextView = findViewById(R.id.textView_hard_score);
        String str = "Ваш результат:\n\u20BF " + HardMode.score;
        scoreHardTextView.setText(str);

        // Image from Firebase
        scoreHardImage = findViewById(R.id.imageView_hard_score);
        String scoreUrl = "https://firebasestorage.googleapis.com/v0/b/low-level-programmer." +
                "appspot.com/o/OnlineGame%2FSimple%2Fscoore.png?alt=media&token=371c1a83-9c24-" +
                "48eb-b3d3-541aae43070d";
        Glide.with(getApplicationContext()).load(scoreUrl).into(scoreHardImage);

        db = FirebaseDatabase.getInstance();
        questionsScore = db.getReference("QuestionScore");

        questionsScore.child(String.format("%s_%s", OnlineHelper.currentUser.getUserName(),
                OnlineHelper.modeID))
                .setValue(new QuestionScore(String.format("%s_%s",
                        OnlineHelper.currentUser.getUserName(), OnlineHelper.modeID),
                        OnlineHelper.currentUser.getUserName(),
                        String.valueOf(HardMode.score), OnlineHelper.modeID,
                        OnlineHelper.modeName));
    }

    public void backHome(View view) {
        Intent intent = new Intent(ScoreHard.this, HardMode.class);
        startActivity(intent);
        this.finish();
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

    // Whether to play the game again or not
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно не хотите сыграть еще раз");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ScoreHard.this, MultplayerHome.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("0", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
