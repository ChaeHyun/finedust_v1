package ch.breatheinandout.model.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import ch.breatheinandout.R;
import ch.breatheinandout.databinding.ListviewItemBinding;
import ch.breatheinandout.model.AirCondition;

import java.util.List;

public class MyAdapter extends ArrayAdapter<AirCondition> {

    List<AirCondition> airConditionList;
    Context context;
    private LayoutInflater mInflater;

    public MyAdapter(Context context, int resource, List<AirCondition> airConditionList) {
        super(context, resource, airConditionList);
        this.airConditionList = airConditionList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public AirCondition getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListviewItemBinding binding;

        if(convertView == null) {
            binding = DataBindingUtil.inflate(mInflater, R.layout.listview_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        else {
            binding = (ListviewItemBinding) convertView.getTag();
        }

        AirCondition item = getItem(position);
        binding.textViewDate.setText(item.getDataTime());
        binding.textViewPm10.setText(item.getPm10Value());
        binding.textViewPm25.setText(item.getPm25Value());

        return binding.getRoot();
    }


}
