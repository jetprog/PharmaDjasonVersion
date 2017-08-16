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
import com.etrouve.egestion.adapters.StockArrayAdapter;
import com.etrouve.egestion.models.Gestion_stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class ListeStockFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    SharedPreferences sharedPreferences ;
    String entrepriseIdLogin;
    String typeUser;
    String objectUser;
    private ArrayList<Gestion_stock> List_stock;
    private StockArrayAdapter stockArrayAdapter;
    ListView lvStock;
    TextView tvPullRefresh;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static Fragment newInstance(Context context) {
        ListeStockFragment f = new ListeStockFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_liste_stock, null);

        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        //entrepriseIdLogin= ((MainCaissierActivity)this.getActivity()).entrepriseIdLogin;
        progressBar=(ProgressBar) root.findViewById(R.id.progressBar);
        tvPullRefresh=(TextView) root.findViewById(R.id.tvPullRefresh);
        swipeRefresh=(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        lvStock=(ListView)root.findViewById(R.id.lvStock);

        tvPullRefresh.setVisibility(View.GONE);
        findStockList();
//Listen for Swipe Refresh to fetch data again
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvPullRefresh.setVisibility(View.GONE);
                findStockList();
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

    private void findStockList() {

        List_stock = new ArrayList<>();
        stockArrayAdapter = new StockArrayAdapter(getContext(),List_stock);
        lvStock.setAdapter(stockArrayAdapter);
        progressBar.setVisibility(View.VISIBLE);

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( "entrepriseID.objectId='"+entrepriseIdLogin+"'" );
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("created DESC");
        queryOptions.setPageSize( PAGESIZE );
        dataQuery.setQueryOptions(queryOptions);

//backendless load all
        Backendless.Persistence.of( Gestion_stock.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Gestion_stock>>(){
            @Override
            public void handleResponse( BackendlessCollection<Gestion_stock> foundStockAV )
            {
                Iterator<Gestion_stock> StockIterator = foundStockAV.getCurrentPage().iterator();
                while (StockIterator.hasNext())
                {
                    Gestion_stock newStock = StockIterator.next();
                    List_stock.add(newStock);
                }
                stockArrayAdapter.notifyDataSetChanged();
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