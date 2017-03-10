package com.furazin.android.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.furazin.android.firebase.Objetos.FireBaseReferences;
import com.furazin.android.firebase.Objetos.UsuarioInvitado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by manza on 09/03/2017.
 */

public class BlankActivity extends AppCompatActivity{

    Button buttonRegisterInvitado;
    EditText editText_invitado, editText_passwordInv;

    // BD Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Referenca a la BD de firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(FireBaseReferences.INVITADOS_REFERENCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);

        SharedPreferences sharedPref;
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String user_admin = sharedPref.getString((getString(R.string.email_key)), ""); // email del usuario administrador del invitado

        editText_invitado = (EditText) findViewById(R.id.editTextuserinvitado);
        editText_passwordInv = (EditText) findViewById(R.id.editTextpasswordinvitado);

        buttonRegisterInvitado = (Button) findViewById(R.id.registar_invitado_button);
        buttonRegisterInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_invitado = editText_invitado.getText().toString();
                String pass_invitado = editText_passwordInv.getText().toString();
                registrar(email_invitado,pass_invitado,user_admin);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // FirebaseAuth.getInstance().getCurrentUser

                if (user != null) { // Se ha iniciado sesion
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else { // Si no se ha iniciado sesion
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void registrar(final String email_invitado, final String pass_invitado, final String user_admin) {
        mAuth.createUserWithEmailAndPassword(email_invitado, pass_invitado)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(BlankActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(BlankActivity.this, "Registro correcto!",
                                    Toast.LENGTH_SHORT).show();
                            // Creamos en la base de datos un usuario de tipo invitado, que tiene asociado un usuario de tipo administrador
                            UsuarioInvitado user = new UsuarioInvitado(email_invitado, pass_invitado, user_admin);
                            myRef.push().setValue(user);
                        }
                    }
                });
    }

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
