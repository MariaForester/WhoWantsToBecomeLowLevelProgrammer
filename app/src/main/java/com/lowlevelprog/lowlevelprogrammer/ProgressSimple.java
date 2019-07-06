package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.LightingColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgressSimple extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    TextView textViewer;
    LinearLayout myLinLayout;
    Button[] progressButtons = new Button[10];
    int[] buttonsIDs = new int[]{
            R.id.progress_btn1_simple, R.id.progress_btn2_simple, R.id.progress_btn3_simple,
            R.id.progress_btn4_simple,
            R.id.progress_btn5_simple, R.id.progress_btn6_simple, R.id.progress_btn7_simple,
            R.id.progress_btn8_simple,
            R.id.progress_btn9_simple, R.id.progress_btn10_simple
    };

    // определяем строковый массив
    List<Integer> scoreValues = new ArrayList<>(
            Arrays.asList(34000, 24000, 19000, 14000, 9000, 7000, 5000, 3000, 2000, 1000));

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
/*
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
        // find in list of score value (by value) and recolor all until the index*/

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
    public void onBackPressed() {
        finish();
    }
}
