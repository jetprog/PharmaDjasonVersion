package com.etrouve.egestion.fragment;

import android.content.Context;
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
import com.etrouve.egestion.R;
import com.etrouve.egestion.adapters.UniteEntArrayAdapter;
import com.etrouve.egestion.models.Categorie_produit;
import com.etrouve.egestion.models.UniteRef_entreprise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeUniteEntrepriseFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String typeUser;
    String objectUser;
    String entrepriseIdLogin;
    
    private ArrayList<UniteRef_entreprise> List_unite;
    private UniteEntArrayAdapter uniteEntArrayAdapter;
    ListView lvUnite;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeUniteEntrepriseFragment f = new ListeUniteEntrepriseFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_unite_ent, null);
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        //String telephoneLogin = ((MainCaissierActivity)this.getActivity()).telephoneLogin; entrepriseIdLogin= ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        Toast.makeText(getContext(), entrepriseIdLogin, Toast.LENGTH_LONG).show();

        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvUnite=(ListView)root.findViewById(R.id.lvUnite);

        tvPullRefresh.setVisibility(View.GONE);
        findUniteEnt();
//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findUniteEnt();
                swipeRefresh.setRefreshing(false);
            }
        });

// Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);

        lvUnite.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Categorie_produit obj = (Categorie_produit) lvUnite.getItemAtPosition(position);
                String findProduitCat = obj.getNom_categorie().toString();
            }
        });

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

    private void findUniteEnt() {

        List_unite = new ArrayList<>();
        uniteEntArrayAdapter = new UniteEntArrayAdapter(getContext(),List_unite);
        lvUnite.setAdapter(uniteEntArrayAdapter);
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
                uniteEntArrayAdapter.notifyDataSetChanged();
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