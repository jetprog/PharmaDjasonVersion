package com.etrouve.egestion.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Entreprise_info;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ingdjason on 6/19/17.
 */

public class DialogAddUser extends  android.support.v4.app.DialogFragment
{

    EditText edtUserName;
    EditText edtUserPhone;
    EditText edtUserEmail;
    AppCompatSpinner spDroitUser;
    AppCompatButton btnEnregistrer;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String entrepriseIdLogin;

    String nomUser;
    String phoneUser;
    String emailUser;
    String droitUser;
    int echec=0;

    public DialogAddUser()
    {
    }

    public static DialogAddUser newInstance(String title) {
        DialogAddUser frag = new DialogAddUser();
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
        View rootView = inflater.inflate(R.layout.dialog_add_user, container, false);
        getDialog().setTitle("simple dialog");

        //entrepriseIdLogin = ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        
        edtUserName =(EditText) rootView.findViewById(R.id.edtUserName);
        edtUserPhone =(EditText) rootView.findViewById(R.id.edtUserPhone);
        edtUserEmail =(EditText) rootView.findViewById(R.id.edtUserEmail);
        spDroitUser =(AppCompatSpinner) rootView.findViewById(R.id.spDroitUser);
        btnEnregistrer=(AppCompatButton) rootView.findViewById(R.id.btnEnregistrer);
        spDroitUser.setSelection(2);

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUserName.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer nom Utilisateur", Toast.LENGTH_LONG).show();
                }
                else if(edtUserPhone.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Entrer Telephone Utilisateur", Toast.LENGTH_LONG).show();
                }
                else{
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    
                    nomUser=edtUserName.getText().toString();
                    phoneUser=edtUserPhone.getText().toString();
                    emailUser=edtUserEmail.getText().toString();
                    droitUser=spDroitUser.getSelectedItem().toString();
                    
                    saveUser(entrepriseIdLogin, nomUser, phoneUser, emailUser, droitUser);
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

    public void saveUser(String entrepriseId, String nomUsers, String phoneUsers, String emailUsers, String droitUsers)
    {
//generate pass user
        String[] generatePass= new String[]{"1@!", "2@$","3@&","4@Z","5@#", "6h$", "7m_"};
        List<String> list = Arrays.asList(generatePass);
        Collections.shuffle(list);
        final String s= entrepriseId.substring(0, 3)+generatePass[0];

//end generate pass

        Gson gson = new Gson();
        String json = sharedPreferences.getString("ObjectEntreprise", "");
        Entreprise_info obj = gson.fromJson(json, Entreprise_info.class);
        final BackendlessUser user = new BackendlessUser();
        user.setProperty("type", droitUsers);
        user.setProperty("telephone", phoneUsers);
        user.setProperty("name", nomUsers);
        user.setProperty("email", emailUsers);
        user.setProperty("entrepriseID",obj);
        user.setPassword(s);
//Backendless api for save a user
        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                progressDialog.dismiss();
                String etat="0";
                showAlert("Mot de passe: "+s, etat);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    saveUser(entrepriseIdLogin, nomUser, phoneUser, emailUser, droitUser);
                    echec++;
                }else{
                    String etat="1";
                    showAlert("Essayer a nouveau",etat);
                }
            }
        });
    }

    private void showAlert(final String message, final String etat)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if(etat.equals("0")){
            dialog.setTitle( "Alerte" )
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("telephone:\t"+phoneUser+"\nMot de passe:\t"+message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                            //dialoginterface.cancel();
                        }
                    }).show();
        }else{
            dialog.setTitle( "Alerte" )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                            //dialoginterface.cancel();
                        }
                    }).show();
        }
    }
}