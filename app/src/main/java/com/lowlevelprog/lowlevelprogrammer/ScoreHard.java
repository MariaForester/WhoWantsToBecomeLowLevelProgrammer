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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_hard);

        myLayout = findViewById(R.id.score_hard);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

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
        finish();
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
