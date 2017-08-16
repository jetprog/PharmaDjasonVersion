package com.etrouve.egestion.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Produit_info;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ingdjason on 6/21/17.
 */

public class ProduitArrayAdapter extends ArrayAdapter<Produit_info> {

    public ProduitArrayAdapter(Context context, ArrayList<Produit_info> produits) {
        super(context, android.R.layout.simple_list_item_1, produits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Produit_info produit = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_produit, parent, false);
        }


        AppCompatTextView tvNomProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvNomProduit);
        if(produit.getNom_produit() != null){
            tvNomProduit.setText(produit.getNom_produit().toString().toUpperCase());
        }else{
            tvNomProduit.setVisibility(View.GONE);
        }

        AppCompatTextView tvQteProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvQteProduit);
        if(produit.getQte_produit() > produit.getLimit_produit()){
            tvQteProduit.setText("Qte : "+produit.getQte_produit());
        }else if(produit.getQte_produit() == produit.getLimit_produit()){
            tvQteProduit.setText("Qte : "+produit.getQte_produit());
            tvQteProduit.setTextColor(Color.YELLOW);
        }else if(produit.getQte_produit() < produit.getLimit_produit()){
            tvQteProduit.setText("Qte : "+produit.getQte_produit());
            tvQteProduit.setTextColor(Color.RED);
        }else{
            tvQteProduit.setVisibility(View.GONE);
        }

        AppCompatTextView tvLimitProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvLimitProduit);
        if(produit.getLimit_produit() !=0){
            tvLimitProduit.setText("Limit : "+produit.getLimit_produit());
        }

        AppCompatTextView tvExpProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvExpProduit);
        //String strDateofDay = DateFormat.formatDate(new Date());
        Date dateOfDay = new Date();
        String strDate = DateFormat.formatDate(produit.getExpiration_produit());
        if(dateOfDay.getYear()>produit.getExpiration_produit().getYear()){
            tvExpProduit.setText(strDate+" (expiré)");
            tvExpProduit.setTextColor(Color.RED);
        }else if(dateOfDay.getYear()<produit.getExpiration_produit().getYear()){
            tvExpProduit.setText("Exp : "+strDate);
        }else if(dateOfDay.getYear()==produit.getExpiration_produit().getYear()){
            if(dateOfDay.getMonth()>produit.getExpiration_produit().getMonth()){
                tvExpProduit.setText(strDate+" (expiré)");
                tvExpProduit.setTextColor(Color.RED);
            }else if(dateOfDay.getMonth()<produit.getExpiration_produit().getMonth()){
                tvExpProduit.setText("Exp : "+strDate);
            }else if(dateOfDay.getMonth()==produit.getExpiration_produit().getMonth()){
                tvExpProduit.setText(strDate+" (expiré)");
                tvExpProduit.setTextColor(Color.RED);
            }else if((dateOfDay.getMonth()-1)==produit.getExpiration_produit().getMonth()){
                tvExpProduit.setText(strDate+" (expiré)");
                tvExpProduit.setTextColor(Color.YELLOW);
            }
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvExpProduit.setText(textLastLogin);
            tvExpProduit.setTextColor(Color.RED);
        }


        return convertView;
    }
}