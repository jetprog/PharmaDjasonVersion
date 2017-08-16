package com.etrouve.egestion.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etrouve.egestion.R;
import com.etrouve.egestion.models.Users;
import com.etrouve.egestion.utils.DateFormat;

import java.util.ArrayList;

/**
 * Created by ingdjason on 6/21/17.
 */

public class UsersArrayAdapter extends ArrayAdapter<Users> {

    int viewInfo=0;
    public UsersArrayAdapter(Context context, ArrayList<Users> bank) {
        super(context, android.R.layout.simple_list_item_1, bank);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Users user = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_user, parent, false);
        }

        final LinearLayoutCompat lcMoreInfo = ( LinearLayoutCompat ) convertView.findViewById(R.id.lcMoreInfo);
        lcMoreInfo.setVisibility(View.GONE);
        LinearLayoutCompat lcUserInfo = ( LinearLayoutCompat ) convertView.findViewById(R.id.lcUserInfo);
        lcUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewInfo==0){
                    lcMoreInfo.setVisibility(View.VISIBLE);
                    viewInfo=1;
                }else if(viewInfo==1){
                    lcMoreInfo.setVisibility(View.GONE);
                    viewInfo=0;
                }else{
                    lcMoreInfo.setVisibility(View.GONE);
                    viewInfo=0;
                }
            }
        });
        //nom user
        AppCompatTextView tvUserName = ( AppCompatTextView ) convertView.findViewById(R.id.tvUserName);
        if(user.getName() != null){
            tvUserName.setText(user.getName().toString().toUpperCase());
        }else{
            String textNa = "NULL";
            tvUserName.setText(textNa);
        }
        //Tel user
        AppCompatTextView tvTelUser = ( AppCompatTextView ) convertView.findViewById(R.id.tvTelUser);
        if(user.getTelephone() != null){
            tvTelUser.setText(user.getTelephone().toString());
        }else{
            String textTel = "(+509) 00 00 0000";
            tvTelUser.setText(textTel);
        }

        AppCompatTextView tvMailUser = ( AppCompatTextView ) convertView.findViewById(R.id.tvMailUser);
        if(user.getEmail() != null){
            tvMailUser.setText(user.getEmail().toString());
        }else{
            String textMail = "default@mail.com";
            tvMailUser.setText(textMail);
        }

        AppCompatSpinner spTypeUser = ( AppCompatSpinner ) convertView.findViewById(R.id.spTypeUser);
        if(user.getType() != null){
            if(user.getType().equals("SUPER ADMINISTRATEUR")){
                spTypeUser.setSelection(0);
            }else if(user.getType().equals("ADMINISTRATEUR")) {
                spTypeUser.setSelection(1);
            }else if(user.getType().equals("CAISSIER")) {
                spTypeUser.setSelection(2);
            }
        }else{
            int sel = 2;
            spTypeUser.setSelection(sel);
        }

        AppCompatTextView tvDateAddUser = ( AppCompatTextView ) convertView.findViewById(R.id.tvDateAddUser);
        if(user.getCreated() != null){
            String strDate = DateFormat.formatDate(user.getCreated());
            tvDateAddUser.setText(strDate);
        }

        AppCompatTextView tvLastLoginUser = ( AppCompatTextView ) convertView.findViewById(R.id.tvLastLoginUser);
        if(user.getLastLogin() != null){
            String strDate = DateFormat.formatDate(user.getLastLogin());
            tvLastLoginUser.setText(strDate);
        }else{
            String textLastLogin = "00 / 00 / 00";
            tvLastLoginUser.setText(textLastLogin);
        }

        AppCompatSpinner spStatusUser = ( AppCompatSpinner ) convertView.findViewById(R.id.spStatusUser);
        if(user.getUserStatus() != null){
            if(user.getUserStatus().equals("ENABLED")){
                spStatusUser.setSelection(0);
            }else if(user.getUserStatus().equals("DISABLED")) {
                spStatusUser.setSelection(1);
            }
        }

        return convertView;
    }
}
