package com.finedust.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.FragmentAirConditionBinding;
import com.finedust.model.AirCondition;
import com.finedust.model.adapter.MyAdapter;
import com.finedust.presenter.AirConditionFragmentPresenter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AirConditionFragment extends Fragment implements Views.AirConditionFragmentView {
    private static final String TAG = AirConditionFragment.class.getSimpleName();

    FragmentAirConditionBinding  binding;
    AirConditionFragmentPresenter airConditionFragmentPresenter = new AirConditionFragmentPresenter(this);
    private MyAdapter adapter;

    public AirConditionFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_air_condition, container, false);
        binding.setAircondition(this);

        binding.button.setText("버튼");
        binding.listView.setOnItemClickListener(onClickListViewItem);

        return binding.getRoot();
    }

    private AdapterView.OnItemClickListener onClickListViewItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if(adapter != null) {
                Toast.makeText(getContext(),
                        "PM10 Value : " + adapter.getItem(position).getPm10Value()
                                + "\nCO Value : " + adapter.getItem(position).getCoValue()
                                + "\nSO2 Value : " + adapter.getItem(position).getSo2Value()
                        , Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onSampleButtonClick(View view) {
        String temporaryStationName = "호림동";
        Log.i(TAG, "onSamplelButtonoClick()");
        binding.button.setText("Changed");

        //Presenter를 이용해서 airConditionPresenter의 onSampleButtonClicked() 메소드를 호출.
        //프리젠터에 호출을 요청하고 난 뒤에 역할 끝.

        // .onSampleButtonClicked() 내부에서 Business Logic을 처리한 후 View에게 업데이트를 요청. (view.showTestToastMessage)
        airConditionFragmentPresenter.getAirConditionData(getContext(), temporaryStationName);
        airConditionFragmentPresenter.onSampleButtonClicked();

    }

    @Override
    public void updateAirConditionData(ArrayList<AirCondition> data) {
        Log.i(TAG, "updating Air Condition Data to List Adapter");
        adapter = new MyAdapter(getContext(), 0, data);
        binding.listView.setAdapter(adapter);
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
