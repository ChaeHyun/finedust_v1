package com.finedust.presenter;

import android.content.Context;
import android.util.Log;

import com.finedust.model.AirCondition;
import com.finedust.model.AirConditionList;
import com.finedust.retrofit.api.ApiService;
import com.finedust.retrofit.api.RetrofitClient;
import com.finedust.utils.CheckConnectivity;
import com.finedust.view.Views;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirConditionFragmentPresenter implements Presenter.AirConditionFragmentPresenter {
    private static final String TAG = AirConditionFragmentPresenter.class.getSimpleName();

    private Views.AirConditionFragmentView view;

    public AirConditionFragmentPresenter(Views.AirConditionFragmentView view) {
        this.view = view;
    }

    /**
     * 측정소명을 파라미터로 해당 측정소의 대기정보를 요청하는 메소드.
     * */

    @Override
    public void getAirConditionData(Context context, String stationName) {

        // Checking InternetConnection
        if(CheckConnectivity.checkNetworkConnection(context)) {
            ApiService apiService = RetrofitClient.getApiService();

            Map<String, String> queryParams = RetrofitClient.setQueryParamsForStationName(stationName);

            Log.v(TAG, "Check URL : " + apiService.getAirConditionData(queryParams).request().url().toString());
            final Call<AirConditionList> requestForAirConditionData = apiService.getAirConditionData(queryParams);

            requestForAirConditionData.enqueue(new Callback<AirConditionList>() {
                @Override
                public void onResponse(Call<AirConditionList> call, Response<AirConditionList> response) {
                    if(response.isSuccessful()) {
                        ArrayList<AirCondition> airConditionList = response.body().getList();
                        if(airConditionList.size() > 0)
                            /*
                            Log.v(TAG, "Check Response Data : "
                                    + "\n Pm10Val : "+ airConditionList.get(0).getPm10Value()
                                    + "\n Pm25Val : "+ airConditionList.get(0).getPm25Value()
                                    + "\n KhaiVal : "+ airConditionList.get(0).getKhaiValue()
                            );
                            */

                        // response 받은것을 view에 업데이트 지시.
                        Log.i(TAG, "ORDER to view : updateAirConditionData");
                        view.updateAirConditionData(airConditionList);
                    }
                }

                @Override
                public void onFailure(Call<AirConditionList> call, Throwable t) {
                    Log.v(TAG, "Fail to get data from server");
                    view.showToastMessage("Fail to get data from server");
                }
            });
        }
        else {
            view.showToastMessage("Internet Connection is not available now.");
        }

    }

    @Override
    public void onSampleButtonClicked() {
        // ... Business Logic works
        Log.i(TAG, "Working on some Business Logic when the button is clicked.");

        // update UI with the results.
        String msg = "수행결과값 : 버튼이 클릭 되었습니다.";

        view.showToastMessage(msg);
    }


}
