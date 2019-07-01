package com.lowlevelprog.lowlevelprogrammer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Progress extends AppCompatActivity {

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    TextView textViewer;

    // определяем строковый массив
    String[] scoreValues = new String[]{
            "\u20BF 80000", "\u20BF 40000", "\u20BF 25000", "\u20BF 15000", "\u20BF 10000",
            "\u20BF 7000", "\u20BF 5000", "\u20BF 2000", "\u20BF 500", "\u20BF 100"
    };
    // Bars
    int[] barImage = new int[]{
            R.drawable.dark_bar
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        myLayout = findViewById(R.id.progress_lay);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // получаем экземпляр элемента ListView
        ListView listView = findViewById(R.id.listView);

        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, scoreValues);

        listView.setAdapter(adapter);
        new CountDownTimer(60000, 1000) {

            public void onTick(long l) {
                textViewer.setText(" " + l/ 1000);
            }

            public void onFinish() {
                textViewer.setText("-");
            }
        }.start();
        startActivity(new Intent(this, Progress.class));

    }


}
