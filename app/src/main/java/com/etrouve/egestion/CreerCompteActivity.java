package com.etrouve.egestion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.etrouve.egestion.models.Entreprise_info;
import com.etrouve.egestion.models.Users;

import static com.etrouve.egestion.utils.BackendSettings.APP_ID;
import static com.etrouve.egestion.utils.BackendSettings.SECRET_KEY;
import static com.etrouve.egestion.utils.BackendSettings.VERSION;

public class CreerCompteActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    EditText edtNomEnt;
    String nomEnt;
    EditText edtTelephoneEnt;
    String telEnt;
    EditText edtEmailEnt;
    String emailEnt;
    EditText edtAdresseEnt;
    String adresseEnt;

    EditText edtUserName;
    String userName;
    EditText edtUserPhone;
    String userPhone;

    EditText edtPass;
    String userPass;
    EditText edtConfirmPass;
    String userConfirmPass;

    AppCompatButton btnEnregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_compte);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ajouter Entreprise:");

        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);
        progressDialog = new ProgressDialog(CreerCompteActivity.this, R.style.Custom);

        edtNomEnt= (EditText)findViewById(R.id.edtNomEnt);
        edtTelephoneEnt= (EditText)findViewById(R.id.edtTelephoneEnt);
        edtEmailEnt= (EditText)findViewById(R.id.edtEmailEnt);
        edtAdresseEnt= (EditText)findViewById(R.id.edtAdresseEnt);

        edtUserName= (EditText)findViewById(R.id.edtUserName);
        edtUserPhone= (EditText)findViewById(R.id.edtUserPhone);
        edtPass= (EditText)findViewById(R.id.edtPass);
        edtConfirmPass= (EditText)findViewById(R.id.edtConfirmPass);

        btnEnregistrer=(AppCompatButton) findViewById(R.id.btnEnregistrer);
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomEnt = edtNomEnt.getText().toString();
                telEnt = edtTelephoneEnt.getText().toString();
                emailEnt = edtEmailEnt.getText().toString();
                adresseEnt = edtAdresseEnt.getText().toString();
                userName = edtUserName.getText().toString();
                userPhone = edtUserPhone.getText().toString();
                userPass = edtPass.getText().toString();
                userConfirmPass = edtConfirmPass.getText().toString();
                if (nomEnt.equals("")) {
                    Toast.makeText(getApplicationContext(), "Entrer nom entreprise", Toast.LENGTH_SHORT).show();
                } else {
                    if (telEnt.equals("")) {
                        Toast.makeText(getApplicationContext(), "Entrer telephone entreprise", Toast.LENGTH_SHORT).show();
                    } else {
                        if (adresseEnt.equals("")) {
                            Toast.makeText(getApplicationContext(), "Entrer adresse entreprise", Toast.LENGTH_SHORT).show();
                        } else {
                            if (emailEnt.equals("")) {
                                Toast.makeText(getApplicationContext(), "Entrer email entreprise", Toast.LENGTH_SHORT).show();
                            } else {
                                if (userName.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Entrer nom propriétaire", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (userPhone.equals("")) {
                                        Toast.makeText(getApplicationContext(), "Entrer Numéro Utilisateur", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (userPass.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Entrer mot de passe", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (userConfirmPass.equals("")) {
                                                Toast.makeText(getApplicationContext(), "Confirmer mot de passe", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (userPass.equals(userConfirmPass)){
                                                    progressDialog.setIndeterminate(true);
                                                    progressDialog.setMessage("Anthentification en cours...");
                                                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();
                                                    verifEntUser(nomEnt,telEnt,emailEnt,adresseEnt,userName,userPhone,userConfirmPass);
                                                    Toast.makeText(getApplicationContext(), "Verification en cours....", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Mot de passe different, verifier a nouveau", Toast.LENGTH_SHORT).show();
                                                }}}}}}}}
                }
            }});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onConnecter(View view){
        finish();
    }

    public void onAide(View view){
        Toast.makeText(getApplicationContext(), "Aide non disponible", Toast.LENGTH_SHORT).show();
    }

    private void verifEntUser(final String nomEnt, final String telEnt, final String emailEnt, final String adresseEnt, final String userName, final String userPhone, final String userConfirmPass) {


        BackendlessDataQuery dataQueryEnt = new BackendlessDataQuery();
        dataQueryEnt.setWhereClause( "tel_entreprise='"+telEnt+"' OR email_entreprise='"+emailEnt+"'" );
        QueryOptions queryOptionsEnt = new QueryOptions();
        dataQueryEnt.setQueryOptions( queryOptionsEnt );

        Backendless.Persistence.of( Entreprise_info.class ).find( dataQueryEnt, new AsyncCallback<BackendlessCollection<Entreprise_info>>() {
            @Override
            public void handleResponse(final BackendlessCollection<Entreprise_info> entL) {
                if (entL.getCurrentPage().size()==0) {

                    BackendlessDataQuery dataQueryUser = new BackendlessDataQuery();
                    dataQueryUser.setWhereClause( "telephone='"+telEnt+"' OR email='"+emailEnt+"'" );
                    QueryOptions queryOptionsUSer = new QueryOptions();
                    dataQueryUser.setQueryOptions( queryOptionsUSer );

                    Backendless.Data.of( Users.class ).find( dataQueryUser, new AsyncCallback<BackendlessCollection<Users>>() {
                        @Override
                        public void handleResponse(BackendlessCollection<Users> userL) {
                            if (userL.getCurrentPage().size()==0) {
                                saveEntUser(nomEnt,telEnt,emailEnt,adresseEnt,userName,userPhone,userConfirmPass);
                            }else{
                                alerteMessage("Information concernant l'utilisateur sont enregistré dans le système.\nContacter administrateur pour plus de details...");
                            }
                        }

                        @Override
                        public void handleFault( BackendlessFault backendlessFault )
                        {
                            alerteMessage("Essayer a nouveau: ");
                        }
                    } );

                }else{
                    alerteMessage("Information concernant l'entreprise sont enregistré dans le système.\nContacter administrateur pour plus de details...");
                }
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                alerteMessage("Essayer a nouveau: ");
            }
        } );
    }

    private void saveEntUser(String nomEnt, String telEnt, String emailEnt, String adresseEnt, final String userName, final String userPhone, final String userConfirmPass) {

        final Entreprise_info newEntrepriseInfo = new Entreprise_info();
        newEntrepriseInfo.setNom_entreprise(nomEnt);
        newEntrepriseInfo.setTel_entreprise(telEnt);
        newEntrepriseInfo.setEmail_entreprise(emailEnt);
        newEntrepriseInfo.setAdresse_entreprise(adresseEnt);

        Backendless.Persistence.save(newEntrepriseInfo, new AsyncCallback<Entreprise_info>() {
            @Override
            public void handleResponse(Entreprise_info response) {

                final BackendlessUser user = new BackendlessUser();
                user.setProperty("type", "SUPER ADMINISTRATEUR");
                user.setProperty("telephone", userPhone);
                user.setProperty("name", userName);
                user.setProperty("entrepriseID",newEntrepriseInfo);
                user.setPassword(userConfirmPass);
//Backendless api for save a user
                Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Enregistrer avec succès...", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        alerteMessage("Echec sauvegarde, essayer a nouveau...");
                    }
                });
                
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                alerteMessage("Echec sauvegarde, essayer a nouveau...");
            }
        });
    }

    private void alerteMessage(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreerCompteActivity.this);

        progressDialog.dismiss();
        dialog.setTitle( "Alerte" )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                }).show();
    }
}