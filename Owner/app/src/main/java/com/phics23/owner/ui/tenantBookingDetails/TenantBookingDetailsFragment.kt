package com.phics23.owner.ui.tenantBookingDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.phics23.owner.data.model.booking.Payment
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.model.owner.Owner
import com.phics23.owner.databinding.FragmentTenantDetailsBinding
import com.phics23.owner.ui.viewModels.TenantBookingDetailsViewModel

import dagger.hilt.android.AndroidEntryPoint
import com.phics23.owner.ui.tenantBookingDetails.DuePaymentsAdapter
import com.phics23.owner.ui.tenantBookingDetails.PreviousPaymentsAdapter
import com.phics23.owner.ui.viewModels.TenantDetailsSharedViewModel

@AndroidEntryPoint
class TenantBookingDetailsFragment : Fragment() {

    lateinit var binding: FragmentTenantDetailsBinding
    private val viewModel: TenantBookingDetailsViewModel by viewModels()
//    private val sharedViewModel = ViewModelProvider(requireActivity()).get(TenantDetailsSharedViewModel::class.java)
    //private lateinit var listing: Listing
    private lateinit var tenant: Tenant
    private var payment: Payment? = null
    private val TAG = "TenantBookingDetailsFragment"
    private lateinit var duePayments : List<Payment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tenant = TenantBookingDetailsFragmentArgs.fromBundle(requireArguments()).tenant

        binding = FragmentTenantDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(TenantDetailsSharedViewModel::class.java)
        binding.tenant = tenant
        viewModel.getBookingDetails(tenant)



        viewModel.error.observe(viewLifecycleOwner){ error -> createSnackBar(error)}


        viewModel.duePayments.observe(viewLifecycleOwner){ dues ->

                duePayments = dues
                val duePaymentsAdapter = DuePaymentsAdapter()
                duePaymentsAdapter.setDuePayments(dues)
                binding.paymentsdueViewTenantDetailsFragment.paymentsDueRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                binding.paymentsdueViewTenantDetailsFragment.paymentsDueRecyclerView.adapter = duePaymentsAdapter


        }

        viewModel.previousPayments.observe(viewLifecycleOwner){ previouspayments ->
            val previousPaymentsAdapter = PreviousPaymentsAdapter()
            previousPaymentsAdapter.setPreviousPayments(previouspayments)
            binding.progressbar.visibility = View.GONE
            binding.mainContentView.visibility = View.VISIBLE
            binding.previouspaymentsViewTenantDetailsFragment.previousPaymentsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
            binding.previouspaymentsViewTenantDetailsFragment.previousPaymentsRecyclerView.adapter = previousPaymentsAdapter
        }

        binding.contactTenantButtonTenantDetailsFragment.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${tenant.phoneNumber}")
            startActivity(intent)
        }

//        binding.payRentButtonMyBookingFragment.setOnClickListener {
//            val action = MyBookingFragmentDirections.actionMyBookingFragmentToPaymentFragment(listing,duePayments.last())
//            findNavController().navigate(action)
//        }
//
//        binding.bookedListingCardviewMyBookingFragment.root.setOnClickListener {
//            val action = MyBookingFragmentDirections.actionMyBookingFragmentToListingDetailFragment(listing)
//            findNavController().navigate(action)
//        }


        binding.checkoutButtonTenantDetailsFragment.setOnClickListener {

            findNavController().navigate(TenantBookingDetailsFragmentDirections.actionTenantBookingDetailsFragmentToDialogCheckoutRequestMessage(tenant.tenantId))

        }

        binding.reportTenantButtonTenantDetailsFragment.setOnClickListener {
            findNavController().navigate(TenantBookingDetailsFragmentDirections.actionTenantBookingDetailsFragmentToReportTenantDialog(tenant.tenantId))
        }


        sharedViewModel.message.observe(viewLifecycleOwner)
        {message->
            viewModel.requestCheckout(tenant.tenantId,message)
        }
        sharedViewModel.reportReason.observe(viewLifecycleOwner)
        {reason->
            createSnackBar("Report submitted")
            viewModel.reportTenant(tenant.tenantId,reason)
        }



    }


    private fun createSnackBar(message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }

}