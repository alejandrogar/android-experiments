package com.homechat.homechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homechat.homechat.Objects.User;
import com.homechat.homechat.Utils.Utils;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Bind;

public class Signup extends AppCompatActivity {

    private static final String TAG = "Signup";

    @Bind(R.id.input_name) TextInputLayout nameText;
    @Bind(R.id.input_email) TextInputLayout emailText;
    @Bind(R.id.confirm_input_password) TextInputLayout reEnterPasswordText;
    @Bind(R.id.input_password) TextInputLayout passwordText;

    @Bind(R.id.btn_signup) Button signupButton;
    @Bind(R.id.link_login) TextView loginLink;

    String name;
    String email;
    String password;
    String reEnterPassword;

    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = database.getReference("users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getEditText().getText().toString();
                email = emailText.getEditText().getText().toString();
                password = passwordText.getEditText().getText().toString();
                reEnterPassword = reEnterPasswordText.getEditText().getText().toString();
                if(validate()) {
                    progressDialog = new ProgressDialog(Signup.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Por favor espere...");
                    progressDialog.show();

                    signUp();

                }
            }
        });


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Signup activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    public void signUp(){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(Signup.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{

                    String keyUserGenerated = users.push().getKey();

                    Utils.saveSharedSetting(Signup.this, Login.USER_UID, keyUserGenerated);

                    User user = new User(name,"NO IMAGE", email, "Some description");
                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean validate(){
        boolean isValid = true;
        if(name.length() < 5 || name.isEmpty()){
            nameText.setError("5 caracteres como mínimo");
            isValid = false;
        }else{
            nameText.setError(null);
        }

        if(email.isEmpty()){
            emailText.setError("email es obligarotio");
            isValid = false;
        }else{
            emailText.setError(null);
        }

        if(password.length() < 8 || password.isEmpty()) {
            passwordText.setError("8 caracteres como mínimo");
            isValid = false;
        }else{
            passwordText.setError(null);
        }

        if(!Objects.equals(password, reEnterPassword) || reEnterPassword.isEmpty()) {
            reEnterPasswordText.setError("Las contraseñas no conciden");
            isValid = false;
        }else{
            reEnterPasswordText.setError(null);
        }

        return isValid;
    }
}
