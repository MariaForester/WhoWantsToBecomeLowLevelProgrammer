package com.lowlevelprog.lowlevelprogrammer;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    static int score;
    TextView reward;
    DecimalFormat decimalFormat;
    String pattern;
    TextView textViewer;
    CountDownTimer cdt;
    HomeWatcher mHomeWatcher;

    // 4 lists for each set of questions. We choose a random question from the certain set
    List<Integer> listForRandomChoices1;
    List<Integer> listForRandomChoices2;
    List<Integer> listForRandomChoices3;
    List<Integer> listForRandomChoices4;
    int question, setNumber;

    ImageButton callBtn, fiftyFiftyBtn, audienceButton;
    int selectionIndex1, selectionIndex2;

    boolean callIsUsed, fiftyFiftyIsUSed, audienceIsUsed;

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

        // Getting questions from each set in random order
        listForRandomChoices1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        listForRandomChoices2 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        listForRandomChoices3 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        listForRandomChoices4 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        Collections.shuffle(listForRandomChoices1);
        Collections.shuffle(listForRandomChoices2);
        Collections.shuffle(listForRandomChoices3);
        Collections.shuffle(listForRandomChoices4);

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

        number = 0;
        score = 0;
        callIsUsed = false;
        fiftyFiftyIsUSed = false;
        audienceIsUsed = false;
        radioGroup = findViewById(R.id.radioGroup);
        question_ref = findViewById(R.id.question);
        btn = findViewById(R.id.submit_loc_mode);
        reward = findViewById(R.id.reward);
        setRadios();

        // Setting up the first question
        question = listForRandomChoices1.get(0);
        setNumber = 0;
        setUp(question, setNumber);

        // Отправка вопроса в Whatsapp (помощь друга)
        callBtn = findViewById(R.id.btn_call_help);
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Спросить у друга'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionToShare = query.getQuestion(question, setNumber);
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Привет!\nЗнаешь ли ты ответ на "
                        + "вопрос: '" + questionToShare + "?'";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Отправить через"));
                callIsUsed = true;
            }
        });
        // Оставить 2 ответа из 4
        fiftyFiftyBtn = findViewById(R.id.btn_fifty_help);
        fiftyFiftyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fiftyFiftyIsUSed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Убрать 2 неправильных ответа'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int answerIndex = query.getAnswerIndex(question, setNumber);
                selectionIndex1 = -1;
                selectionIndex2 = -1;
                while (selectionIndex1 == -1 || selectionIndex1 == answerIndex) {
                    double randomDouble = Math.random();
                    randomDouble *= 3;
                    selectionIndex1 = (int) randomDouble;
                }
                while (selectionIndex2 == -1 || selectionIndex2 == selectionIndex1 || selectionIndex2 == answerIndex) {
                    double randomDouble = Math.random();
                    randomDouble *= 3;
                    selectionIndex2 = (int) randomDouble;
                }
                radios[selectionIndex1].setVisibility(View.INVISIBLE);
                radios[selectionIndex2].setVisibility(View.INVISIBLE);
                fiftyFiftyIsUSed = true;
            }
        });

        // Помощь аудитории - верный ответ с вероятностью 40 процентов. Вероятность
        // выбора любого другого из 4-х - 20 процентов.
        audienceButton = findViewById(R.id.btn_audience_help);
        audienceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (audienceIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Помощь аудитории'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int answerIndex = query.getAnswerIndex(question, setNumber);
                ArrayList<Integer> weightedArr = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (i == answerIndex) {
                        for (int j = 0; j < 4; j++) weightedArr.add(i);
                    } else {
                        for (int j = 0; j < 2; j++) weightedArr.add(i);
                    }
                }

                Random rand = new Random();
                int randomBtnIndex = weightedArr.get(rand.nextInt(weightedArr.size()));

                for (int i = 0; i < 4; i++) {
                    if (i != randomBtnIndex) radios[i].setVisibility(View.INVISIBLE);
                }
                audienceIsUsed = true;
            }
        });

        // Game timer. There are only 30 seconds to submit the answer
        textViewer = findViewById(R.id.local_game_play_mode_counter);
        cdt = new CountDownTimer(30000, 1000) {

            public void onTick(long l) {
                textViewer.setText(String.valueOf(l / 1000));
            }

            public void onFinish() {
                textViewer.setText(R.string.time_over_rus);
                Intent intent = new Intent();
                intent.setClass(LocalGamePlayMode.this, FailedGame.class);
                intent.putExtra("source", "FromLocalGame");
                LocalGamePlayMode.this.startActivity(intent);
                cdt.cancel();
            }
        }.start();

    }

    // Actions when one of the answer buttons is pressed
    public void btnPressed(View v) {
        // Вернуть кнопки на место после выбора ответа
        for (int i = 0; i < 4; i++) {
            radios[i].setVisibility(View.VISIBLE);
        }

        // Toast when nothing is clicked encouraging a user to click on some button
        int radioID = radioGroup.getCheckedRadioButtonId();
        if (radioID == -1) {    // ни один ответ не выбран
            Toast.makeText(getApplicationContext(), "Выберите ответ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        answer = findViewById(radioID);
        textViewer = findViewById(R.id.local_game_play_mode_counter);

        // WA
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Спросить у друга'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionToShare = query.getQuestion(question, setNumber);
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Привет!\nЗнаешь ли ты ответ на "
                        + "вопрос: '" + questionToShare + "?'";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Отправить через"));
                callIsUsed = true;
            }
        });

        // Оставить 2 ответа из 4
        fiftyFiftyBtn = findViewById(R.id.btn_fifty_help);
        fiftyFiftyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fiftyFiftyIsUSed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Убрать 2 неправильных ответа'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int answerIndex = query.getAnswerIndex(question, setNumber);
                selectionIndex1 = -1;
                selectionIndex2 = -1;
                while (selectionIndex1 == -1 || selectionIndex1 == answerIndex) {
                    double randomDouble = Math.random();
                    randomDouble *= 3;
                    selectionIndex1 = (int) randomDouble;
                }
                while (selectionIndex2 == -1 || selectionIndex2 == selectionIndex1 || selectionIndex2 == answerIndex) {
                    double randomDouble = Math.random();
                    randomDouble *= 3;
                    selectionIndex2 = (int) randomDouble;
                }
                radios[selectionIndex1].setVisibility(View.INVISIBLE);
                radios[selectionIndex2].setVisibility(View.INVISIBLE);
                fiftyFiftyIsUSed = true;
            }
        });

        // Помощь аудитории - верный ответ с вероятностью 40 процентов. Вероятность
        // выбора любого другого из 4-х - 20 процентов.
        audienceButton = findViewById(R.id.btn_audience_help);
        audienceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (audienceIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Помощь аудитории'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int answerIndex = query.getAnswerIndex(question, setNumber);
                ArrayList<Integer> weightedArr = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (i == answerIndex) {
                        for (int j = 0; j < 4; j++) weightedArr.add(i);
                    } else {
                        for (int j = 0; j < 2; j++) weightedArr.add(i);
                    }
                }

                Random rand = new Random();
                int randomBtnIndex = weightedArr.get(rand.nextInt(weightedArr.size()));

                for (int i = 0; i < 4; i++) {
                    if (i != randomBtnIndex) radios[i].setVisibility(View.INVISIBLE);
                }
                audienceIsUsed = true;
            }
        });

        // killing the timer when we are moving up to the next activity
        cdt.cancel();
        // Setting up the timer for the next questions (5 seconds are reserved for the Progress
        // screen)
        cdt = new CountDownTimer(35000, 1000) {

            public void onTick(long l) {
                textViewer.setText(String.valueOf(l / 1000));
            }

            public void onFinish() {
                textViewer.setText(R.string.time_over_rus);
                Intent intent = new Intent();
                intent.setClass(LocalGamePlayMode.this, FailedGame.class);
                intent.putExtra("source", "FromLocalGame");
                LocalGamePlayMode.this.startActivity(intent);
                cdt.cancel();
            }
        }.start();

        // Checking the answer. Dependence on the number of set.
        if (query.checkAnswer(question, answer.getText().toString(), setNumber)) {
            score = query.calculateScore(setNumber, score);
            startActivity(new Intent(this, Progress.class));

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
            } else {
                cdt.cancel();
                Intent intent = new Intent();
                intent.setClass(LocalGamePlayMode.this, WonGame.class);
                intent.putExtra("source", "FromLocalGame");
                LocalGamePlayMode.this.startActivity(intent);
                // All 10 answers were correct
                this.finish();
            }
        } else {
            cdt.cancel();
            Intent intent = new Intent();
            intent.setClass(LocalGamePlayMode.this, FailedGame.class);
            intent.putExtra("source", "FromLocalGame");
            LocalGamePlayMode.this.startActivity(intent);
            // At least one answer was incorrect
            this.finish();
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

    // Leaving or not leaving the game on button back pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно хотите покинуть игру?");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(LocalGamePlayMode.this, MainActivity.class);
                startActivity(intent);
                cdt.cancel();
                LocalGamePlayMode.this.finish();
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
