package com.phics23.tenant.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
//import com.phics23.tenant.PaymentFragmentArgs
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.databinding.FragmentPaymentBinding
import com.phics23.tenant.ui.ListingDetail.ImagesViewpagerAdapter
import com.phics23.tenant.ui.adapters.BindingAdapters.Companion.setFromToDate
import com.phics23.tenant.ui.viewModels.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.phics23.tenant.util.Result
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    lateinit var binding: FragmentPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var listing: Listing
    private var payment: Payment? = null
    private val paymentFragmentArgs: PaymentFragmentArgs by navArgs()
    private val TAG = "PaymentFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        try {
            //listing = PaymentFragmentArgs.fromBundle(requireArguments()).listing
            listing = paymentFragmentArgs.listing
            payment = paymentFragmentArgs.payment
            //payment = PaymentFragmentArgs.fromBundle(requireArguments()).
            //viewModel.setListing(listing)
        } catch (e: Exception) {
            //listing = viewModel.getListing()
        }

        //viewModel.getDetails(listing)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.testDb()
        binding.listing = listing

        if (payment == null) {
            createNewBooking()
        }else
        {
            Log.e("TAG", "onCreateView: "+payment.toString() )
            makePayment()
        }

    }


    fun createNewBooking()
    {
        val zoneId = ZoneId.of("Asia/Kolkata")
        val pcStartDate = LocalDate.now(zoneId).atStartOfDay(zoneId)
        Log.e(TAG, "createNewBooking: "+pcStartDate.toString() )
        val pcEndDate = pcStartDate.plusMonths(1)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/LL/yy")
        val FromToDate = pcStartDate.format(formatter)+" to "+pcEndDate.format(formatter)
//        setFromToDate(binding.fromToDate, pcStartDate.toEpochSecond(), pcEndDate.toEpochSecond())
//        binding.fromToDate.text = FromToDate
        binding.payment = Payment("",pcStartDate.toEpochSecond(),pcEndDate.toEpochSecond(),listing.price,false,false,"",0,"")
        binding.loadingIndicator.visibility = View.GONE
        binding.paymentFragmentContent.visibility = View.VISIBLE

            binding.payRentButtonMyBookingFragment.setOnClickListener {
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.paymentFragmentContent.visibility = View.GONE
                viewModel.createNewBooking(listing)

            }
        viewModel.newBooking.observe(viewLifecycleOwner){ result ->
            when (result) {
                is Result.Success -> {
                    binding.paymentFragmentContent.visibility = View.VISIBLE
                    binding.loadingIndicator.visibility = View.GONE
                    createSnackBar(result.data)
                    val action = PaymentFragmentDirections.actionPaymentFragmentToMyBookingFragment()
                    findNavController().navigate(action)
                }
                is Result.Failure -> {
                    createSnackBar(result.message)
                }
            }

        }


    }




    fun makePayment()
    {
        binding.payment = payment

        binding.payRentButtonMyBookingFragment.setOnClickListener {
            binding.loadingIndicator.visibility = View.VISIBLE
            binding.paymentFragmentContent.visibility = View.GONE

            viewModel.makePayment(payment!!)
        }

        viewModel.payment.observe(viewLifecycleOwner){ result ->
            when (result) {
                is Result.Success -> {
                    binding.paymentFragmentContent.visibility = View.VISIBLE
                    binding.loadingIndicator.visibility = View.GONE
                    createSnackBar(result.data)
                    val action = PaymentFragmentDirections.actionPaymentFragmentToMyBookingFragment()
                    findNavController().navigate(action)
                }
                is Result.Failure -> {
                    createSnackBar(result.message)
                }
            }

        }



    }

    fun createSnackBar( message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }
}
