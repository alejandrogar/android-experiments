package com.homechat.homechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.Utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String USER_UID = "NO_USER_LOGED";

    @Bind(R.id.input_email) TextInputLayout emailText;
    @Bind(R.id.input_password) TextInputLayout passwordText;
    @Bind(R.id.btn_login) Button loginButton;
    @Bind(R.id.link_signup) TextView signupLink;

    String email;
    String password;

    boolean isUserFirstTime;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;


    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(Login.this, PREF_USER_FIRST_TIME, "true"));

        database = FirebaseDatabase.getInstance();

        //Intent introIntent = new Intent(Login.this, Intro.class);
        //introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime) {
            Toast.makeText(this, "First Time", Toast.LENGTH_SHORT).show();
            /***
             * Remover las siguiente dos líneas al agregar el intro
             * */
            Utils.saveSharedSetting(Login.this, Login.PREF_USER_FIRST_TIME, "false");

            //startActivity(introIntent);
        }

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = emailText.getEditText().getText().toString();
                password = passwordText.getEditText().getText().toString();

                if(validate()) {
                    progressDialog = new ProgressDialog(Login.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Autenticando...");
                    progressDialog.show();

                    login();
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login(){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                        }

                    }
                });
    }

    public boolean validate(){
        boolean isValid = true;

        if(email.isEmpty()){
            emailText.setError("Correo es requerido");
            isValid = false;
        }else{
            emailText.setError(null);
        }

        if(password.isEmpty()){
            passwordText.setError("Contraseña es requerida");
            isValid = false;
        }else{
            passwordText.setError(null);
        }

        return isValid;
    }
}
