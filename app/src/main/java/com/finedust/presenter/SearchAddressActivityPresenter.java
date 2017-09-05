package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.Address;
import com.finedust.model.AddressList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.InternetConnection;
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
    public void getAddressData(Context context, String addr) {

        if(InternetConnection.checkConnection(context)) {
            ApiService  apiService = RetrofitClient.getApiService();

            Map<String, String> queryParams = RetrofitClient.setQueryParamsForAddress(addr);

            Log.v(TAG, "Check URL : " + apiService.getAddressData(queryParams).request().url().toString());
            final Call<AddressList> requestForAddressData = apiService.getAddressData(queryParams);

            requestForAddressData.enqueue(new Callback<AddressList>() {
                @Override
                public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                    if(response.isSuccessful()) {
                        ArrayList<Address> addressList = response.body().getList();

                        if(addressList.size() > 0) {
                            for(int i = 0; i < addressList.size(); i++){
                                /*
                                Log.v(TAG, "Check Response Data : "
                                        + "\n SidoName : " + addressList.get(i).getSidoName()
                                        + "\n Ssg : " + addressList.get(i).getSggName()
                                        + "\n Umd : " + addressList.get(i).getUmdName()
                                        + "\n TmX : " + addressList.get(i).getTmX()
                                        + "\n TmY : " + addressList.get(i).getTmY()
                                );
                                 */
                            }
                        }
                        Log.i(TAG, "ORDER to view : updateAddressData");
                        view.updateAddressData(addressList);
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
            view.showToastMessage("Internet Connection is not available now.");
        }

    }
}
