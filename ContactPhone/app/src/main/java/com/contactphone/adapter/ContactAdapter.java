package com.contactphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contactphone.R;
import com.contactphone.model.PhoneModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bekti on 08/05/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<PhoneModel> data   = new ArrayList<>();
    private Context mcontext;

    public ContactAdapter(Context context, List<PhoneModel> android) {
        this.data = android;
        this.mcontext = context;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int i) {
        PhoneModel phoneModel   = data.get(i);
        holder.textName.setText(phoneModel.name);
        holder.textPhone.setText(phoneModel.number);
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {
        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.item_contact, vGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textPhone;

        public ViewHolder(View v) {
            super(v);
            textName = (TextView) v.findViewById(R.id.name);
            textPhone = (TextView) v.findViewById(R.id.phoneNumber);
        }
    }

}