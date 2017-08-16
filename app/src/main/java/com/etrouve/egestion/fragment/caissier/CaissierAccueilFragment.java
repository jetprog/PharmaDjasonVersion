package com.etrouve.egestion.fragment.caissier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.etrouve.egestion.MainCaissierActivity;
import com.etrouve.egestion.R;
import com.etrouve.egestion.adapters.ListeAchatProduitArrayAdapter;
import com.etrouve.egestion.models.UniteRef_entreprise;
import com.etrouve.egestion.models.UniteRef_produit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ingdjason on 6/13/17.
 */

public class CaissierAccueilFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;
    String entrepriseIdLogin;

    AppCompatSpinner spUniteVente;
    AppCompatButton btnFindProduit;
    AppCompatButton btnVente;
    TextView tvDateJour;
    EditText edtGetProduit;
    EditText edtQteProduit;

    String uniteSelected;
    String nomProduit;
    int qteProduit;
    int echec=0;
    ListView lvPanierVente;
    /*
    ArrayList<String> panierVente;
    ArrayAdapter<String> adapterPanierVente; */

    private ArrayList<UniteRef_produit> List_achat;
    private ListeAchatProduitArrayAdapter listeAchatProduitArrayAdapter;


    ArrayAdapter<String> adapterUniteEnt;
    ArrayList<String> uniteEnt;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences ;

    int parts;
    public static ArrayList<UniteRef_entreprise> unite;
    public static Fragment newInstance(Context context) {
        CaissierAccueilFragment f = new CaissierAccueilFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_accueil_caissier, null);

        progressDialog = new ProgressDialog(getActivity(),R.style.Custom);
        sharedPreferences = getActivity().getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        entrepriseIdLogin = sharedPreferences.getString("entrepriseIdLogin", null);
        spUniteVente=(AppCompatSpinner)root.findViewById(R.id.spUniteVente);
        btnFindProduit=(AppCompatButton)root.findViewById(R.id.btnFindProduit);
        btnVente=(AppCompatButton)root.findViewById(R.id.btnVente);
        lvPanierVente=(ListView) root.findViewById(R.id.lnPanierVente);
        tvDateJour=(TextView)root.findViewById(R.id.tvDateJour);
        edtGetProduit=(EditText) root.findViewById(R.id.edtGetProduit);
        edtQteProduit=(EditText) root.findViewById(R.id.edtQteProduit);

        findDate();
        String telephoneLogin = ((MainCaissierActivity)this.getActivity()).telephoneLogin;
        Toast.makeText(getContext(), telephoneLogin, Toast.LENGTH_LONG).show();

        /*panierVente = new ArrayList<>();
        adapterPanierVente = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, panierVente);
        lvPanierVente.setVisibility(View.GONE); */

        uniteEnt = new ArrayList<>();
        adapterUniteEnt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, uniteEnt);
        adapterUniteEnt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUniteVente.setAdapter(adapterUniteEnt);
        spUniteVente.setFocusableInTouchMode(false);
        btnFindProduit.setVisibility(View.GONE);
        loadUniteEntreprise();

        btnFindProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uniteSelected=spUniteVente.getSelectedItem().toString();
                nomProduit=edtGetProduit.getText().toString();
                qteProduit= Integer.parseInt(edtQteProduit.getText().toString());

                String spZero="Toucher→ Choisir unite:";
                if(uniteSelected.equals(spZero)){
                    Toast.makeText(getContext(), "Selectionner unité de vente...", Toast.LENGTH_LONG).show();
                }else if(nomProduit.equals("")){
                    Toast.makeText(getContext(), "Entrer nom produit...", Toast.LENGTH_LONG).show();
                }else if(edtQteProduit.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Quantité produit...", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sauvegarde en cours...");
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    findProduit(uniteSelected,nomProduit,qteProduit);
                }
            }
        });

        List_achat = new ArrayList<>();
        listeAchatProduitArrayAdapter = new ListeAchatProduitArrayAdapter(getContext(),List_achat );
        lvPanierVente.setAdapter(listeAchatProduitArrayAdapter);

        /*
        spUniteVente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //UniteRef_entreprise obj = (UniteRef_entreprise) spUniteVente.getItemAtPosition(pos);

                String findProduitCat = String.valueOf(adapterUniteEnt.getItemId(pos));

                //String findProduitCat = obj.getObjectId().toString();
                Toast.makeText(getContext(),findProduitCat,Toast.LENGTH_SHORT).show();

                Object item = parent.getItemAtPosition(pos);
                Object o =item;
                String s= String.valueOf(adapterUniteEnt.getItemId(pos));
                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); */
        /* save arrayList
        Bundle value= new Bundle();
        value.putStringArrayList("temp1", al);
        */
        /*TextView textView = new TextView(this);
        textView.setText(textArray[i]);
        linearLayout.addView(textView);*/
        return root;
    }

    private void findDate() {
        //Get HHours and Minutes
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
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

        tvDateJour.setText(frenchJour+", "+formatDate);
    }

    public void loadUniteEntreprise() {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        //dataQuery.setWhereClause( "sigle_monnaie='"++"' and bank_ref_monnaie='BRH'" );
        queryOptions.addSortByOption("nom_uniteRef DESC");
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(UniteRef_entreprise.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UniteRef_entreprise>>() {
            @Override
            public void handleResponse(BackendlessCollection<UniteRef_entreprise> uniterTrouver) {
                unite = (ArrayList<UniteRef_entreprise>) uniterTrouver.getData();
                uniteEnt.clear();
                uniteEnt.add("Toucher→ Choisir unite:");
                for (UniteRef_entreprise devises : unite) {
                    if(devises.getEntrepriseID().getObjectId().equals(entrepriseIdLogin)){
                        uniteEnt.add(devises.getNom_uniteRef());
                    }
                }
                adapterUniteEnt.notifyDataSetChanged();
                spUniteVente.setEnabled(true);
                spUniteVente.setFocusableInTouchMode(true);
                btnFindProduit.setVisibility(View.VISIBLE);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                loadUniteEntreprise();
                btnFindProduit.setVisibility(View.GONE);
            }
        });
    }

    public void findProduit(String uniteSelect, String nomProd, final int qteProd) {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        dataQuery.setWhereClause( "uniteEnt_ID.entrepriseID.objectId=='"+entrepriseIdLogin+"' AND uniteEnt_ID.nom_uniteRef=='"+uniteSelect+"' AND produitID.nom_produit=='"+nomProd+"' AND produitID.qte_produit<='"+qteProd+"'" );
        queryOptions.addSortByOption("nom_uniteRef DESC");
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(UniteRef_produit.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UniteRef_produit>>() {
            @Override
            public void handleResponse(BackendlessCollection<UniteRef_produit> produitTrouver) {
                Iterator<UniteRef_produit> ProduitIterator = produitTrouver.getCurrentPage().iterator();
                while (ProduitIterator.hasNext())
                {
                    UniteRef_produit newProduitInfo = ProduitIterator.next();
                    List_achat.add(newProduitInfo);

                    /*String findNomProd= newProduitInfo.getProduitID().getNom_produit();
                    int findQteProd=newProduitInfo.getProduitID().getQte_produit();
                    String findUniteProd=newProduitInfo.getUniteEnt_ID().getNom_uniteRef();
                    Double findPrixProd=newProduitInfo.getPrix_unite();

                    Toast.makeText(getContext(),"Nom :\t"+findNomProd+"\nQte :\t"+findQteProd+"\nUnite :\t"+findUniteProd+"\nPrix :\t"+findPrixProd+"\nPrix Total:\t"+(qteProd*findPrixProd),Toast.LENGTH_SHORT).show();
                    panierVente.add("Nom :\t"+findNomProd+"\nQte :\t"+findQteProd+"\nUnite :\t"+findUniteProd+"\nPrix :\t"+findPrixProd+"\nPrix Total:\t"+(qteProd*findPrixProd));
                    adapterPanierVente.notifyDataSetChanged();
                    lvPanierVente.setAdapter(adapterPanierVente);
                    lvPanierVente.setVisibility(View.VISIBLE); */
                }

                listeAchatProduitArrayAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(echec==0){
                    findProduit(uniteSelected,nomProduit,qteProduit);
                }else{
                    Toast.makeText(getContext(),"Ce produit n'est pas disponible...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}