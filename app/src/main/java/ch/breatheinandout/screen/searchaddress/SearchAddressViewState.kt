package ch.breatheinandout.screen.searchaddress

import ch.breatheinandout.domain.searchaddress.SearchedAddress

sealed class SearchAddressViewState

object Loading: SearchAddressViewState()
object Error: SearchAddressViewState()

data class SearchAddressContent(
    val searchedAddressList: List<SearchedAddress>
) : SearchAddressViewState()