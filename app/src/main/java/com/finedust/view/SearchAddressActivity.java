package com.finedust.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.finedust.R;
import com.finedust.databinding.ActivitySearchAddressBinding;
import com.finedust.model.Addresses;
import com.finedust.model.adapter.AddressListAdapter;
import com.finedust.presenter.SearchAddressActivityPresenter;
import com.finedust.utils.CheckConnectivity;

import java.util.List;

public class SearchAddressActivity extends AppCompatActivity implements Views.SearchAddressActivityView {
    private static final String TAG = SearchAddressActivity.class.getSimpleName();

    ActivitySearchAddressBinding binding;
    SearchAddressActivityPresenter searchAddressActivityPresenter = new SearchAddressActivityPresenter(this);
    AddressListAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_address);
        binding.setSearch(this);

        binding.listViewAddress.setVisibility(View.INVISIBLE);
        binding.listViewAddress.setOnItemClickListener(onClickListViewItem);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("주소검색");

        binding.editSearch.setOnKeyListener(onKeySearchEdit);
        binding.editSearch.setText("삼덕");

        binding.buttonSearch.setOnClickListener(onSearchButtonClick);

    }

    private ImageView.OnClickListener onSearchButtonClick = new ImageView.OnClickListener() {
        @Override
        public void onClick(View view) {

            binding.textVisible.setVisibility(View.VISIBLE);
            binding.listViewAddress.setVisibility(View.INVISIBLE);
            binding.listViewAddress.setOnItemClickListener(onClickListViewItem);

            if (!binding.editSearch.getText().toString().equals("")) {
                searchAddressActivityPresenter.getAddressData(getApplicationContext(), binding.editSearch.getText().toString());
            }
            else {
                Snackbar.make(view, "검색어를 입력해주세요.", Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    private AdapterView.OnItemClickListener onClickListViewItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Addresses data = (Addresses) adapterView.getAdapter().getItem(i);
            setResult(RESULT_OK, putIntentExtraString(data));

            finish();
        }
    };

    private Intent putIntentExtraString(Addresses addr) {
        Intent intent = new Intent();
        intent.putExtra("Sido", addr.getSidoName())
                .putExtra("Sgg", addr.getSggName())
                .putExtra("Umd", addr.getUmdName())
                .putExtra("Addr", addr.getSidoName() + " "+ addr.getSggName()+ " " + addr.getUmdName())
                .putExtra("TmX", addr.getTmX())
                .putExtra("TmY", addr.getTmY());

        return intent;
    }

    private View.OnKeyListener onKeySearchEdit = new View.OnKeyListener() {
        final String TAG = "OnKeyListener";
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                // Hide keypad
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.editSearch.getWindowToken(), 0);

                binding.buttonSearch.callOnClick();
                return true;
            }
            return false;
        }
    };


    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, 3000).show();
    }

    @Override
    public void enableNetworkOptions() {
        Snackbar.make(binding.getRoot(), "네트워크 연결이 필요합니다.", 5000).setAction("켜기", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckConnectivity.showNetworkDisabledAlert(SearchAddressActivity.this);
            }
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateAddressData(List<Addresses> data) {
        binding.textVisible.setVisibility(View.INVISIBLE);
        binding.listViewAddress.setVisibility(View.VISIBLE);

        Log.i(TAG, "# Updating an Address list to the List Adapter.");
        addressAdapter = new AddressListAdapter(getApplicationContext(), 0, data);
        addressAdapter.clear();
        addressAdapter.addAll(data);
        binding.listViewAddress.setAdapter(addressAdapter);
        //addressAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        searchAddressActivityPresenter.clearDisposable();
        super.onDestroy();
    }
}
