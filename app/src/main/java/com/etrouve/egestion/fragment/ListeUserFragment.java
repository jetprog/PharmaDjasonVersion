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
import com.etrouve.egestion.adapters.UsersArrayAdapter;
import com.etrouve.egestion.models.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeUserFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String typeUser;
    String entrepriseIdLogin;

    private ArrayList<Users> List_user;
    private UsersArrayAdapter usersArrayAdapter;
    ListView lvUser;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeUserFragment f = new ListeUserFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_user, null);

        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);

        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvUser=(ListView)root.findViewById(R.id.lvUser);

        tvPullRefresh.setVisibility(View.GONE);
        findUserList();
//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findUserList();
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

    private void findUserList() {

        List_user = new ArrayList<>();
        usersArrayAdapter = new UsersArrayAdapter(getContext(),List_user );
        lvUser.setAdapter(usersArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        typeUser = sharedPreferences.getString("typeLogin", null);
        if(typeUser.equals("ADMINISTRATEUR")){
            dataQuery.setWhereClause( "type!='SUPER ADMINISTRATEUR' AND entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        }else if(typeUser.equals("CAISSIER")){
            dataQuery.setWhereClause( "type='CAISSIER' AND entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        }else{
            dataQuery.setWhereClause( "entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        }
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all taux
        Backendless.Persistence.of( Users.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Users>>(){
            @Override
            public void handleResponse( BackendlessCollection<Users> foundUserAV )
            {
                Iterator<Users> UsersIterator = foundUserAV.getCurrentPage().iterator();
                while (UsersIterator.hasNext())
                {
                    Users newTaux = UsersIterator.next();
                    List_user.add(newTaux);

                }
                usersArrayAdapter.notifyDataSetChanged();
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