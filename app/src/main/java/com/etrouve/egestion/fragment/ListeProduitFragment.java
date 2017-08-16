package com.etrouve.egestion.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.etrouve.egestion.R;
import com.etrouve.egestion.adapters.ProduitArrayAdapter;
import com.etrouve.egestion.dialogs.DialogAddProduit;
import com.etrouve.egestion.models.Produit_info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeProduitFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String entrepriseIdLogin;
    String typeUser;
    String objectUser;
    public String typeLogin;
    private ArrayList<Produit_info> List_produit;
    private ProduitArrayAdapter produitArrayAdapter;
    ListView lvProduit;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    FloatingActionButton fabAddProd;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeProduitFragment f = new ListeProduitFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_produit, null);
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvProduit=(ListView)root.findViewById(R.id.lvProduit);

        tvPullRefresh.setVisibility(View.GONE);
        findProduitList();
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

        typeLogin = sharedPreferences.getString("typeLogin", null);
        fabAddProd = (FloatingActionButton) root.findViewById(R.id.fabAddProd);
        if(typeLogin.equals("CAISSIER")){
            fabAddProd.setVisibility(View.GONE);
        }
        fabAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                DialogAddProduit dialog_register_type = DialogAddProduit.newInstance("Enregistrer Produit");
                dialog_register_type.show(fm, "dialog_add_produit");
            }
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getFragmentManager().getBackStackEntryCount() == 0) getActivity().finish();
            }
        });
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

    private void findProduitList() {

        List_produit = new ArrayList<>();
        produitArrayAdapter = new ProduitArrayAdapter(getContext(),List_produit);
        lvProduit.setAdapter(produitArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        dataQuery.setWhereClause( "categorieID.entrepriseID.objectId='"+entrepriseIdLogin+"'" );
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
                Toast.makeText(getContext(), " Error connexion internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}