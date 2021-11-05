package ch.breatheinandout.screen.searchaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.breatheinandout.searchaddress.SearchAddressUseCase
import ch.breatheinandout.searchaddress.SearchedAddress
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAddressViewModel @Inject constructor(
    private val searchAddressUseCase: SearchAddressUseCase
): ViewModel(){
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val viewState: MutableLiveData<SearchAddressViewState> = MutableLiveData()

    fun search(query: String) {
        viewState.value = Loading
        viewModelScope.launch {
            when (val result = searchAddressUseCase.search(query)) {
                is SearchAddressUseCase.Result.Success -> {
                    Logger.i("result@ViewModel -> $result")
                    viewState.value = SearchAddressContent(result.searchedAddressList)
                }
                is SearchAddressUseCase.Result.Failure -> { viewState.value = Error }
            }
        }
    }

    fun save(item: SearchedAddress) {
        // TODO : Save the item in the database.

    }
}