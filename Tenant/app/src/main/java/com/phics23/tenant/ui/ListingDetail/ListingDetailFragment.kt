package com.phics23.tenant.ui.ListingDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.databinding.FragmentListingDetailBinding
import com.phics23.tenant.ui.viewModels.ListingdetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListingDetailFragment : Fragment() {

    lateinit var binding : FragmentListingDetailBinding
    private val viewModel : ListingdetailViewModel by viewModels()
    private lateinit var listing : Listing
    private lateinit var owner : Owner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListingDetailBinding.inflate(inflater, container, false)
        try {
            listing = ListingDetailFragmentArgs.fromBundle(requireArguments()).listing
            viewModel.setListing(listing)
        }catch (e: Exception)
        {
            listing = viewModel.getListing()
        }

        viewModel.getDetails(listing)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listing=listing
        val imageAdapter = ImagesViewpagerAdapter()
        imageAdapter.setImageUrls(listing.listingImages)
        binding.listingDetailImageViewPager.adapter=imageAdapter
        binding.listingDetailImageViewPagerDots.setViewPager2(binding.listingDetailImageViewPager)

        viewModel.owner.observe(viewLifecycleOwner)
        {
            owner = it
            binding.owner = owner

            binding.listingDetailOwner.buttonCallOwner.setOnClickListener {
                viewModel.contactOwner(owner)
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${owner.phoneNumber}")
                startActivity(intent)

            }
        }

        binding.buttonBookLisitng.setOnClickListener {
            val action = ListingDetailFragmentDirections.actionListingDetailFragmentToPaymentFragment(listing,null)
            findNavController().navigate(action)
        }


        viewModel.rating.observe(viewLifecycleOwner)
        {
            binding.rating = it
        }



    }
}