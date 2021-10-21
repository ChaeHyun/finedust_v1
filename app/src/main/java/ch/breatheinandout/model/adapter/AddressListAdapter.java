package ch.breatheinandout.model.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import ch.breatheinandout.R;
import ch.breatheinandout.databinding.ListviewAddressItemBinding;
import ch.breatheinandout.model.Addresses;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.utils.ChangeFont;

import java.util.List;


public class AddressListAdapter extends ArrayAdapter<Addresses> {
    private static final String TAG = AddressListAdapter.class.getSimpleName();

    List<Addresses> addressList;
    Context context;
    private LayoutInflater mInflater;

    public AddressListAdapter(@NonNull Context context, @LayoutRes int resource, List<Addresses> addressList) {
        super(context, resource);
        this.addressList = addressList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public Addresses getItem(int position) {
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

        Addresses addr = getItem(position);
        Typeface tf = ChangeFont.getTypefaceFromAsset(getContext(), Const.FONT_DEFAULT_NORMAL);
        binding.item.setTypeface(tf);
        binding.item.setText(addr.getSidoName() + " " + addr.getSggName() + " " + addr.getUmdName());

        return binding.getRoot();
    }

}
