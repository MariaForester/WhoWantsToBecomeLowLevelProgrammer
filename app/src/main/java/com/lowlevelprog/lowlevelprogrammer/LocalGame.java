package com.lowlevelprog.lowlevelprogrammer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocalGame extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    private Button button_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);

        myLayout = findViewById(R.id.local_game);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        button_start = findViewById(R.id.but_start_loc); // start the game
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(v.getId());
            }
        });
    }

    // There is a button to start a game. That is what happens when it`s clicked
    public void startGame(int identifier) {
        Intent intent;
        if (identifier == R.id.but_start_loc) {
            intent = new Intent(this, LocalGamePlayMode.class);
            startActivity(intent);
        }
    }

    // Closing activity when the back button is pressed
    @Override
    public void onBackPressed() {
       finish();
    }
}
