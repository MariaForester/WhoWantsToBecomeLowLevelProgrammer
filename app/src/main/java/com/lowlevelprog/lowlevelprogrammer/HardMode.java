package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HardMode extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    DecimalFormat decimalFormat;
    String pattern;
    boolean callIsUsed, fiftyFiftyIsUSed;
    int number, question, setNumber;
    static int score;
    TextView question_ref, reward, textViewer;
    Button btn;
    ImageButton callBtn;
    CountDownTimer cdt;
    MaterialEditText answerField;
    static List<Integer> listForProgress;

    // списки для каждого сета вопросов
    List<Integer> listForRandomChoices1;
    List<Integer> listForRandomChoices2;
    List<Integer> listForRandomChoices3;
    List<Integer> listForRandomChoices4;

    Helper query = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_mode);

        myLayout = findViewById(R.id.hard_mode);

        listForProgress = new ArrayList<>();

        // анимированный фон
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // для отображения текущего счета
        pattern = "\u20BF###,###.###";
        decimalFormat = new DecimalFormat(pattern);

        // Списки вопросов
        listForRandomChoices1 = Arrays.asList(0, 1, 2, 3, 4);
        listForRandomChoices2 = Arrays.asList(0, 1, 2, 3, 4);
        listForRandomChoices3 = Arrays.asList(0, 1, 2, 3);
        listForRandomChoices4 = Arrays.asList(0, 1);
        Collections.shuffle(listForRandomChoices1);
        Collections.shuffle(listForRandomChoices2);
        Collections.shuffle(listForRandomChoices3);
        Collections.shuffle(listForRandomChoices4);

        // В начале счет равен 0, индекс первого вопроса также 0
        // Кнопки помощи неиспользованы
        number = 0;
        score = 0;
        callIsUsed = false;
        fiftyFiftyIsUSed = false;

        // Соответствие частей экрана с xml файлом
        question_ref = findViewById(R.id.hard_question);
        btn = findViewById(R.id.submit_hard_mode);
        reward = findViewById(R.id.reward_hard);

        // Установить первый вопрос
        question = listForRandomChoices1.get(0);
        setNumber = 0;
        setUpQuestionOnly(question, setNumber);

        answerField = findViewById(R.id.answer_editing_hard);

        // Отправка вопроса в Whatsapp (помощь друга)
        callBtn = findViewById(R.id.hard_call_help);
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Спросить у друга'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionForWA = query.getQuestion(question, setNumber);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Привет! :) Знаешь ли ты ответ на "
                        + "вопрос: " + questionForWA + "?");
                try {
                    startActivity(whatsappIntent);
                    callIsUsed = true;
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp не установлен!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // игровой таймер
        textViewer = findViewById(R.id.hard_mode_counter);
        cdt = new CountDownTimer(30000, 1000) {

            public void onTick(long l) {
                textViewer.setText(String.valueOf(l / 1000));
            }

            public void onFinish() {
                textViewer.setText(R.string.time_over_rus);
                startActivity(new Intent(HardMode.this, FailedGame.class));
            }
        }.start();
    }

    // Установление вопросов и счета
    private void setUpQuestionOnly(int index, int setNumber) {
        question_ref.setText(query.getHardQuestion(index, setNumber));
        reward.setText(decimalFormat.format(score));
    }

    public void btnPressed(View v) {

        String currentAnswer;
        try {
            currentAnswer = answerField.getText().toString();
        } catch (NullPointerException ex) {
            throw ex;
        }
        // Если ничего не было введено в поле и нажата кнопка "подтвердить"
        if (currentAnswer.equals("")) {
            Toast.makeText(getApplicationContext(), "Введите ответ",
                    Toast.LENGTH_SHORT).show();
        }

        // counter
        textViewer = findViewById(R.id.hard_mode_counter);

        // WA
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Спросить у друга'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionForWA = query.getQuestion(question, setNumber);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Привет! :) Знаешь ли ты ответ на "
                        + "вопрос: '" + questionForWA + "?'");
                try {
                    startActivity(whatsappIntent);
                    callIsUsed = true;
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp не установлен!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // killing the timer when we are moving up to the next activity (if a user typed smth in)
        if (!currentAnswer.equals("")) {
            cdt.cancel(); // Setting up the timer for the next questions (5 seconds are reserved
            // for the Progress screen)
            cdt = new CountDownTimer(35000, 1000) {

                public void onTick(long l) {
                    textViewer.setText(String.valueOf(l / 1000));
                }

                public void onFinish() {
                    textViewer.setText(R.string.time_over_rus);
                    startActivity(new Intent(HardMode.this, FailedGame.class));
                }
            }.start();
        }

        // Checking the answer. Dependence on the number of set.
        if (query.checkAnswerHard(question, currentAnswer, setNumber))
            score = query.calculateScoreHard(question, currentAnswer, setNumber, score);
        listForProgress.add(score);

        // moving up to the progress screen
        Intent intent = new Intent(HardMode.this, ProgressHard.class);
        startActivity(intent);

        number++;
        if (number < query.count()) {
            answerField.getText().clear();
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

            setUpQuestionOnly(question, setNumber);
        } else {
            cdt.cancel();
            startActivity(new Intent(this, ScoreHard.class));
            finish();
        }
    }

    // Leaving or not leaving the game on button back pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно хотите покинуть игру?");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(HardMode.this, MultplayerHome.class);
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
