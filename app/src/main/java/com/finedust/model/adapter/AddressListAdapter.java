package com.finedust.model.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.finedust.R;
import com.finedust.databinding.ListviewAddressItemBinding;
import com.finedust.model.Address;
import com.finedust.model.AddressList;

import java.util.ArrayList;
import java.util.List;



public class AddressListAdapter extends ArrayAdapter<Address> {
    private static final String TAG = AddressListAdapter.class.getSimpleName();
    List<Address> addressList;
    Context context;
    private LayoutInflater mInflater;

    public AddressListAdapter(@NonNull Context context, @LayoutRes int resource, List<Address> addressList) {
        super(context, resource);
        this.addressList = addressList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public Address getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListviewAddressItemBinding binding;

        if(convertView == null) {
            binding = DataBindingUtil.inflate(mInflater, R.layout.listview_address_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        else {
            binding = (ListviewAddressItemBinding) convertView.getTag();
        }

        Address addr = getItem(position);
        binding.item.setText(addr.getSidoName() + " " + addr.getSggName() + " " + addr.getUmdName());

        return binding.getRoot();
    }
}
