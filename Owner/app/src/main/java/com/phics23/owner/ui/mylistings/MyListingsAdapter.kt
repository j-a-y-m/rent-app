package com.phics23.owner.ui.mylistings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.databinding.ItemListingMylistingsBinding
import com.phics23.owner.databinding.ItemMylistingmanagementTenantlistingcardBinding
import com.phics23.owner.databinding.ItemPaymentdueTenantDetailsBinding

class MyListingsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding : ItemListingMylistingsBinding
    var myListings = listOf<Listing>()
    private val _listingListSize: MutableLiveData<Int> = MutableLiveData<Int>()
    public val listingListSize: LiveData<Int> by this::_listingListSize

    private val _clickedListing: MutableLiveData<Listing> = MutableLiveData<Listing>()
    public val clickedListing: LiveData<Listing> by this::_clickedListing

    class viewHolder(binding: ItemListingMylistingsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemListingMylistingsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyListingsAdapter.viewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.listing=myListings[position]
        binding.root.setOnClickListener { listingItemView->
            _clickedListing.postValue(myListings[position])
        }
    }

    override fun getItemCount(): Int {
        _listingListSize.postValue(myListings.size)
        return myListings.size
    }

    fun setListings(listinglist : List<Listing>)
    {
        myListings = listinglist
        notifyDataSetChanged()
    }
}