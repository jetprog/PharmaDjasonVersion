package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.UniteRef_entreprise;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/21/17.
 */

public class UniteEntArrayAdapter extends ArrayAdapter<UniteRef_entreprise> {

    //ActivityPrixProduit pdataProd = new ActivityPrixProduit();
    //int pUniteA= pdataProd.unitP;
    public UniteEntArrayAdapter(Context context, ArrayList<UniteRef_entreprise> categories) {
        super(context, android.R.layout.simple_list_item_1, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        UniteRef_entreprise unite = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_list_unite_ent, parent, false);

        }
            AppCompatTextView tvUniteName = ( AppCompatTextView ) convertView.findViewById(R.id.tvUniteName);
            if(unite.getNom_uniteRef() != null){
                tvUniteName.setText(unite.getNom_uniteRef().toString().toUpperCase());
            }else{
                tvUniteName.setVisibility(View.GONE);
            }

            AppCompatTextView tvUniteDate = ( AppCompatTextView ) convertView.findViewById(R.id.tvUniteDate);
            if(unite.getCreated() != null){
                String strDate = DateFormat.formatDate(unite.getCreated());
                tvUniteDate.setText("Ajouté le: "+strDate);
            }else{
                String textLastLogin = "00 / 00 / 00";
                tvUniteDate.setText(textLastLogin);
            }

        return convertView;
    }
}