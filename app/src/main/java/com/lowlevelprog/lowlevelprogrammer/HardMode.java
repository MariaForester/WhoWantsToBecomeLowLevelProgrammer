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
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HardMode extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    DecimalFormat decimalFormat;
    String pattern;
    boolean callIsUsed, fiftyFiftyIsUSed, audienceIsUsed;
    int number, question, setNumber;
    static int score;
    TextView question_ref, reward, textViewer;
    Button btn;
    ImageButton callBtn, audienceButton;
    CountDownTimer cdt;
    MaterialEditText answerField;
    static List<Integer> listForProgress;
    HomeWatcher mHomeWatcher;
    boolean soundIsOff;

    // списки для каждого сета вопросов
    List<Integer> listForRandomChoices1;
    List<Integer> listForRandomChoices2;
    List<Integer> listForRandomChoices3;
    List<Integer> listForRandomChoices4;

    Helper query = new Helper();

    // music
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

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
        listForRandomChoices1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        listForRandomChoices2 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        listForRandomChoices3 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        listForRandomChoices4 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        Collections.shuffle(listForRandomChoices1);
        Collections.shuffle(listForRandomChoices2);
        Collections.shuffle(listForRandomChoices3);
        Collections.shuffle(listForRandomChoices4);

        // music
        soundIsOff = MainActivity.soundIsOff;
        if (soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }
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

        // В начале счет равен 0, индекс первого вопроса также 0
        // Кнопки помощи неиспользованы
        number = 0;
        score = 0;
        callIsUsed = false;
        fiftyFiftyIsUSed = false;
        audienceIsUsed = false;

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

        // with a 60 percent probability the audience knows the answer
        audienceButton = findViewById(R.id.btn_audience_help_hard);
        audienceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (audienceIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Помощь аудитории'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Integer> weightedArr = new ArrayList<>();
                for (int i = 0; i < 6; i++) weightedArr.add(1);
                for (int i = 0; i < 4; i++) weightedArr.add(0);
                Random rand = new Random();
                int randomFromArr = weightedArr.get(rand.nextInt(weightedArr.size()));
                if (randomFromArr == 1) {
                    answerField.setText(query.getAnswerStringHard(question, setNumber));
                } else {
                    Toast.makeText(getApplicationContext(), "Аудитория не знает ответа на данный " +
                            "вопрос!", Toast.LENGTH_SHORT).show();
                    return;
                }
                audienceIsUsed = true;
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
                Intent intent = new Intent();
                intent.setClass(HardMode.this, FailedGame.class);
                intent.putExtra("source", "FromHardGame");
                HardMode.this.startActivity(intent);
                cdt.cancel();
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

        // with a 60 percent probability the audience knows the answer
        audienceButton = findViewById(R.id.btn_audience_help_hard);
        audienceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (audienceIsUsed) {
                    Toast.makeText(getApplicationContext(), "Вы уже использовали попытку " +
                                    "'Помощь аудитории'!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Integer> weightedArr = new ArrayList<>();
                for (int i = 0; i < 6; i++) weightedArr.add(1);
                for (int i = 0; i < 4; i++) weightedArr.add(0);
                Random rand = new Random();
                int randomFromArr = weightedArr.get(rand.nextInt(weightedArr.size()));
                if (randomFromArr == 1) {
                    answerField.setText(query.getAnswerStringHard(question, setNumber));
                } else {
                    Toast.makeText(getApplicationContext(), "Аудитория не знает ответа на данный " +
                            "вопрос!", Toast.LENGTH_SHORT).show();
                    return;
                }
                audienceIsUsed = true;
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
                    Intent intent = new Intent();
                    intent.setClass(HardMode.this, FailedGame.class);
                    intent.putExtra("source", "FromHardGame");
                    HardMode.this.startActivity(intent);
                    cdt.cancel();
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
            this.finish();
        }
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
        music.setClass(this,MusicService.class);
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
                Intent intent = new Intent(HardMode.this, MultplayerHome.class);
                startActivity(intent);
                cdt.cancel();
                HardMode.this.finish();
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
