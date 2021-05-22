package com.phics23.tenant.ui.listingsHome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.databinding.ItemListingListingsHomeBinding
import com.phics23.tenant.databinding.ItemSearchResultListingsHomeBinding

class SearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding : ItemSearchResultListingsHomeBinding
    val citiesSearchResult = mutableListOf<String>()
    private val _listingListSize: MutableLiveData<Int> = MutableLiveData<Int>()
    public val citiesSearchResultListSize: LiveData<Int> by this::_listingListSize

    class  CityViewHolder(binding : ItemSearchResultListingsHomeBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemSearchResultListingsHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.city = citiesSearchResult[position]
    }

    override fun getItemCount(): Int {
        return citiesSearchResult.size
    }

    public fun setListings(cities: List<String>)
    {
        citiesSearchResult.addAll(citiesSearchResult)
        notifyDataSetChanged()
    }

}