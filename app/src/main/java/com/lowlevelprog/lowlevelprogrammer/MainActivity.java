package com.lowlevelprog.lowlevelprogrammer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.line.LineTextView;

public class MainActivity extends AppCompatActivity {

    private LineTextView hTextView;
    private Button button_local, button_multiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hTextView = findViewById(R.id.tv_htext); // block on above
        hTextView.setLineColor(R.color.textColor);
        hTextView.setAnimationListener(new SimpleAnimationListener(this));
        hTextView.animateText(getString(R.string.app_full_name));

        button_local = findViewById(R.id.but_loc_game); // open one of game modes
        button_multiplayer = findViewById(R.id.but_multip_game);
        button_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameMode(v.getId());
            }
        });
        button_multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameMode(v.getId());
            }
        });
    }

    public void openGameMode(int identifier) {
        Intent intent;
        if (identifier == R.id.but_loc_game)
            intent = new Intent(this, LocalGame.class);
        else
            intent = new Intent(this, MultiplayerGame.class);
        startActivity(intent);
    }

    class SimpleAnimationListener implements AnimationListener {

        private Context context;

        SimpleAnimationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onAnimationEnd(HTextView hTextView) {
            hTextView.animateText(getString(R.string.app_full_name));
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Вы точно хотите выйти?");
        alertDialogBuilder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).setNegativeButton("0", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConnected(MainActivity.this)) {
            buildDialog(MainActivity.this).show();
        }

    }

    public boolean isConnected(Context context) throws NullPointerException {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null &&
                    wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(false);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Connect Your Device To Continue");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, Splash_Intro.class);
                finish();
                startActivity(i);
            }
        });
        return builder;
    }
}

