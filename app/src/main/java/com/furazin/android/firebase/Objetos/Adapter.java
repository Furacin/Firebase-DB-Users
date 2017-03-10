package com.furazin.android.firebase.Objetos;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.furazin.android.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by manza on 10/03/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.InvitadosviewHolder>{

    // Referenca a la BD de firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(FireBaseReferences.INVITADOS_REFERENCE);

    List<UsuarioInvitado> invitados;

    public Adapter(List<UsuarioInvitado> invitados) {
        this.invitados = invitados;
    }

    @Override
    public InvitadosviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler, parent, false);
        InvitadosviewHolder holder = new InvitadosviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(InvitadosviewHolder holder, int position) {
        UsuarioInvitado invitado = invitados.get(position);
        holder.textViewEmailInvitado.setText(invitado.getEmail());

        // Obtener la fila del invitado a eliminar tras seleccionar el boton de eliminar
        final Query userQuery = myRef.orderByChild("email").equalTo(holder.textViewEmailInvitado.getText().toString());

        final String user_invitado = holder.textViewEmailInvitado.getText().toString();

        holder.buttonDeleteInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Dialog de confirmacion para eliminar usuario
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Estás seguro?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        // Eliminar el usuario con el email del textview
                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                        deleteUser(holder.textViewEmailInvitado.getText().toString());
                                for (DataSnapshot emailSnapshot: dataSnapshot.getChildren()) {
                                    emailSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
//                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return invitados.size();
    }

    public static class InvitadosviewHolder extends RecyclerView.ViewHolder {

        TextView textViewEmailInvitado;
        Button buttonDeleteInvitado;

        public InvitadosviewHolder(View itemView) {
            super(itemView);
            textViewEmailInvitado = (TextView) itemView.findViewById(R.id.invitadotextview_from_bd);
            buttonDeleteInvitado = (Button) itemView.findViewById(R.id.button_delete_invitado);
        }
    }

    public void deleteUser(String email_user) {

    }
}
