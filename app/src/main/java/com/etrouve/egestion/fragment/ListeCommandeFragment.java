package com.etrouve.egestion.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.etrouve.egestion.adapters.CommandeArrayAdapter;
import com.etrouve.egestion.models.Commande;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeCommandeFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String entrepriseIdLogin;
    String typeUser;
    String objectUser;
    private ArrayList<Commande> List_commande;
    private CommandeArrayAdapter commandeArrayAdapter;
    ListView lvCommande;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeCommandeFragment f = new ListeCommandeFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_commande, null);

        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvCommande=(ListView)root.findViewById(R.id.lvCommande);

        tvPullRefresh.setVisibility(View.GONE);
        findCommandeList();
//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findCommandeList();
                swipeRefresh.setRefreshing(false);
            }
        });

// Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);

        return root;
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

    private void findCommandeList() {

        List_commande = new ArrayList<>();
        commandeArrayAdapter = new CommandeArrayAdapter(getContext(),List_commande);
        lvCommande.setAdapter(commandeArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        typeUser = sharedPreferences.getString("typeLogin", null);
        objectUser = sharedPreferences.getString("objectIdLogin", null);
        if(typeUser.equals("CAISSIER")){
            dataQuery.setWhereClause( "usersID.entrepriseID.objectId='"+entrepriseIdLogin+"' AND usersID.objectId='"+objectUser+"'" );
        }else if(typeUser.equals("ADMINISTRATEUR")){
            dataQuery.setWhereClause( "usersID.entrepriseID.objectId='"+entrepriseIdLogin+"' AND ( usersID.objectId='"+objectUser+"' OR usersID.type!='SUPER ADMINISTRATEUR' )" );
        }else {
            dataQuery.setWhereClause( "usersID.entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        }
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all
        Backendless.Persistence.of( Commande.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Commande>>(){
            @Override
            public void handleResponse( BackendlessCollection<Commande> foundCommandeAV )
            {
                Iterator<Commande> CommandeIterator = foundCommandeAV.getCurrentPage().iterator();
                while (CommandeIterator.hasNext())
                {
                    Commande newCommande = CommandeIterator.next();
                    List_commande.add(newCommande);
                }
                commandeArrayAdapter.notifyDataSetChanged();
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