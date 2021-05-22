package com.phics23.owner.ui.myListingManagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.databinding.FragmentMylistingManagementBinding
import com.phics23.owner.ui.viewModels.MyListingManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.phics23.owner.util.Result

@AndroidEntryPoint
class MyListingManagementFragment : Fragment(){

    lateinit var binding: FragmentMylistingManagementBinding
    private val viewModel: MyListingManagementViewModel by viewModels()
    private lateinit var listing: Listing
    lateinit var tenants : List<Tenant>

    private val TAG = "MyListingManagementFrag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMylistingManagementBinding.inflate(inflater, container, false)

        listing = MyListingManagementFragmentArgs.fromBundle(requireArguments()).listing
        binding.listing=listing
        Log.e(TAG, "onCreateView: $listing", )
        viewModel.getTenants(listing.id)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TenantsRecyclerAdapter()
        viewModel.tenants.observe(viewLifecycleOwner){ tenants->
            when (tenants) {
                is Result.Success -> {
                    this.tenants = tenants.data
                    binding.mainContentView.visibility = View.VISIBLE
                    binding.progressbar.visibility = View.GONE
                    if (tenants.data.size<1)
                    {
                        binding.viewNoTenants.visibility= View.VISIBLE
                        binding.tenantsCardView.visibility = View.GONE

                    }else
                    {
                        adapter.setTenants(tenants.data)
                        binding.tenantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.tenantsRecyclerView.adapter = adapter
                        binding.viewNoTenants.visibility= View.GONE
                        binding.tenantsCardView.visibility = View.VISIBLE
                    }


                }
                is Result.Failure -> {
                    binding.mainContentView.visibility = View.GONE
                    binding.progressbar.visibility = View.GONE
                    binding.error.root.visibility = View.VISIBLE
                    createSnackBar(tenants.message)
                }
                is  Result.Loading ->{
                    binding.mainContentView.visibility = View.GONE
                    binding.progressbar.visibility = View.VISIBLE
                }
            }
        }



        adapter.clickedTenant.observe(viewLifecycleOwner){tenant->
            val action  = MyListingManagementFragmentDirections.actionMyListingManagementFragmentToTenantDetailFragment(tenant)
            findNavController().navigate(action)
        }

        adapter.clickedContactTenant.observe(viewLifecycleOwner){ number->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)

        }


        binding.buttonDelete.setOnClickListener {
            if (tenants.size>0)
            {
                createSnackBar("You have active tenants in this property please request tenants to checkout before deleting this listing")
            }else
            {
                viewModel.deleteListing(listing)
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner)
        {
            createSnackBar(it)
        }



        binding.bookedListingCardviewMyBookingFragment.root.setOnClickListener {

            val action = MyListingManagementFragmentDirections.actionMyListingManagementFragmentToMyListingDetail(listing)
            findNavController().navigate(action)
        }

        viewModel.error.observe(viewLifecycleOwner){ error->

            createSnackBar(error)

        }
    }

    fun createSnackBar( message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }

}