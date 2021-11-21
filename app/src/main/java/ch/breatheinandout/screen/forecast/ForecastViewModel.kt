package ch.breatheinandout.screen.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.breatheinandout.domain.forecast.ForecastInfoGroup
import ch.breatheinandout.domain.forecast.GetForecastUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {
    val forecastLiveData : MutableLiveData<ForecastInfoGroup> = MutableLiveData()

    fun getForecast(searchDate: String) {
        viewModelScope.launch {
            val result = getForecastUseCase.forecast(searchDate)
            forecastLiveData.value = result
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}