package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.UniteRef_produit;

import java.util.ArrayList;

/**
 * Created by ingdjason on 7/6/17.
 */

public class ListeAchatProduitArrayAdapter extends ArrayAdapter<UniteRef_produit> {

    public ListeAchatProduitArrayAdapter(Context context, ArrayList<UniteRef_produit> produits_achat) {
        super(context, android.R.layout.simple_list_item_1, produits_achat);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        UniteRef_produit achat_prod_info = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_liste_achat_produit, parent, false);

        }
        AppCompatTextView tvCategorieAchatProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvCategorieAchatProduit);
        AppCompatTextView tvNomAchatProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvNomAchatProduit);
        AppCompatTextView tvQteTotalProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvQteTotalProduit);
        AppCompatTextView tvPrixTotalProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvPrixTotalProduit);
        AppCompatTextView tvUniteAchatProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvUniteAchatProduit);
        AppCompatTextView tvPrixUniteAchatProduit = ( AppCompatTextView ) convertView.findViewById(R.id.tvPrixUniteAchatProduit);

        /*String findNomProd= newProduitInfo.getProduitID().getNom_produit();
        int findQteProd=newProduitInfo.getProduitID().getQte_produit();
        String findUniteProd=newProduitInfo.getUniteEnt_ID().getNom_uniteRef();
        Double findPrixProd=newProduitInfo.getPrix_unite();*/

        achat_prod_info.getProduitID().getCategorieID().getNom_categorie();

        if(achat_prod_info.getProduitID().getCategorieID().getNom_categorie() != null){
            tvCategorieAchatProduit.setText(achat_prod_info.getProduitID().getCategorieID().getNom_categorie().toString().toUpperCase());
        }else{
            tvCategorieAchatProduit.setVisibility(View.GONE);
        }

        if(achat_prod_info.getProduitID().getNom_produit() != null){
            tvNomAchatProduit.setText(achat_prod_info.getProduitID().getNom_produit().toString().toUpperCase());
        }else{
            tvNomAchatProduit.setVisibility(View.GONE);
        }


        if(achat_prod_info.getUniteEnt_ID().getNom_uniteRef() != null){
            tvUniteAchatProduit.setText(achat_prod_info.getUniteEnt_ID().getNom_uniteRef().toString().toUpperCase());
        }else{
            tvUniteAchatProduit.setVisibility(View.GONE);
        }

        if(achat_prod_info.getPrix_unite() != null){
            tvPrixUniteAchatProduit.setText(achat_prod_info.getPrix_unite().toString().toUpperCase());
        }else{
            tvPrixUniteAchatProduit.setVisibility(View.GONE);
        }

        return convertView;
    }
}

