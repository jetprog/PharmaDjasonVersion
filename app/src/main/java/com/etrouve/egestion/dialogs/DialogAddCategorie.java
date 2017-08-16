package com.etrouve.egestion.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Categorie_produit;
import com.etrouve.egestion.models.Entreprise_info;
import com.google.gson.Gson;

/**
 * Created by ingdjason on 6/19/17.
 */

public class DialogAddCategorie extends android.support.v4.app.DialogFragment
{

    EditText edtNomCatego;
    EditText edtDescCatego;
    AppCompatButton btnAddCatego;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String entrepriseIdLogin;
    String nomCategorie;
    String descCategorie;
    int echec=0;
    public DialogAddCategorie()
    {
    }

    public static DialogAddCategorie newInstance(String title) {
        DialogAddCategorie frag = new DialogAddCategorie();
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
        View rootView = inflater.inflate(R.layout.dialog_add_categorie, container, false);
        getDialog().setTitle("simple dialog");

        //entrepriseIdLogin = ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        edtNomCatego =(EditText) rootView.findViewById(R.id.edtNomCatego);
        edtDescCatego=(EditText) rootView.findViewById(R.id.edtDescCatego);
        btnAddCatego=(AppCompatButton) rootView.findViewById(R.id.btnAddCatego);

        btnAddCatego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtNomCatego.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer nom Categorie Produit", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    nomCategorie=edtNomCatego.getText().toString();
                    descCategorie=edtDescCatego.getText().toString();
                    saveCategorie(entrepriseIdLogin, nomCategorie,descCategorie);
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
        String title = getArguments().getString("title", "title");
        getDialog().setTitle(title);
    }

    public void saveCategorie(String entrepriseId, String nomCatego, String descCatego)
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ObjectEntreprise", "");
        Entreprise_info obj = gson.fromJson(json, Entreprise_info.class);
        Categorie_produit newCategorie = new Categorie_produit();
        newCategorie.setNom_categorie(nomCatego);
        newCategorie.setDesc_categorie(descCatego);
        newCategorie.setEntrepriseID(obj);

        Backendless.Persistence.save( newCategorie, new AsyncCallback<Categorie_produit>() {
            @Override
            public void handleResponse(Categorie_produit response) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Categorie sauvegardé: "+response.getNom_categorie(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    saveCategorie(entrepriseIdLogin, nomCategorie,descCategorie);
                    echec++;
                }else{
                    Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    DialogAddCategorie.this.dismiss();
                }
            }
        });
    }
}