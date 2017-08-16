package com.etrouve.egestion.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.etrouve.egestion.ListeProduitActivity;
import com.etrouve.egestion.R;
import com.etrouve.egestion.adapters.CategorieArrayAdapter;
import com.etrouve.egestion.models.Categorie_produit;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeCategorieFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String typeUser;
    String objectUser;
    String entrepriseIdLogin;
    private ArrayList<Categorie_produit> List_categorie;
    private CategorieArrayAdapter categorieArrayAdapter;
    ListView lvCategorie;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeCategorieFragment f = new ListeCategorieFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_categorie, null);

        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvCategorie=(ListView)root.findViewById(R.id.lvCategorie);

        tvPullRefresh.setVisibility(View.GONE);
        findCategorieList();
//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findCategorieList();
                swipeRefresh.setRefreshing(false);
            }
        });

// Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);

        lvCategorie.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                Categorie_produit obj = (Categorie_produit) lvCategorie.getItemAtPosition(position);
                Gson gson= new Gson();
                String jsonCatego= gson.toJson(obj);
                String findProduitCat = obj.getNom_categorie().toString();

                //Toast.makeText(getContext(), findProduitCat, Toast.LENGTH_SHORT).show();

                showProduitCategorie(findProduitCat,jsonCatego);
            }
        });
        return root;
    }

    private void showProduitCategorie(String findProduitCat, String jsonCatego) {
        /* fragment to fragment

        ListeProduitFragment fragmentProd = new ListeProduitFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragmentProd);
        fragmentTransaction.commit();*/
        Intent i = new Intent(getActivity(), ListeProduitActivity.class);
        i.putExtra("nomCategorie", findProduitCat);
        i.putExtra("objCategorie", jsonCatego);
        startActivity(i);
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

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private void findCategorieList() {

        List_categorie = new ArrayList<>();
        categorieArrayAdapter = new CategorieArrayAdapter(getContext(),List_categorie);
        lvCategorie.setAdapter(categorieArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();


        dataQuery.setWhereClause( "entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        dataQuery.setWhereClause( "entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all
        Backendless.Persistence.of( Categorie_produit.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Categorie_produit>>(){
            @Override
            public void handleResponse( BackendlessCollection<Categorie_produit> foundCategorieAV )
            {
                Iterator<Categorie_produit> CategorieIterator = foundCategorieAV.getCurrentPage().iterator();
                while (CategorieIterator.hasNext())
                {
                    Categorie_produit newCategorie = CategorieIterator.next();
                    if(newCategorie.getEntrepriseID().getObjectId().equals(entrepriseIdLogin)){
                        List_categorie.add(newCategorie);
                    }
                }
                categorieArrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                progressBar.setVisibility(View.GONE);
                tvPullRefresh.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), " Error connexion internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}