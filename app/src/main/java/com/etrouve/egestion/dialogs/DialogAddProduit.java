package com.etrouve.egestion.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.etrouve.egestion.ActivityPrixProduit;
import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Categorie_produit;
import com.etrouve.egestion.models.Entreprise_info;
import com.etrouve.egestion.models.Produit_info;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by ingdjason on 6/19/17.
 */

public class DialogAddProduit extends  android.support.v4.app.DialogFragment
{

    EditText edtNomProduit;
    String nomProduit;
    EditText edtExpProduit;
    String expProduit;
    EditText edtQteProduit;
    int qteProduit;
    EditText edtQteCaisse;
    EditText edtLimitProduit;
    int limitProduit;
    EditText edtPrixAchatProduit;
    Double prixAchatProduit;
    AppCompatButton btnAddProduit;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String entrepriseIdLogin;
    String jsonCategorie;
    String json;
    int echec=0;
    public DialogAddProduit()
    {
    }

    public static DialogAddProduit newInstance(String title) {
        DialogAddProduit frag = new DialogAddProduit();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.dialog_add_produit, container, false);
        getDialog().setTitle("simple dialog");
        jsonCategorie = getArguments().getString("jsonObjCategorie");

        //entrepriseIdLogin = ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        json = sharedPreferences.getString("ObjectEntreprise", "");

        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        edtNomProduit =(EditText) rootView.findViewById(R.id.edtNomProduit);
        edtExpProduit =(EditText) rootView.findViewById(R.id.edtExpProduit);
        edtQteProduit =(EditText) rootView.findViewById(R.id.edtQteProduit);
        edtQteCaisse =(EditText) rootView.findViewById(R.id.edtQteCaisse);
        edtLimitProduit =(EditText) rootView.findViewById(R.id.edtLimitProduit);
        edtPrixAchatProduit =(EditText) rootView.findViewById(R.id.edtPrixAchatProduit);

        btnAddProduit=(AppCompatButton) rootView.findViewById(R.id.btnAddProduit);

        btnAddProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateExpProd=edtExpProduit.getText().toString();
                String splitDate[]=dateExpProd.split("/");

                if(edtNomProduit.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer nom Produit", Toast.LENGTH_LONG).show();
                }
                else if(edtExpProduit.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer expiration Produit", Toast.LENGTH_LONG).show();
                }
                else if(edtQteProduit.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Quantite Produit", Toast.LENGTH_LONG).show();
                }
                else if(edtLimitProduit.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Limit alerte Produit", Toast.LENGTH_LONG).show();
                }
                else if(edtPrixAchatProduit.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Prix Achat Produit", Toast.LENGTH_LONG).show();
                }else if(splitDate.length<3 || splitDate.length>3 ){
                    Toast.makeText(getActivity(),"utiliser le format date recommandé", Toast.LENGTH_SHORT).show();
                }
                else if(splitDate[0].length()<2 || splitDate[0].length()>2){
                    Toast.makeText(getActivity(),"Format Jour: 00", Toast.LENGTH_SHORT).show();
                }
                else if(splitDate[1].length()<2 || splitDate[1].length()>2){
                    Toast.makeText(getActivity(),"Format Mois: 00", Toast.LENGTH_SHORT).show();
                }
                else if(splitDate[2].length()<4 || splitDate[2].length()>4){
                    Toast.makeText(getActivity(),"Format année: 0000", Toast.LENGTH_SHORT).show();
                }
                else{
                    
                    nomProduit=edtNomProduit.getText().toString();
                    expProduit=edtExpProduit.getText().toString();
                    limitProduit= Integer.parseInt(edtLimitProduit.getText().toString());
                    prixAchatProduit= Double.valueOf(edtPrixAchatProduit.getText().toString());
                    
                    if(edtQteCaisse.getText().toString().equals("")){
                        qteProduit= Integer.parseInt(edtQteProduit.getText().toString());
                    }else{
                        int qteCaisse= Integer.parseInt(edtQteCaisse.getText().toString());
                        int qteParCaisse=Integer.parseInt(edtQteProduit.getText().toString());
                        qteProduit= qteCaisse*qteParCaisse;
                    }
                    
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    
                    saveProduit(entrepriseIdLogin, nomProduit, expProduit, limitProduit, prixAchatProduit, qteProduit);
                    //Toast.makeText(getActivity(), "Information application...", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Ajouter :");
        getDialog().setTitle(title);
    }

    public void saveProduit(String entrepriseId, String nomProd, String expProd, int limitProd, Double prixAchatProd, int qteProd)
    {
        Gson gson = new Gson();
        Date dExp= new Date(expProd+" 00:00:00");

        Categorie_produit objCategorie = gson.fromJson(jsonCategorie, Categorie_produit.class);
        final Entreprise_info obj = gson.fromJson(json, Entreprise_info.class);

        final Produit_info newProduit = new Produit_info();
        newProduit.setNom_produit(nomProd);
        newProduit.setExpiration_produit(dExp);
        newProduit.setLimit_produit(limitProd);
        newProduit.setPrixAch_produit(prixAchatProd);
        newProduit.setQte_produit(qteProd);
        newProduit.setCategorieID(objCategorie);

        Backendless.Persistence.save( newProduit, new AsyncCallback<Produit_info>() {
            @Override
            public void handleResponse(Produit_info response) {
                Gson gson= new Gson();
                String jsonProduit= gson.toJson(newProduit);
                Intent i = new Intent(getActivity(), ActivityPrixProduit.class);
                i.putExtra("objProduit", jsonProduit);
                startActivity(i);
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    saveProduit(entrepriseIdLogin, nomProduit, expProduit, limitProduit, prixAchatProduit, qteProduit);
                    echec++;
                }else{
                    Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    DialogAddProduit.this.dismiss();
                }
            }
        });
    }
}