package com.example.akshaygrover.instaclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    Button signUpButton;
    TextView changeSignUp;
    EditText userNameEditText ;
    EditText passwordEditText;
    RelativeLayout background;
    ImageView logo;
    Boolean signUpModeActive =true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
        .applicationId("S5BX3YzYhgT03LOEKGGE8uE7Jg3QEozgUjCwEhdp")
        .clientKey("7J6AeH8AVnWyu5yuPE0MNDSAilhVqJ04auwAGj3F")
        .server("https://parseapi.back4app.com/")
        .build());

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicWriteAccess(true);
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);

        signUpButton =(Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

         userNameEditText = (EditText)findViewById(R.id.userNameEditText);
         passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        passwordEditText.setOnKeyListener(this);

        background = (RelativeLayout)findViewById(R.id.background);
        logo = (ImageView)findViewById(R.id.imageView);

        background.setOnClickListener(this);
        logo.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            showUserList();
        }




        changeSignUp =(TextView)findViewById(R.id.changeSignUpMode);
        changeSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.changeSignUpMode){
                    Log.i("Appinfo:","changeSignUpMode");
                    if (signUpModeActive){
                        signUpModeActive = false;
                        signUpButton.setText("Login");
                        changeSignUp.setText("or, SignUp");
                    }
                    else {
                        signUpModeActive = true;
                        signUpButton.setText("SignUp");
                        changeSignUp.setText("or, Login");

                    }
                }
                else if (v.getId() == R.id.background || v.getId() == R.id.imageView){

                    InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {


        if(userNameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password is required", Toast.LENGTH_SHORT).show();
        }
        else {

            if(signUpModeActive) {


                ParseUser user = new ParseUser();
                user.setUsername(userNameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.i("signUp: ", "sucessful");

                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {

                ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(user != null){

                            Log.i("Login: ","success");

                            showUserList();

                        }
                        else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN){

            onClick(v);
        }

        return false;

    }
    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }
}
