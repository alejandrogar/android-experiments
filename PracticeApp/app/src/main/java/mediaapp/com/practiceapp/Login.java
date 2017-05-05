package mediaapp.com.practiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Firebase ref;
    EditText email;
    EditText pass;
    Button login;

    String e;
    String p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);


        ref = new Firebase("https://chatepel.firebaseio.com");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = email.getText().toString();
                p = pass.getText().toString();
                /*

                ref.createUser(e, p, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(Login.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        Toast.makeText(Login.this, firebaseError.toString() , Toast.LENGTH_LONG).show();
                    }
                });*/


                ref.authWithPassword(e, p, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent toChat = new Intent(Login.this,FirebaseExample.class);
                        toChat.putExtra("ID",authData.getUid());
                        toChat.putExtra("PROVIDER",authData.getProvider());
                        toChat.putExtra("EMAIL",e);
                        toChat.putExtra("PASSWORD",p);
                        startActivity(toChat);
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        Toast.makeText(Login.this, firebaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login){
        }
    }
}
