package com.finedust.presenter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finedust.model.Address;
import com.finedust.model.AddressList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.view.Views;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAddressActivityPresenter implements Presenter.SearchAddressActivityPresenter {
    private static final String TAG = SearchAddressActivityPresenter.class.getSimpleName();

    private Views.SearchAddressActivityView view;

    public SearchAddressActivityPresenter(Views.SearchAddressActivityView view) {
        this.view = view;
    }

    @Override
    public void getAddressData(Context context, String UmdName) {

        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService  apiService = RetrofitClient.getApiService();

            Map<String, String> queryParams = RetrofitClient.setQueryParamsForAddress(UmdName);

            Log.v(TAG, "Check URL : " + apiService.getAddressData(queryParams).request().url().toString());
            final Call<AddressList> requestForAddressData = apiService.getAddressData(queryParams);

            requestForAddressData.enqueue(new Callback<AddressList>() {
                @Override
                public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                    if(response.isSuccessful()) {
                        ArrayList<Address> addressList = response.body().getList();
                        if(addressList.size() > 0) {
                            Log.i(TAG, "ORDER to view : updateAddressData");
                            view.updateAddressData(addressList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddressList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage("Fail to get data from server");
                }
            });
        }
        else {
            view.enableNetworkOptions();
        }
    }
}
