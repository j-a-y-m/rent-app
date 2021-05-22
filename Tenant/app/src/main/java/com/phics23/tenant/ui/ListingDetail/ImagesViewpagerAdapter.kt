package com.phics23.tenant.ui.ListingDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.databinding.ItemListingDetailImageBinding
import com.phics23.tenant.databinding.ItemListingListingsHomeBinding
import com.squareup.picasso.Picasso

class ImagesViewpagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()    {

    lateinit var binding : ItemListingDetailImageBinding
    private var imageUrls = listOf<String>()

//    private val _listingListSize: MutableLiveData<Int> = MutableLiveData<Int>()
//    public val listingResultListSize: LiveData<Int> by this::_listingListSize
//
//    private val _clickedListing: MutableLiveData<Listing> = MutableLiveData<Listing>()
//    public val clickedListing: LiveData<Listing> by this::_clickedListing

    class  ListingViewHolder(binding : ItemListingDetailImageBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemListingDetailImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Picasso.get().load(imageUrls[position]).into(binding.itemListingdetailImageview)
//        binding.root.setOnClickListener { listingItemView->
//            _clickedListing.postValue(listings[position])
//        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }



    public fun setImageUrls(items: List<String>)
    {
        imageUrls=items
        notifyDataSetChanged()
    }
}