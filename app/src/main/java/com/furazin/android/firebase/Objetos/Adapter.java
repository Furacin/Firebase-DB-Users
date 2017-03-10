package com.furazin.android.firebase.Objetos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furazin.android.firebase.R;

import java.util.List;

/**
 * Created by manza on 10/03/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.InvitadosviewHolder>{

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
    }

    @Override
    public int getItemCount() {
        return invitados.size();
    }

    public static class InvitadosviewHolder extends RecyclerView.ViewHolder {

        TextView textViewEmailInvitado;

        public InvitadosviewHolder(View itemView) {
            super(itemView);
            textViewEmailInvitado = (TextView) itemView.findViewById(R.id.invitadotextview_from_bd);

        }
    }
}
