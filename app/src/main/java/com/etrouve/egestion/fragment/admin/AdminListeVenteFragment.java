package com.etrouve.egestion.fragment.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etrouve.egestion.MainAdminActivity;
import com.etrouve.egestion.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ingdjason on 6/13/17.
 */

public class AdminListeVenteFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    String formatDate;
    String frenchJour;

    public static Fragment newInstance(Context context) {
        AdminListeVenteFragment f = new AdminListeVenteFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_accueil_admin, null);

        String telephoneLogin = ((MainAdminActivity)this.getActivity()).telephoneLogin;
        Toast.makeText(getContext(), telephoneLogin, Toast.LENGTH_LONG).show();

        //findDate();
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
}