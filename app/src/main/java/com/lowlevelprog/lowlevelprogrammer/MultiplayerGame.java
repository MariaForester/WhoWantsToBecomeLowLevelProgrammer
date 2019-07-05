package com.lowlevelprog.lowlevelprogrammer;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowlevelprog.lowlevelprogrammer.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MultiplayerGame extends AppCompatActivity {

    // Signing up
    MaterialEditText editNewUser, editNewPassword, editNewEmail;
    // Signing in
    MaterialEditText editUser, editPassword;

    Button btnSignUp, btnSignIn;

    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulpiplayer_game);

        myLayout = findViewById(R.id.multiplayer);

        animationDrawable = (AnimationDrawable) myLayout.getBackground(); // animated background
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        // Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        editUser = findViewById(R.id.user_name_editing);
        editPassword = findViewById(R.id.password_editing);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn(editUser.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pass) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()){
                    if (!user.isEmpty()){
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pass))
                            Toast.makeText(MultiplayerGame.this, "Вход успешен!",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MultiplayerGame.this, "Неверный пароль!",
                                    Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MultiplayerGame.this, "Пожалуйста" +
                                "введите данные пользователя", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MultiplayerGame.this, "Пользователь не существует!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MultiplayerGame.this,
                R.style.AlertDialogStyle);
        ad.setTitle("Sign up");
        ad.setMessage("Пожалуйста, введите данные");

        LayoutInflater inf = this.getLayoutInflater();
        View signUpLayout = inf.inflate(R.layout.signing_up, null);

        editNewUser = signUpLayout.findViewById(R.id.new_user_name_editing);
        editNewEmail = signUpLayout.findViewById(R.id.new_email_editing);
        editNewPassword = signUpLayout.findViewById(R.id.new_password_editing);

        ad.setView(signUpLayout);
        ad.setIcon(R.drawable.ic_account_circle_black_24dp);

        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        ad.setPositiveButton("Далее", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(editNewUser.getText().toString(),
                        editNewPassword.getText().toString(), editNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MultiplayerGame.this,
                                    "Пользователь уже существует!", Toast.LENGTH_SHORT).show();
                        else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MultiplayerGame.this,
                                    "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });
        ad.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
