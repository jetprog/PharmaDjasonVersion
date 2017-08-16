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
import com.etrouve.egestion.models.Entreprise_info;
import com.etrouve.egestion.models.UniteRef_entreprise;
import com.google.gson.Gson;

/**
 * Created by ingdjason on 6/19/17.
 */

public class DialogAddUniteEntreprise extends  android.support.v4.app.DialogFragment
{

    EditText edtNomUnite;
    EditText edtDescUnite;
    AppCompatButton btnAddUnite;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String entrepriseIdLogin;
    String nomUnite;
    String descUnite;
    int echec=0;
    public DialogAddUniteEntreprise()
    {
    }

    public static DialogAddUniteEntreprise newInstance(String title) {
        DialogAddUniteEntreprise frag = new DialogAddUniteEntreprise();
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
        View rootView = inflater.inflate(R.layout.dialog_add_unite_entreprise, container, false);
        getDialog().setTitle("simple dialog");

        //entrepriseIdLogin = ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        edtNomUnite =(EditText) rootView.findViewById(R.id.edtNomUnite);
        edtDescUnite=(EditText) rootView.findViewById(R.id.edtDescUnite);
        btnAddUnite=(AppCompatButton) rootView.findViewById(R.id.btnAddUnite);

        btnAddUnite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtNomUnite.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer nom Valeur Unité entreprise", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    nomUnite=edtNomUnite.getText().toString();
                    descUnite=edtDescUnite.getText().toString();
                    saveCategorie(entrepriseIdLogin, nomUnite,descUnite);
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

    public void saveCategorie(String entrepriseId, String nomUnit, String descUnit)
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ObjectEntreprise", "");
        Entreprise_info obj = gson.fromJson(json, Entreprise_info.class);

        UniteRef_entreprise newUniteEnt = new UniteRef_entreprise();
        newUniteEnt.setNom_uniteRef(nomUnit);
        newUniteEnt.setDesc_uniteRef(descUnit);
        newUniteEnt.setEntrepriseID(obj);

        Backendless.Persistence.save( newUniteEnt, new AsyncCallback<UniteRef_entreprise>() {
            @Override
            public void handleResponse(UniteRef_entreprise response) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Unité sauvegardé: "+response.getNom_uniteRef(), Toast.LENGTH_LONG).show();
            }


            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    saveCategorie(entrepriseIdLogin, nomUnite,descUnite);
                    echec++;
                }else{
                    Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    DialogAddUniteEntreprise.this.dismiss();
                }
            }
        });
    }
}