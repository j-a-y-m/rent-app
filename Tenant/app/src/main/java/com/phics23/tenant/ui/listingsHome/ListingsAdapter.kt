package com.phics23.tenant.ui.listingsHome

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.databinding.ItemListingListingsHomeBinding
import com.phics23.tenant.ui.viewModels.ListingsHomeViewModel

class ListingsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    lateinit var binding : ItemListingListingsHomeBinding
    private var listingItems = listOf<Listing>()

    private val _listingListSize: MutableLiveData<Int> = MutableLiveData<Int>()
    public val listingResultListSize: LiveData<Int> by this::_listingListSize

    private val _clickedListing: MutableLiveData<Listing> = MutableLiveData<Listing>()
    public val clickedListing: LiveData<Listing> by this::_clickedListing

    class  ListingViewHolder(binding : ItemListingListingsHomeBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemListingListingsHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.listing = listingItems[position]

        binding.root.setOnClickListener { listingItemView->
            _clickedListing.postValue(listingItems[position])
        }
    }

    override fun getItemCount(): Int {
        return listingItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun setListings(listingItems: List<Listing>)
    {
        this.listingItems = listingItems
        notifyDataSetChanged()
        notifyDataSetChanged()
    }
}