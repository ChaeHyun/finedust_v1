package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.AddressList;
import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.view.Views;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchAddressActivityPresenter implements Presenter.SearchAddressActivityPresenter {
    private static final String TAG = SearchAddressActivityPresenter.class.getSimpleName();

    private Views.SearchAddressActivityView view;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    public SearchAddressActivityPresenter(Views.SearchAddressActivityView view) {
        this.view = view;
        apiService = RetrofitClient.getApiService();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getAddressData(Context context, String UmdName) {
        if (!CheckConnectivity.checkNetworkConnection(context)) {
            view.enableNetworkOptions();
        }

        Map<String, String> queryParams = RetrofitClient.setQueryParamsForAddress(UmdName);
        // Log.v(TAG, "Check URL : " + apiService.getAddressData(queryParams).request().url().toString());
        addDisposable(apiService.getAddressData(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddressList>() {
                    @Override
                    public void accept(AddressList addressList) throws Exception {
                        List<Addresses> list = addressList.getList();
                        if (list.isEmpty())
                            view.showSnackBarMessage("검색결과가 없습니다.");
                        else {
                            Log.i(TAG, "ORDER to view : updateAddressData");
                            view.updateAddressData(list);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.v(TAG, Const.STR_FAIL_GET_DATA_FROM_SERVER);
                        view.showToastMessage(Const.STR_FAIL_GET_DATA_FROM_SERVER);
                    }
                }));
    }

    @Override
    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        compositeDisposable.clear();
    }
}
