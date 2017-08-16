package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Gestion_stock;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/21/17.
 */

public class StockArrayAdapter extends ArrayAdapter<Gestion_stock> {

    public StockArrayAdapter(Context context, ArrayList<Gestion_stock> gestion_stocks) {
        super(context, android.R.layout.simple_list_item_1, gestion_stocks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Gestion_stock gestion_stock = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_gestionstock, parent, false);
        }


        AppCompatTextView tvIdStock = ( AppCompatTextView ) convertView.findViewById(R.id.tvIdStock);
        if(gestion_stock.getId_stock() != null){
            tvIdStock.setText("Identifiant : "+gestion_stock.getId_stock().toString());
        }else{
            tvIdStock.setVisibility(View.GONE);
        }

        AppCompatTextView tvMontantStock = ( AppCompatTextView ) convertView.findViewById(R.id.tvMontantStock);
        if(gestion_stock.getMontantTotal_stock() != null){
            tvMontantStock.setText("Montant Total : "+gestion_stock.getMontantTotal_stock().toString());
        }else{
            tvMontantStock.setVisibility(View.GONE);
        }

        AppCompatTextView tvDateStock = ( AppCompatTextView ) convertView.findViewById(R.id.tvDateStock);
        if(gestion_stock.getDate_stock() != null){
            String strDate = DateFormat.formatDate(gestion_stock.getDate_stock());
            tvDateStock.setText("Reçu le : "+strDate);
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvDateStock.setText(textLastLogin);
        }

        AppCompatTextView tvDateAddStock = ( AppCompatTextView ) convertView.findViewById(R.id.tvDateAddStock);
        if(gestion_stock.getCreated() != null){
            String strDate = DateFormat.formatDate(gestion_stock.getCreated());
            tvDateAddStock.setText("Ajouté le : "+strDate);
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvDateAddStock.setText(textLastLogin);
        }

        AppCompatTextView tvDescStock = ( AppCompatTextView ) convertView.findViewById(R.id.tvDescStock);
        if(gestion_stock.getCreated() != null){
            tvDescStock.setText(gestion_stock.getDescription_stock().toString());
        }

        return convertView;
    }
}