package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.LightingColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProgressHard extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    TextView textViewer;
    LinearLayout myLinLayout;
    Button[] progressButtons = new Button[10];
    int[] buttonsIDs = new int[]{
            R.id.progress_btn1_hard, R.id.progress_btn2_hard, R.id.progress_btn3_hard,
            R.id.progress_btn4_hard,
            R.id.progress_btn5_hard, R.id.progress_btn6_hard, R.id.progress_btn7_hard,
            R.id.progress_btn8_hard,
            R.id.progress_btn9_hard, R.id.progress_btn10_hard
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_hard);

        myLayout = findViewById(R.id.progress_lay_hard);
        myLinLayout = findViewById(R.id.prog_linear_1_hard);

        // animated background
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // Coloring buttons (unactivated)
        for (int i = 0; i < buttonsIDs.length; i++) {
            progressButtons[i] = findViewById(buttonsIDs[i]);
            progressButtons[i].getBackground().setColorFilter(new
                    LightingColorFilter(0xFF292973, 0x00000000));
            progressButtons[i].setText(R.string.question_signs);
            progressButtons[i].invalidate();
        }

        // На каждом шаге отображение текущего счета
        List<Integer> listOfScores = HardMode.listForProgress;
        for (int i = 9; i > 9 - listOfScores.size(); i--){
            progressButtons[i] = findViewById(buttonsIDs[i]);
            progressButtons[i].getBackground().setColorFilter(new
                    LightingColorFilter(0xFFFFBC00, 0x00000000));
            String str = "\u20BF" + listOfScores.get(9 - i);
            progressButtons[i].setText(str);
            progressButtons[i].invalidate();
        }

        textViewer = findViewById(R.id.progress_counter_hard);
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


    @Override   // disabling back button during the splash screen
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).getView();
        return false;
    }
}
