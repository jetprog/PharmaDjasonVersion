package com.etrouve.egestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.etrouve.egestion.adapters.ProduitArrayAdapter;
import com.etrouve.egestion.dialogs.DialogAddProduit;
import com.etrouve.egestion.models.Produit_info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ListeProduitActivity extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String entrepriseIdLogin;
    String typeUser;
    String objectUser;
    String nomCategorie;
    String jsonCategorie;
    public String typeLogin;
    FloatingActionButton fabAddProd;
    private ArrayList<Produit_info> List_produit;
    private ProduitArrayAdapter produitArrayAdapter;
    ListView lvProduit;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_produit);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();

        nomCategorie = i.getStringExtra("nomCategorie");
        jsonCategorie=i.getStringExtra("objCategorie");
        getSupportActionBar().setTitle(nomCategorie);

        sharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        typeLogin = sharedPreferences.getString("typeLogin", null);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        lvProduit=(ListView) findViewById(R.id.lvProduit);

        tvPullRefresh.setVisibility(View.GONE);
        findProduitList();

        fabAddProd = (FloatingActionButton) findViewById(R.id.fabAddProd);
        if(typeLogin.equals("CAISSIER")){
            fabAddProd.setVisibility(View.GONE);
        }
        fabAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("jsonObjCategorie", jsonCategorie);

                FragmentManager fm = getSupportFragmentManager();
                DialogAddProduit dialog_register_type = DialogAddProduit.newInstance("Enregistrer Categorie");
                dialog_register_type.setArguments(bundle);
                dialog_register_type.show(fm, "dialog_add_categorie");
            }
        });

//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findProduitList();
                swipeRefresh.setRefreshing(false);
            }
        });

// Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);
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
    private void findDate() {
        //Get HHours and Minutes
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy ⏤ HH:mm");
        formatDate = simpleDateFormat.format(new Date());

        String dayTranslate=new SimpleDateFormat("EEEE").format(new Date());
        switch(dayTranslate) {
            case "Monday":
                frenchJour="Lundi";
                break;
            case "Tuesday":
                frenchJour="Mardi";
                break;
            case "Wednesday":
                frenchJour="Mercredi";
                break;
            case "Thursday":
                frenchJour="Jeudi";
                break;
            case "Friday":
                frenchJour="Vendredi";
                break;
            case "Saturday":
                frenchJour="Samedi";
                break;
            case "Sunday":
                frenchJour="Dimanche";
                break;
            default:
                frenchJour=" ";
        }
    }

    private void findProduitList() {

        List_produit = new ArrayList<>();
        produitArrayAdapter = new ProduitArrayAdapter(getApplicationContext(),List_produit);
        lvProduit.setAdapter(produitArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        dataQuery.setWhereClause( "categorieID.entrepriseID.objectId='"+entrepriseIdLogin+"' AND categorieID.nom_categorie='"+nomCategorie+"'" );
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all
        Backendless.Persistence.of( Produit_info.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Produit_info>>(){
            @Override
            public void handleResponse( BackendlessCollection<Produit_info> foundProduitAV )
            {
                Iterator<Produit_info> ProduitIterator = foundProduitAV.getCurrentPage().iterator();
                while (ProduitIterator.hasNext())
                {
                    Produit_info newProduit = ProduitIterator.next();
                    List_produit.add(newProduit);
                }
                produitArrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                progressBar.setVisibility(View.GONE);
                tvPullRefresh.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Prod: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
