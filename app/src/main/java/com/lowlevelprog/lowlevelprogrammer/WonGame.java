package com.lowlevelprog.lowlevelprogrammer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class WonGame extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    ImageView winImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_game);

        myLayout = findViewById(R.id.won_game);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // Image from Firebase
        winImage = findViewById(R.id.imageView_win);
        String winUrl = "https://firebasestorage.googleapis.com/v0/b/low-level-programmer.appspot." +
                "com/o/LocWIn%2FWebp.net-resizeimage.png?alt=media&token=41f7d58d-421f-4b32-a1ab-" +
                "4561e9d4fd33";
        Glide.with(getApplicationContext()).load(winUrl).into(winImage);
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
                Intent intent = new Intent(WonGame.this, MainActivity.class);
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
