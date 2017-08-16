package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Commande;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/21/17.
 */

public class CommandeArrayAdapter extends ArrayAdapter<Commande> {

    public CommandeArrayAdapter(Context context, ArrayList<Commande> commandes) {
        super(context, android.R.layout.simple_list_item_1, commandes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Commande commande = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_commande, parent, false);
        }

        AppCompatTextView tvClientName = ( AppCompatTextView ) convertView.findViewById(R.id.tvClientName);
        if(commande.getNomClient() != null){
            tvClientName.setText(commande.getNomClient().toString().toUpperCase());
        }else{
            tvClientName.setVisibility(View.GONE);
        }

        AppCompatTextView tvTelClient = ( AppCompatTextView ) convertView.findViewById(R.id.tvTelClient);
        if(commande.getTelephoneClient() != null){
            tvTelClient.setText(commande.getTelephoneClient().toString());
        }else{
            tvTelClient.setVisibility(View.GONE);
        }

        AppCompatTextView tvQteProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvQteProduit);
        if(commande.getQteTotalProduit() != null){
            tvQteProduit.setText(commande.getQteTotalProduit().toString());
        }else{
            tvQteProduit.setVisibility(View.GONE);
        }

        AppCompatTextView tvTotalAchat = ( AppCompatTextView ) convertView.findViewById(R.id.tvTotalAchat);
        if(commande.getNomClient() != null){
            tvTotalAchat.setText(commande.getPrixTotalCommande().toString()+" gdes.");
        }else{
            tvTotalAchat.setVisibility(View.GONE);
        }
        AppCompatTextView tvDateVente = ( AppCompatTextView ) convertView.findViewById(R.id.tvDateVente);
        if(commande.getCreated() != null){
            String strDate = DateFormat.formatDate(commande.getCreated());
            tvDateVente.setText(strDate);
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvDateVente.setText(textLastLogin);
        }

        return convertView;
    }
}