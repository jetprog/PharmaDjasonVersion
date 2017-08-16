package com.etrouve.egestion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.etrouve.egestion.models.Users;
import com.google.gson.Gson;

import java.util.Iterator;

import static com.etrouve.egestion.utils.BackendSettings.APP_ID;
import static com.etrouve.egestion.utils.BackendSettings.SECRET_KEY;
import static com.etrouve.egestion.utils.BackendSettings.VERSION;

public class LoginActivity extends AppCompatActivity {
    EditText edtUserPhone;
    EditText edtPassUser;
    TextView tvCreerCompte;
    TextView tvForgetPass;
    TextView tvInformation;
    Button btnLogin;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Custom);
        
        edtUserPhone=(EditText)findViewById(R.id.edtUserPhone);
        edtPassUser=(EditText)findViewById(R.id.edtPassUser);

        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUserPhone.getText().toString().equals("")){

                }else if(edtPassUser.getText().toString().equals("")){

                }else{
                    String userPhone=edtUserPhone.getText().toString();
                    String userPass=edtPassUser.getText().toString();

                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Anthentification en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    findLoginUser(userPhone,userPass);
                }
            }
        });

        tvCreerCompte=(TextView)findViewById(R.id.tvCreerCompte);
        tvCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreerCompteActivity.class);
                startActivity(i);
            }
        });

        tvForgetPass=(TextView)findViewById(R.id.tvForgetPass);
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Mot de passe oublié...", Toast.LENGTH_LONG).show();
                //findUserPass();
                showAlertChangePAss();
            }
        });

        tvInformation=(TextView)findViewById(R.id.tvInformation);
        tvInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Information application...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findLoginUser(String userPhone, String userPass) {
        Backendless.UserService.login(userPhone, userPass, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(final BackendlessUser response) {
                sharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();

                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause( "telephone='"+response.getProperty("telephone").toString()+"' and objectId='"+response.getObjectId()+"'" );
                QueryOptions queryOptions = new QueryOptions();
                dataQuery.setQueryOptions( queryOptions );

                Backendless.Data.of( Users.class ).find( dataQuery, new AsyncCallback<BackendlessCollection<Users>>()
                {
                    @Override
                    public void handleResponse( BackendlessCollection<Users> usersL ) {
                        Iterator<Users> iterator = usersL.getCurrentPage().iterator();
                        Users userL = iterator.next();

                        final Gson gson = new Gson();
                        String json = gson.toJson(userL.getEntrepriseID());
                        editor.putString("ObjectEntreprise", json);

                        editor.putString("telephoneLogin", response.getProperty("telephone").toString());
                        editor.putString("typeLogin", response.getProperty("type").toString().toUpperCase());
                        editor.putString("nameLogin", response.getProperty("name").toString());
                        editor.putString("emailLogin", response.getEmail());
                        editor.putInt("passwordLogin", response.getUserId().hashCode());
                        //editor.putString("entrepriseLogin", response.getProperty("entrepriseID").toString());
                        editor.putString("objectIdLogin", response.getObjectId());

                        editor.putString("entrepriseIdLogin", userL.getEntrepriseID().getObjectId().toString());
                        editor.apply();

                        if(response.getProperty("type").toString().toUpperCase().equals("SUPER ADMINISTRATEUR")){
                            Intent i = new Intent(getApplicationContext(), MainSuperadminActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                            finish();
                        }
                        else if(response.getProperty("type").toString().toUpperCase().equals("ADMINISTRATEUR")){
                            Intent i = new Intent(getApplicationContext(), MainAdminActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                            finish();
                        }
                        else if(response.getProperty("type").toString().toUpperCase().equals("CAISSIER")){
                            Intent i = new Intent(getApplicationContext(), MainCaissierActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Essayer a nouveau...", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void handleFault( BackendlessFault backendlessFault )
                    {
                        System.out.println( "N'est pas connecté a un entreprise: " + backendlessFault.getMessage() );
                    }
                } );
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(),backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }, true);
    }


    private void showAlertChangePAss()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);

        LayoutInflater inflater=LoginActivity.this.getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_change_password, null);
        dialog.setView(layout);
        //dialog.setView(inflater.inflate(R.layout.dialog_change_password,null));
        final EditText edtChangePassPhone=(EditText)layout.findViewById(R.id.edtChangePassPhone);
        dialog.setTitle( "Changer Mot de Passe" )
                .setIcon(android.R.drawable.ic_lock_idle_lock)
                .setMessage("Entrer numéro Telephone de connexion: ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        String edtPhoneUser=edtChangePassPhone.getText().toString();
                        Toast.makeText(getApplicationContext(),edtPhoneUser,Toast.LENGTH_SHORT).show();
                        //dialoginterface.cancel();
                    }
                }).show();
    }

    private void findUserPass() {
        Backendless.UserService.restorePassword( "james.bond", new AsyncCallback<Void>()
        {
            public void handleResponse( Void response ){

                // / Backendless has completed the operation - an email has been sent to the user
            }
            public void handleFault( BackendlessFault fault ){
                // password revovery failed, to get the error code call fault.getCode()}
            }
        });
    }

}
