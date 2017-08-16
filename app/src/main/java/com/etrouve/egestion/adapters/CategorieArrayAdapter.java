package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Categorie_produit;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/21/17.
 */

public class CategorieArrayAdapter extends ArrayAdapter<Categorie_produit> {

    public CategorieArrayAdapter(Context context, ArrayList<Categorie_produit> categories) {
        super(context, android.R.layout.simple_list_item_1, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Categorie_produit categorie = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_categorie, parent, false);
        }


        AppCompatTextView tvCategorieName = ( AppCompatTextView ) convertView.findViewById(R.id.tvCategorieName);
        if(categorie.getNom_categorie() != null){
            tvCategorieName.setText(categorie.getNom_categorie().toString().toUpperCase());
        }else{
            tvCategorieName.setVisibility(View.GONE);
        }

        AppCompatTextView tvCategorieDate = ( AppCompatTextView ) convertView.findViewById(R.id.tvCategorieDate);
        if(categorie.getCreated() != null){
            String strDate = DateFormat.formatDate(categorie.getCreated());
            tvCategorieDate.setText("Ajouté le: "+strDate);
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvCategorieDate.setText(textLastLogin);
        }

        return convertView;
    }
}