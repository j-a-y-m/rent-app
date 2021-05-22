package com.phics23.tenant.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phics23.tenant.data.repository.MyListingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.repository.ListingsHomeRepository
import com.phics23.tenant.data.repository.SearchRepository
import com.phics23.tenant.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class ListingsHomeViewModel @Inject constructor(val listingsHomeRepository: ListingsHomeRepository, val searchRepository: SearchRepository) : ViewModel(){

    private val TAG = "ListingsHomeViewModel"

    private val _listingsSearchResult: MutableLiveData<List<Listing>> = MutableLiveData<List<Listing>>()
    public val listingsSearchResult: LiveData<List<Listing>> by this::_listingsSearchResult

    private val _listingsFilterResult: MutableLiveData<List<Listing>> = MutableLiveData<List<Listing>>()
    public val listingsFilterResult: LiveData<List<Listing>> by this::_listingsFilterResult

    private val _citiesResult: MutableLiveData<Result<List<String>>> = MutableLiveData<Result<List<String>>>()
    public val citiesResult: LiveData<Result<List<String>>> by this::_citiesResult

    private val _cities: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    public val cities: LiveData<List<String>> by this::_cities

    public val cityQuery: MutableLiveData<String> = MutableLiveData<String>()

    private var listings : List<Listing>? = null

    private val _listingsSearchResultError: MutableLiveData<Result<List<Listing>>> =MutableLiveData<Result<List<Listing>>>()
    public val listingsSearchResultError: LiveData<Result<List<Listing>>> by this::_listingsSearchResultError

    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    public val error : LiveData<String> by this::_error

    public  var city : String? = null

    public fun searchListings(cityQuery : String)
    {  // _listingsSearchResult.postValue(Result.Loading())
        city = cityQuery
        viewModelScope.launch(Dispatchers.IO) {
            //getCities()
            val result  = listingsHomeRepository.getListings(cityQuery)
            if (result is Result.Success)
            {
                listings = result.data!!
                _listingsSearchResult.postValue(result.data!!)
            }else
                if (result is Result.Failure)
                {
                    _error.postValue(result.message)
                        //_listingsSearchResultError.postValue(result)
                }

        }
    }

    public fun getCities() : Result<List<String>>
    {
        var result : Result<List<String>> = Result.Loading()
        _citiesResult.postValue(Result.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            //cities.postValue(mutableListOf<String>())
            when (val res = searchRepository.getCities()) {
                is Result.Success -> {
                    val originalvalue = _cities.value
                    _cities.postValue(res.data!!)
                    _citiesResult.postValue(Result.Success(res.data))
                    result = Result.Success(res.data)
                }
                is Result.Failure -> {
                    _citiesResult.postValue(Result.Failure("Error retriving search terms"))
                    result = Result.Failure("Error retriving search terms")
                }
            }
        }
        return result
    }

    public fun searchCities(query: String)
    {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun getListings()  {
        if(listings!=null)
        {
            Log.e(TAG, "getListings: "+listings )
            _listingsSearchResult.postValue(listings!!)
        }

    }

    fun filterListings(filter: MutableMap<String, String>) {


            viewModelScope.launch(Dispatchers.IO){

                if (listings != null) {
                    var filteredListings = listings!!
                    val fil = filter.filter {filterItem-> filterItem.value.isNotBlank()  }
                    Log.e(TAG, "filterListings: "+fil )
                    if (fil.containsKey("minArea"))
                    {
                        filteredListings = filteredListings.filter { listing -> listing.area.toInt() > fil["minArea"]!!.toInt() }
                    }
                    if (fil.containsKey("priceMin"))
                    {
                        filteredListings = filteredListings.filter { listing -> listing.price.toInt() > fil["priceMin"]!!.toInt() }
                    }
                    if (fil.containsKey("priceMax"))
                    {
                        filteredListings = filteredListings.filter { listing -> listing.price.toInt() < fil["priceMax"]!!.toInt() }
                    }
                    if (fil.containsKey("maxOccupants"))
                    {
                        filteredListings = filteredListings.filter { listing -> listing.totalOccupants.toInt() < fil["maxOccupants"]!!.toInt() }
                    }
                    if (fil.containsKey("listingBhk"))
                    {
                        filteredListings = filteredListings.filter { listing -> listing.listingBhk.contentEquals(fil["listingBhk"]!!)  }
                    }

                    if (filteredListings.isEmpty())
                    {
                        _error.postValue("No listings match your query")
                    }else
                    if(filteredListings==listings)
                    {
                        return@launch
                    }else
                    {
                        listings=filteredListings
                        _listingsSearchResult.postValue(filteredListings)
                    }

                }else
                {
                    _error.postValue("No listings match your query !ngutngtu!")
                }

            }


    }

    fun refreshListings() {
        if(listings!=null)
        {
            Log.e(TAG, "getListings: "+listings )
            _listingsSearchResult.postValue(listings!!)
        }
    }

    fun searchListings() {
        city?.let { searchListings(it) }
    }

    fun setListings(listings: List<Listing>) {
        this.listings = listings
    }


}