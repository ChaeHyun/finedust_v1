package ch.breatheinandout.screen.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.breatheinandout.domain.forecast.model.ForecastInfoGroup
import ch.breatheinandout.domain.forecast.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {
    val forecastLiveData : MutableLiveData<ForecastInfoGroup> = MutableLiveData()

    fun getForecast() {
        viewModelScope.launch {
            val result = getForecastUseCase.getForecastFromLocal()
            result?.let { forecastLiveData.value = result }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}