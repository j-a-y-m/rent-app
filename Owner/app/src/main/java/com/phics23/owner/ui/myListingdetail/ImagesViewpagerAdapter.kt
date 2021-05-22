package com.phics23.owner.ui.myListingdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.databinding.ItemListingDetailImageBinding
import com.squareup.picasso.Picasso

class ImagesViewpagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding : ItemListingDetailImageBinding
    private var imageUrls = listOf<String>()



    class  ListingViewHolder(binding : ItemListingDetailImageBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemListingDetailImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        binding.imgUrl=imageUrls[position]
        //Picasso.get().load(imageUrls[position]).into(binding.itemListingdetailImageview)
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