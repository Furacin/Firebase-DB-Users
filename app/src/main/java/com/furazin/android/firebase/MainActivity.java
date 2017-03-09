package com.furazin.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "OK";

    Button buttonRegister, buttonSignIn;
    EditText editTextEmail, editTextPass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = (Button) findViewById(R.id.register_button);
        buttonSignIn = (Button) findViewById(R.id.signin_button);
        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPass = (EditText) findViewById(R.id.login_password);

        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // FirebaseAuth.getInstance().getCurrentUser

                if (user != null) { // Se ha iniciado sesion
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else { // Si no se ha iniciado sesion
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    private void registrar(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(MainActivity.this, "Registro correcto!",
                                Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void iniciarSesion(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(MainActivity.this, "Login correcto!",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, BlankActivity.class);
                        startActivity(i);
                        finish();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_button:
                 String emailInicio = editTextEmail.getText().toString();
                 String passInicio = editTextPass.getText().toString();
                 iniciarSesion(emailInicio,passInicio);
                 break;
            case R.id.register_button:
                String emailReg = editTextEmail.getText().toString();
                String passReg = editTextPass.getText().toString();
                registrar(emailReg,passReg);
                break;
        }
    }
//
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
