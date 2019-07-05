package com.lowlevelprog.lowlevelprogrammer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lowlevelprog.lowlevelprogrammer.Model.Category;


public class MultplayerHome extends AppCompatActivity {

    RelativeLayout myLayout;
    AnimationDrawable animationDrawable;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multplayer_home);

        myLayout = findViewById(R.id.multiplay_home);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // Переход на фрагменты при надатии кнопок внизу экрана
        bottomNavigationView = findViewById(R.id.navigation_multiplayer_home);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment selection = null;
                        switch (menuItem.getItemId()) {
                            case R.id.action_category:
                                selection = CategoryFragment.newInstance();
                                break;
                            case R.id.action_ranking:
                                selection = RankingFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_multiplayer_home, selection);
                        transaction.commit();
                        return true;
                    }
                });
        setDefaultFragment();
    }

    private void setDefaultFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_multiplayer_home, CategoryFragment.newInstance());
        transaction.commit();
    }
}
