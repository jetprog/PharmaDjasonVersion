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
import com.etrouve.egestion.models.Gestion_stock;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by ingdjason on 6/19/17.
 */

public class DialogAddStock extends  android.support.v4.app.DialogFragment
{

    EditText edtIdentifiantStock;
    String identifiantStock;
    EditText edtMontantTotalStock;
    Double montantStock;
    EditText edtDateStock;
    String dateStock;
    EditText edtDescStock;
    String descStock;
    AppCompatButton btnEnregistrerStock;
    
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String entrepriseIdLogin;
    int echec=0;
    public DialogAddStock()
    {
    }

    public static DialogAddStock newInstance(String title) {
        DialogAddStock frag = new DialogAddStock();
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
        View rootView = inflater.inflate(R.layout.dialog_add_stock, container, false);
        getDialog().setTitle("simple dialog");

        //entrepriseIdLogin = ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        
        edtIdentifiantStock =(EditText) rootView.findViewById(R.id.edtIdentifiantStock);
        edtMontantTotalStock =(EditText) rootView.findViewById(R.id.edtMontantTotalStock);
        edtDateStock =(EditText) rootView.findViewById(R.id.edtDateStock);
        edtDescStock =(EditText) rootView.findViewById(R.id.edtDescStock);
        btnEnregistrerStock=(AppCompatButton) rootView.findViewById(R.id.btnEnregistrerStock);

        btnEnregistrerStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateStock=edtDateStock.getText().toString();
                String splitDate[]=dateStock.split("/");
                if(edtIdentifiantStock.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Identifiant achat Stock...", Toast.LENGTH_LONG).show();
                }
                else if(edtMontantTotalStock.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Identifiant achat Stock...", Toast.LENGTH_LONG).show();
                }
                else if(edtDateStock.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Entrer Date Achat Stock", Toast.LENGTH_SHORT).show();
                }
                else if(splitDate.length<3 || splitDate.length>3 ){
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
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    
                    identifiantStock=edtIdentifiantStock.getText().toString();
                    montantStock=Double.valueOf(edtMontantTotalStock.getText().toString());
                    //get date
                    String jourStock=splitDate[0];
                    String moisStock=splitDate[1];
                    String anneeStock=splitDate[2];
                    descStock=edtDescStock.getText().toString();
                    
                    saveStock(entrepriseIdLogin, identifiantStock, montantStock, dateStock, descStock);
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

    public void saveStock(String entrepriseId, String identifiantStoc, Double montantStoc, String dateStoc, String descStoc)
    {
        Date d= new Date(dateStoc+" "+"00:00:00");
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ObjectEntreprise", "");
        Entreprise_info obj = gson.fromJson(json, Entreprise_info.class);

        Gestion_stock newStock = new Gestion_stock();
        newStock.setId_stock(identifiantStoc);
        newStock.setMontantTotal_stock(montantStoc);
        newStock.setDate_stock(d);
        newStock.setDescription_stock(descStoc);
        newStock.setEntrepriseID(obj);

        Backendless.Persistence.save( newStock, new AsyncCallback<Gestion_stock>() {
            @Override
            public void handleResponse(Gestion_stock response) {
                edtIdentifiantStock.getText().clear();
                edtMontantTotalStock.getText().clear();
                edtDateStock.getText().clear();
                edtDescStock.getText().clear();

                Toast.makeText(getActivity(), "Enregistrer avec succès...", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    saveStock(entrepriseIdLogin, identifiantStock, montantStock, dateStock, descStock);
                    echec++;
                }else{
                    Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    DialogAddStock.this.dismiss();
                }
            }
        });
    }
}