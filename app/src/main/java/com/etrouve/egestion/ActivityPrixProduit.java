package com.etrouve.egestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.etrouve.egestion.adapters.PrixUniteProdArrayAdapter;
import com.etrouve.egestion.models.Produit_info;
import com.etrouve.egestion.models.UniteRef_entreprise;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;

public class ActivityPrixProduit extends AppCompatActivity {

    public String jsonProduit;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    String entrepriseIdLogin;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<UniteRef_entreprise> List_unite;
    private PrixUniteProdArrayAdapter prixProdbyUniteArrayAdapter;
    ListView lvUnite;
    public String typeLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix_produit);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        Intent i = getIntent();
        jsonProduit=i.getStringExtra("objProduit");
        editor.putString("objProduit", jsonProduit);
        editor.apply();


        Gson gson = new Gson();
        Produit_info objProduit = gson.fromJson(jsonProduit, Produit_info.class);
        objProduit.getNom_produit();
        Toast.makeText(getApplicationContext(), objProduit.getNom_produit(), Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle(objProduit.getNom_produit());

        //TextView textView2=(TextView)findViewById(R.id.textView2);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        lvUnite=(ListView)findViewById(R.id.lvUnitePrix);
        tvPullRefresh.setVisibility(View.GONE);
        //textView2.setText("Nom Produit :\t"+objProduit.getNom_produit()+"\nObjectID :\t"+objProduit.getObjectId());
        findUniteProd();

//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findUniteProd();
                swipeRefresh.setRefreshing(false);
            }
        });

// Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);

       /* lvUnite.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Categorie_produit obj = (Categorie_produit) lvUnite.getItemAtPosition(position);
                String findProduitCat = obj.getNom_categorie().toString();
            }
        }); */
    }

    private void findUniteProd() {
        List_unite = new ArrayList<>();
        prixProdbyUniteArrayAdapter = new PrixUniteProdArrayAdapter(getApplicationContext(),List_unite);
        lvUnite.setAdapter(prixProdbyUniteArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);
        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        /*
        if(typeUser.equals("CAISSIER")){
            dataQuery.setWhereClause( "entrepriseID='"+entrepriseIdLogin+"'" );
        } */
        dataQuery.setWhereClause( "entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all
        Backendless.Persistence.of( UniteRef_entreprise.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UniteRef_entreprise>>(){
            @Override
            public void handleResponse( BackendlessCollection<UniteRef_entreprise> foundUniteAV )
            {
                Iterator<UniteRef_entreprise> UniteIterator = foundUniteAV.getCurrentPage().iterator();
                while (UniteIterator.hasNext())
                {
                    UniteRef_entreprise newUniteEnt = UniteIterator.next();
                    List_unite.add(newUniteEnt);
                }
                prixProdbyUniteArrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                progressBar.setVisibility(View.GONE);
                tvPullRefresh.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Prix: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
