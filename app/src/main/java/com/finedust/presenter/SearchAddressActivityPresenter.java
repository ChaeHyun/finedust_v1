package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.Addresses;
import com.finedust.model.AddressList;
import com.finedust.model.Const;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.view.Views;

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
                        if (response.body().getTotalCount().equals(0))
                            view.showSnackBarMessage("검색결과가 없습니다.");
                        else {
                            ArrayList<Addresses> addressList = response.body().getList();
                            Log.i(TAG, "ORDER to view : updateAddressData");

                            view.updateAddressData(addressList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddressList> call, Throwable t) {
                    Log.v(TAG, Const.STR_FAIL_GET_DATA_FROM_SERVER);
                    view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                }
            });
        }
        else {
            view.enableNetworkOptions();
        }
    }
}
