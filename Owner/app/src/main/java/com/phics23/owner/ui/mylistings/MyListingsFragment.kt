package com.phics23.owner.ui.mylistings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phics23.owner.databinding.FragmentMylistingsBinding
import com.phics23.owner.ui.viewModels.MyListingsViewModel
import com.phics23.owner.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyListingsFragment : Fragment() {
    private   val TAG = "MyListingsFragment"
    lateinit var binding : FragmentMylistingsBinding
    val viewModel : MyListingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMylistingsBinding.inflate(inflater,container,false)
        viewModel.getMyListings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myListingsAdapter = MyListingsAdapter()
        binding.myListings.adapter= myListingsAdapter
        binding.myListings.layoutManager=LinearLayoutManager(requireContext())


        myListingsAdapter.listingListSize.observe(viewLifecycleOwner){

            if (it<1)
            {
                binding.nolistings.root.visibility= View.VISIBLE
                binding.myListings.visibility = View.GONE
            }else
            {
                binding.nolistings.root.visibility= View.GONE
                binding.myListings.visibility = View.VISIBLE
            }
        }


        viewModel.myListingsResult.observe(viewLifecycleOwner){ result->
            when(result)
            {
                is Result.Success -> {

                    myListingsAdapter.setListings(result.data)
                    binding.nolistings.root.visibility = View.GONE
                    binding.myListings.visibility = View.VISIBLE
                    binding.progressbar.visibility = View.GONE
                }
                is Result.Failure -> {
                    binding.error.root.visibility = View.VISIBLE
                    binding.myListings.visibility = View.GONE
                    binding.progressbar.visibility = View.GONE
                    binding.nolistings.root.visibility = View.GONE
                    Log.e("mylistingfragment", "onViewCreated: "+result.message )
                }
                is Result.Loading -> {
                    binding.nolistings.root.visibility= View.GONE
                    binding.myListings.visibility = View.GONE
                    binding.progressbar.visibility = View.VISIBLE
                    binding.error.root.visibility = View.GONE
                }
            }
        }

        myListingsAdapter.clickedListing.observe(viewLifecycleOwner) { listing->
            val action = MyListingsFragmentDirections.actionMyListingsFragmentToMyListingManagementFragment(listing)
            findNavController().navigate(action)
        }



    }
}