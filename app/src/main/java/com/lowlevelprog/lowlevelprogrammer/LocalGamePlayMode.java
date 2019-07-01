package com.lowlevelprog.lowlevelprogrammer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocalGamePlayMode extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;

    TextView question_ref;
    Helper query = new Helper();
    RadioGroup radioGroup;
    Button btn;
    RadioButton answer;
    RadioButton[] radios;
    String[] choices;
    int number;
    int score;
    TextView reward;
    DecimalFormat decimalFormat;
    String pattern;
    TextView textViewer;

    // 4 lists for each set of questions. We choose a random question from the certain set
    List<Integer> listForRandomChoices1;
    List<Integer> listForRandomChoices2;
    List<Integer> listForRandomChoices3;
    List<Integer> listForRandomChoices4;
    int question, setNumber;

    // OnCreate view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game_play_mode);

        myLayout = findViewById(R.id.local_game_play_mode);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        pattern = "\u20BF###,###.###";   // for the score (in currency)
        decimalFormat = new DecimalFormat(pattern);

        listForRandomChoices1 = Arrays.asList(0, 1, 2, 3, 4);
        listForRandomChoices2 = Arrays.asList(0, 1, 2, 3, 4);
        listForRandomChoices3 = Arrays.asList(0, 1, 2, 3);
        listForRandomChoices4 = Arrays.asList(0, 1);
        Collections.shuffle(listForRandomChoices1);
        System.out.println(listForRandomChoices1);
        Collections.shuffle(listForRandomChoices2);
        Collections.shuffle(listForRandomChoices3);
        Collections.shuffle(listForRandomChoices4);

        number = 0;
        score = 0;
        radioGroup = findViewById(R.id.radioGroup);
        question_ref = findViewById(R.id.question);
        btn = findViewById(R.id.submit_loc_mode);
        reward = findViewById(R.id.reward);
        setRadios();

        question = listForRandomChoices1.get(0);
        setNumber = 0;
        setUp(question, setNumber);

    }

    // Actions when one of the answer buttons is pressed
    public void btnPressed(View v) {
        int radioID = radioGroup.getCheckedRadioButtonId();
        answer = findViewById(radioID);


        if (query.checkAnswer(question, answer.getText().toString(), setNumber)) {
            score = query.calculateAnswer(setNumber, score);
            //startActivity(new Intent(this, Progress.class));

            number++;
            if (number < query.count()) {
                radioGroup.clearCheck();
                if (number < 3) {
                    question = listForRandomChoices1.get(number);
                    setNumber = 0;
                } else if (number < 6) {
                    question = listForRandomChoices2.get(number - 3);
                    setNumber = 1;
                } else if (number < 9) {
                    setNumber = 2;
                    question = listForRandomChoices3.get(number - 6);
                } else {
                    setNumber = 3;
                    question = listForRandomChoices4.get(number - 9);
                }

                setUp(question, setNumber);
                startActivity(new Intent(this, Progress.class));
            } else {
                startActivity(new Intent(this, WonGame.class));
                finish();
            }
        } else {
            startActivity(new Intent(this, FailedGame.class));
            finish();
        }

    }

    // Setting up the questions with corresponding answers
    private void setUp(int index, int setNumber) {
        question_ref.setText(query.getQuestion(index, setNumber));
        choices = query.getChoices(index, setNumber);
        reward.setText(decimalFormat.format(score));
        for (int i = 0; i < radios.length; i++) {
            radios[i].setText(choices[i]);
        }
    }

    // Setting ujp list of questions (its view)
    private void setRadios() {
        radios = new RadioButton[4];
        radios[0] = findViewById(R.id.option_1);
        radios[1] = findViewById(R.id.option_2);
        radios[2] = findViewById(R.id.option_3);
        radios[3] = findViewById(R.id.option_4);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно хотите покинуть игру?");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(LocalGamePlayMode.this, MainActivity.class);
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
