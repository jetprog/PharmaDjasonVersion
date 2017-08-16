package com.etrouve.egestion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.etrouve.egestion.dialogs.DialogAddCategorie;
import com.etrouve.egestion.fragment.ListeCategorieFragment;
import com.etrouve.egestion.fragment.ListeCommandeFragment;
import com.etrouve.egestion.fragment.ListeUniteEntrepriseFragment;
import com.etrouve.egestion.fragment.ListeUserFragment;
import com.etrouve.egestion.fragment.caissier.CaissierAccueilFragment;

import static com.etrouve.egestion.utils.BackendSettings.APP_ID;
import static com.etrouve.egestion.utils.BackendSettings.SECRET_KEY;
import static com.etrouve.egestion.utils.BackendSettings.VERSION;

public class MainCaissierActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FloatingActionButton fab;
    //step 1 navigation drawer with fragment
    Fragment fragment = null;
    Class fragmentClass;
    FragmentManager fragmentManager;
    String titleFrag;

    SharedPreferences getSharedPreferences ;
    SharedPreferences.Editor editor ;
    ProgressDialog progressDialog;

    public String telephoneLogin;
    public String typeLogin;
    public String nameLogin;
    public String emailLogin;
    public int passwordLogin;
    public String objectIdLogin;
    public String entrepriseIdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_caissier);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);
        getSharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(MainCaissierActivity.this,R.style.Custom);

        telephoneLogin = getSharedPreferences.getString("telephoneLogin", null);
        typeLogin = getSharedPreferences.getString("typeLogin", null);
        nameLogin = getSharedPreferences.getString("nameLogin", null);
        emailLogin = getSharedPreferences.getString("emailLogin", null);
        passwordLogin = getSharedPreferences.getInt("passwordLogin", 0);
        entrepriseIdLogin = getSharedPreferences.getString("entrepriseIdLogin", null);
        objectIdLogin = getSharedPreferences.getString("objectIdLogin", null);

        if(telephoneLogin!=null && typeLogin!=null && nameLogin!=null && emailLogin!=null && passwordLogin!=0 && objectIdLogin!=null && entrepriseIdLogin!=null){
            Toast.makeText(getApplicationContext(),entrepriseIdLogin, Toast.LENGTH_LONG).show();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(typeLogin.equals("CAISSIER")){
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, titleFrag+" Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FragmentManager fm = getSupportFragmentManager();
                DialogAddCategorie dialog_register_type = DialogAddCategorie.newInstance("Enregistrer Categorie");
                dialog_register_type.show(fm, "dialog_add_categorie");
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //step 2
        defaultFragment();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // step 3
    private void defaultFragment() {
        fragmentClass = CaissierAccueilFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle("NavigationDrawer");
        titleFrag= (String) getTitle();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleFrag= (String) getTitle();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(titleFrag!="NavigationDrawer"){
            defaultFragment();
        }else if(titleFrag=="NavigationDrawer"){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
                EditText et = (EditText) searchView.findViewById(searchEditId);
                String etSearch =et.getText().toString();
                searchQuery(query, etSearch);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchQuery(String query, String etSearch) {
        if(titleFrag.equals("Categorie Produit")){
            fragmentClass = ListeCategorieFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),query+" "+etSearch+" "+titleFrag,Toast.LENGTH_LONG).show();
        }else if(titleFrag.equals("Historique vente")){
            Toast.makeText(getApplicationContext(),query+" "+etSearch+" "+titleFrag,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Recherche non disponible...",Toast.LENGTH_LONG).show();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        titleFrag= query;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_vente:
                fragmentClass = CaissierAccueilFragment.class;
                break;
            case R.id.nav_categorie:
                fragmentClass = ListeCategorieFragment.class;
                break;
            case R.id.nav_histo_vente:
                fragmentClass = ListeCommandeFragment.class;
                break;
            case R.id.nav_histo_connexion:
                fragmentClass = CaissierAccueilFragment.class;
                break;
            case R.id.nav_unite_ent:
                fragmentClass = ListeUniteEntrepriseFragment.class;
                break;
            case R.id.nav_compte:
                fragmentClass = CaissierAccueilFragment.class;
                break;
            case R.id.nav_caissier:
                fragmentClass = ListeUserFragment.class;
                break;
            case R.id.nav_deconnexion:
                deconnexion();
                break;
            default:
                fragmentClass = CaissierAccueilFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        titleFrag= (String) item.getTitle();
        // Close the navigation drawer

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deconnexion() {

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Deconnexion en cours...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Backendless.UserService.logout(new AsyncCallback<Void>() {
            public void handleResponse(Void response) {
                getSharedPreferences.edit().clear().apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                progressDialog.dismiss();
                finish();
            }

            public void handleFault(BackendlessFault fault) {
                Log.d("DEBUG", "Code: "+fault.getCode()+" - Message: "+fault.getMessage());
            }
        });
    }
}
