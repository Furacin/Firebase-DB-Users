package com.furazin.android.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.furazin.android.firebase.Objetos.FireBaseReferences;
import com.furazin.android.firebase.Objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "OK";

    Button buttonRegister, buttonSignIn, buttonRegisterInvitado;
    EditText editTextEmail, editTextPass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Referenca a la BD de firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(FireBaseReferences.USUARIOS_REFERENCE);

    // Variable para recordar las credenciales del usuario
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = (Button) findViewById(R.id.register_button);
        buttonSignIn = (Button) findViewById(R.id.signin_button);
        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPass = (EditText) findViewById(R.id.login_password);

        // Instanciamos una referencia al Contexto
        Context context = this.getApplicationContext();
        //Instanciamos el objeto SharedPrefere  nces y creamos un fichero Privado bajo el
        //nombre definido con la clave preference_file_key en el fichero string.xml
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

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

    private void registrar(final String email, final String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Registro correcto!",
                                    Toast.LENGTH_SHORT).show();
                            Usuario user = new Usuario(email, "Piticli Bonico", pass);
                            myRef.push().setValue(user);
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

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Login correcto!",
                                    Toast.LENGTH_SHORT).show();
                            saveProfile();
                            Intent i = new Intent(MainActivity.this, BlankActivity.class);
                            startActivity(i);
                            finish();
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

    ////////////////////////////////////////////////////////////////////////////

    private void saveProfile() {
        //Capturamos en una variable de tipo String
        String input_email = editTextEmail.getText().toString();
        String input_password = editTextPass.getText().toString();

        //Instanciamos un objeto del SharedPreferences.Editor
        //el cual nos permite almacenar con su metodo putString
        //los 4 valores del perfil profesional asociandolos a una
        //clave la cual definimos como un string en el fichero strings.xml
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.password_key), input_password);
        editor.putString(getString(R.string.email_key), input_email);

        //NOTA: En el caso de que necesitemos gauirdar un valor numerico podeis usar
        //el metodo putInt en vez del putString.

        //Con el mtodo commit logramos guardar los datos en el fichero
        //de preferncias compartidas de nombre cuyo nombre se defini en
        // el String preference_file_key
        editor.commit();

        //Notificamos la usuario de que se han guardado los datos del perfil correctamente.
        //Toast.makeText(getApplicationContext(),getString(R.string.msg_save), Toast.LENGTH_SHORT).show();

    }
}
