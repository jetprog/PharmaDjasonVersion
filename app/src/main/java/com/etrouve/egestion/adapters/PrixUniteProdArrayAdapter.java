package com.etrouve.egestion.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Produit_info;
import com.etrouve.egestion.models.UniteRef_entreprise;
import com.etrouve.egestion.models.UniteRef_produit;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/26/17.
 */

public class PrixUniteProdArrayAdapter extends ArrayAdapter<UniteRef_entreprise> {

    Double prixProd;
    Double prixPromo;
    SharedPreferences sharedPreferences ;
    String pJsonProd;
    /*ActivityPrixProduit pdataProduit = new ActivityPrixProduit();
    final String pJsonProd= pdataProduit.jsonProduit;*/
    public PrixUniteProdArrayAdapter(Context context, ArrayList<UniteRef_entreprise> categories) {
        super(context, android.R.layout.simple_list_item_1, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        ViewWrapper ViewWrapper = null;
        // find or inflate the template
        if (convertView == null) {
            ViewWrapper = new ViewWrapper();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_add_prix_unite_prod, parent, false);

            ViewWrapper.tvUniteName = ( AppCompatTextView ) convertView.findViewById(R.id.tvUniteName);
            ViewWrapper.btnAddPrix = ( AppCompatButton ) convertView.findViewById(R.id.btnAddPrix);
            ViewWrapper.edtPrixUniteProduit = ( EditText ) convertView.findViewById(R.id.edtPrixUniteProduit);
            ViewWrapper.edtPromoUniteProduit = ( EditText ) convertView.findViewById(R.id.edtPromoUniteProduit);

            convertView.setTag(ViewWrapper);

        }else {
            ViewWrapper = (ViewWrapper) convertView.getTag();
        }
        final UniteRef_entreprise unite = getItem(position);



        if(unite.getNom_uniteRef() != null){
            ViewWrapper.tvUniteName.setText(unite.getNom_uniteRef().toString().toUpperCase());
        }else{
            ViewWrapper.tvUniteName.setVisibility(View.GONE);
        }

        //

        ViewWrapper.btnAddPrix.setTag(unite);
        ViewWrapper.edtPrixUniteProduit.setTag(unite);
        ViewWrapper.edtPromoUniteProduit.setTag(unite);

        sharedPreferences = getContext().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        pJsonProd = sharedPreferences.getString("objProduit", null);

        Gson gson = new Gson();
        final Produit_info objProduit = gson.fromJson(pJsonProd, Produit_info.class);

        final PrixUniteProdArrayAdapter.ViewWrapper finalViewWrapper = ViewWrapper;
        ViewWrapper.btnAddPrix.setOnClickListener(new AppCompatButton.OnClickListener() {
            @Override
            public void onClick(View view) {

               UniteRef_entreprise unit = (UniteRef_entreprise) view.getTag();
                finalViewWrapper.edtPromoUniteProduit.getText().toString();
                finalViewWrapper.edtPromoUniteProduit.getText().toString();

                //Toast.makeText(getContext(), unit.getNom_uniteRef()+" --- "+finalViewWrapper.edtPromoUniteProduit.getText().toString(),Toast.LENGTH_SHORT).show();

                objProduit.getNom_produit();

                if(finalViewWrapper.edtPrixUniteProduit.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Entrer Prix Produit...",Toast.LENGTH_SHORT).show();
                }else{
                    if(finalViewWrapper.edtPromoUniteProduit.getText().toString().equals("")){
                        prixProd= Double.valueOf(finalViewWrapper.edtPrixUniteProduit.getText().toString());
                        Toast.makeText(getContext(),objProduit.getNom_produit()+" - \n"+unite.getNom_uniteRef()+" - \nprix: "+prixProd,Toast.LENGTH_SHORT).show();
                        funcAddPrix(objProduit, unit);

                    }else{
                        prixProd= Double.valueOf(finalViewWrapper.edtPrixUniteProduit.getText().toString());
                        prixPromo= Double.valueOf(finalViewWrapper.edtPromoUniteProduit.getText().toString());
                        Toast.makeText(getContext(),objProduit.getNom_produit()+" - \n"+unite.getNom_uniteRef()+" - \nprix: "+prixProd+" \n promo: "+prixPromo,Toast.LENGTH_SHORT).show();
                        funcAddPrix(objProduit, unit);
                    }
                    //Toast.makeText(getContext(),objProduit.getObjectId()+" - "+unite.getObjectId(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    public void funcAddPrix(Produit_info objProduit, UniteRef_entreprise unit) {

        Toast.makeText(getContext(),objProduit.getNom_produit()+" -- "+unit.getNom_uniteRef(),Toast.LENGTH_SHORT).show();
        final UniteRef_produit newUnitProd = new UniteRef_produit();
        newUnitProd.setPrix_unite(prixProd);
        newUnitProd.setPromo_unite(prixProd);

        newUnitProd.setUniteEnt_ID(unit);
        newUnitProd.setProduitID(objProduit);

        Backendless.Persistence.save( newUnitProd, new AsyncCallback<UniteRef_produit>() {
            @Override
            public void handleResponse(UniteRef_produit response) {
                Toast.makeText(getContext(),"Enregistrer avec succès...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getContext(),fault.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ViewWrapper {
        AppCompatTextView tvUniteName;
        AppCompatButton btnAddPrix;
        EditText edtPrixUniteProduit;
        EditText edtPromoUniteProduit;
    }
}

