package com.phics23.tenant.ui.listingsHome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.phics23.tenant.util.Result
import com.phics23.tenant.databinding.FragmentListingsHomeBinding
import com.phics23.tenant.databinding.ItemSearchResultListingsHomeBinding
import com.phics23.tenant.databinding.LayoutLisingsHomeAppbarBinding
import com.phics23.tenant.ui.adapters.CustomArrayAdapter
import com.phics23.tenant.ui.viewModels.ListingsHomeViewModel
import com.phics23.tenant.viewModels.ListingsHomeSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class ListingsHomeFragment : Fragment() {
    private  val TAG = "ListingsHomeFragment"
    lateinit var binding : FragmentListingsHomeBinding
    lateinit var appBarBinding: LayoutLisingsHomeAppbarBinding
    val viewModel : ListingsHomeViewModel by viewModels()
    lateinit var sharedViewModel :  ListingsHomeSharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentListingsHomeBinding.inflate(inflater,container,false)
        //appBarBinding = LayoutLisingsHomeAppbarBinding.inflate(inflater,container,false)
        //viewModel.searchListings("Thane")
        viewModel.getCities()

          //  autoCompleteTextViewSearch.dropDownWidth=binding.include.cardviewSearch.measuredWidth
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            sharedViewModel = ViewModelProvider(requireActivity()).get(ListingsHomeSharedViewModel::class.java)
//        sharedViewModel = ViewModelProvider(this).get(ListingsHomeSharedViewModel::class.java)




        val searchitembinding = ItemSearchResultListingsHomeBinding.inflate(layoutInflater)
        val listingsAdapter = ListingsAdapter()
        viewModel.getListings()
        binding.listingsResult.adapter = listingsAdapter
        binding.listingsResult.layoutManager= LinearLayoutManager(requireContext())

        viewModel.citiesResult.observe(viewLifecycleOwner)
        {result->
            when (result) {
                is Result.Success -> {
                    //viewModel.getlistings()
                    val cities = viewModel.cities.value
                    Log.e("fragment", "onViewCreated: " + cities.toString())
                    val searchAdapter = CustomArrayAdapter(requireContext(), android.R.layout.simple_list_item_activated_1, cities!!)
                    binding.autoCompleteTextViewSearch.setAdapter(searchAdapter)
                    binding.autoCompleteTextViewSearch.setOnItemClickListener { parent, view , position, id ->
                        binding.noListingsView.visibility=View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        val tv = view as TextView
                        viewModel.searchListings(tv.text.toString())
                        //sharedViewModel.setFilter(mutableMapOf())
                    }
                }
                is Result.Failure -> {
                    createSnackBar(result.message+" Search Unavailable")
                }
            }
        }


        viewModel.listingsSearchResult.observe(viewLifecycleOwner)
        { listings->
            Log.e("listingsearchresult", "onViewCreated: "+listings )
            val searchListingsAdapter = ListingsAdapter()
            binding.listingsResult.adapter = searchListingsAdapter
            binding.listingsResult.layoutManager= LinearLayoutManager(requireContext())
            searchListingsAdapter.setListings(listings)
            viewModel.setListings(listings)
            //sharedViewModel.setFilter(mutableMapOf())
            searchListingsAdapter.clickedListing.observe(viewLifecycleOwner) { listing->
                val action = ListingsHomeFragmentDirections.actionListingsHomeFragmentToListingDetailFragment(listing)
                try {
                    findNavController().navigate(action)
                }catch (e : Exception)
                {
                    Log.e(TAG, "onViewCreated:listingsAdapter.clickedListing ",e )
                }

            }
                    binding.listingsSwipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.noListingsView.visibility = View.GONE
                    binding.listingsResult.visibility = View.VISIBLE
        }

        viewModel.listingsSearchResultError.observe(viewLifecycleOwner)
        {result->
            if(result is Result.Failure)
            {
                createSnackBar(result.message)
            }

        }


//        listingsAdapter.clickedListing.observe(viewLifecycleOwner) { listing->
//            val action = ListingsHomeFragmentDirections.actionListingsHomeFragmentToListingDetailFragment(listing)
//            findNavController().navigate(action)
//        }

        binding.listingsHomeFilterButton.setOnClickListener {

            findNavController().navigate(ListingsHomeFragmentDirections.actionListingsHomeFragmentToListingsHomeFilter())
            sharedViewModel.filter.observe(viewLifecycleOwner)
            {   filter->
                Log.e(TAG, "onViewCreated:sharedViewModel.filter FILTERvm ", )
                viewModel.filterListings(filter)
            }
//            val filterBottomSheet = ListingFilterListDialogFragment()
//            filterBottomSheet.show(parentFragmentManager,"FilterBottomSheet")

        }






        binding.listingsSwipeRefresh.setOnRefreshListener {
            viewModel.searchListings()
           // sharedViewModel.setFilter(mutableMapOf())
        }



//        viewModel.listingsFilterResult.observe(viewLifecycleOwner)
//        { listings->
//            Log.e("listingFilterresult", "onViewCreated: "+listings )
//            val filteredListingsAdapter = ListingsAdapter()
//            //viewModel.getlistings()
//            binding.listingsResult.adapter = filteredListingsAdapter
//            filteredListingsAdapter.setListings(listings)
//            //listingsAdapter.setListings(listings)
//            binding.progressBar.visibility = View.GONE
//            binding.listingsResult.visibility = View.VISIBLE
//            filteredListingsAdapter.clickedListing.observe(viewLifecycleOwner) { listing->
//                val action = ListingsHomeFragmentDirections.actionListingsHomeFragmentToListingDetailFragment(listing)
//                try {
//                    findNavController().navigate(action)
//                }catch (e : Exception)
//                {
//                    Log.e(TAG, "onViewCreated:filteredListingsAdapter.clickedListing ",e )
//                }
//
//            }
//        }

        viewModel.error.observe(viewLifecycleOwner){ error->
            binding.progressBar.visibility = View.GONE
            createSnackBar(error)
        }

    }

//    override fun onStop() {
//        sharedViewModel?.let { it.filter.removeObservers(viewLifecycleOwner) }
//        super.onStop()
//    }


    fun createSnackBar( message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }
}