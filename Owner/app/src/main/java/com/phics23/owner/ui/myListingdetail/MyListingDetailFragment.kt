package com.phics23.owner.ui.myListingdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.databinding.FragmentMyListingDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyListingDetailFragment : Fragment() {

    lateinit var binding : FragmentMyListingDetailBinding

    private lateinit var listing : Listing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyListingDetailBinding.inflate(inflater, container, false)
        listing = MyListingDetailFragmentArgs.fromBundle(requireArguments()).listing
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listing=listing
        val imageAdapter = ImagesViewpagerAdapter()
        imageAdapter.setImageUrls(listing.listingImages)
        binding.listingDetailImageViewPager.adapter=imageAdapter
        binding.listingDetailImageViewPagerDots.setViewPager2(binding.listingDetailImageViewPager)
    }
}