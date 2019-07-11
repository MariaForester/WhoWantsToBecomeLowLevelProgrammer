package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowlevelprog.lowlevelprogrammer.Model.QuestionScore;

public class ScoreSimple extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    ImageView scoreSimpleImage;
    TextView scoreSimpleTextView;
    FirebaseDatabase db;
    DatabaseReference questionsScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_simple);

        myLayout = findViewById(R.id.score_simple);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // Set up score on the screen
        scoreSimpleTextView = findViewById(R.id.textView_simple_score);
        String str = "Ваш результат:\n\u20BF " + SimpleMode.score;
        scoreSimpleTextView.setText(str);

        // Image from Firebase
        scoreSimpleImage = findViewById(R.id.imageView_simple_score);
        String scoreUrl = "https://firebasestorage.googleapis.com/v0/b/low-level-programmer." +
                "appspot.com/o/OnlineGame%2FSimple%2Fscoore.png?alt=media&token=371c1a83-9c24-" +
                "48eb-b3d3-541aae43070d";
        Glide.with(getApplicationContext()).load(scoreUrl).into(scoreSimpleImage);

        db = FirebaseDatabase.getInstance();
        questionsScore = db.getReference("QuestionScore");

        questionsScore.child(String.format("%s_%s", OnlineHelper.currentUser.getUserName(),
                OnlineHelper.modeID))
                .setValue(new QuestionScore(String.format("%s_%s",
                        OnlineHelper.currentUser.getUserName(), OnlineHelper.modeID),
                        OnlineHelper.currentUser.getUserName(),
                        String.valueOf(SimpleMode.score), OnlineHelper.modeID,
                        OnlineHelper.modeName));
    }

    public void backHome(View view) {
        Intent intent = new Intent(ScoreSimple.this, SimpleMode.class);
        startActivity(intent);
        this.finish();
    }

    // Whether to play the game again or not
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно не хотите сыграть еще раз");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ScoreSimple.this, MultplayerHome.class);
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
