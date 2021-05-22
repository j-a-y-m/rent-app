package com.phics23.tenant.ui.MyBooking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.databinding.FragmentMybookingBinding
import com.phics23.tenant.viewModels.MyBookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.phics23.tenant.util.Result

@AndroidEntryPoint
class MyBookingFragment : Fragment() {

    lateinit var binding: FragmentMybookingBinding
    private val viewModel: MyBookingViewModel by viewModels()

    private lateinit var listing: Listing
    private lateinit var owner: Owner
    private var payment: Payment? = null
    private val TAG = "MyBookingFragment"
    private lateinit var duePayments : List<Payment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBookingDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMybookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hasBooking.observe(viewLifecycleOwner){ hasBooking ->
                if (hasBooking)
                {
                    binding.viewNoBooking.visibility = View.GONE
                    binding.myBookingProgressbar.visibility = View.VISIBLE
                }else
                {
                    binding.viewNoBooking.visibility = View.VISIBLE
                    binding.myBookingProgressbar.visibility = View.GONE
                }
        }

        viewModel.error.observe(viewLifecycleOwner){ error -> createSnackBar(error)}

        viewModel.listing.observe(viewLifecycleOwner){listing->
            binding.listing = listing
            this.listing=listing
            binding.myBookingProgressbar.visibility = View.GONE
            binding.viewContentMain.visibility = View.VISIBLE
        }


        viewModel.owner.observe(viewLifecycleOwner){owner-> this.owner = owner}

        viewModel.duePayments.observe(viewLifecycleOwner){ dues ->

                duePayments = dues as List<Payment>
                val duePaymentsAdapter = DuePaymentsAdapter()
                duePaymentsAdapter.setDuePayments(dues)
                binding.paymentsdueViewMybookingFragment.recyclerViewDuePayments.layoutManager=LinearLayoutManager(requireContext())
                binding.paymentsdueViewMybookingFragment.recyclerViewDuePayments.adapter = duePaymentsAdapter


        }

        viewModel.previousPayments.observe(viewLifecycleOwner){ previouspayments ->
            Log.e(TAG, "onViewCreated: "+previouspayments.joinToString(",,") )
            val previousPaymentsAdapter = PreviousPaymentsAdapter()
            previousPaymentsAdapter.setPreviousPayments(previouspayments)
            binding.previouspaymentsViewMybookingFragment.recyclerViewPreviousPayments.layoutManager=LinearLayoutManager(requireContext())
            binding.previouspaymentsViewMybookingFragment.recyclerViewPreviousPayments.adapter = previousPaymentsAdapter
            binding.viewNoBooking.visibility = View.GONE
            binding.myBookingProgressbar.visibility = View.GONE
            binding.viewContentMain.visibility = View.VISIBLE
        }

        binding.contactOwnerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${owner.phoneNumber}")
            startActivity(intent)
        }

        binding.payRentButtonMyBookingFragment.setOnClickListener {
            val action = MyBookingFragmentDirections.actionMyBookingFragmentToPaymentFragment(listing,duePayments.last())
            findNavController().navigate(action)
        }

        binding.cancelBookingButtonMyBookingFragment.setOnClickListener {
            viewModel.cancelBooking()
            binding.myBookingProgressbar.visibility = View.VISIBLE
            binding.viewContentMain.visibility = View.GONE

        }

        binding.bookedListingCardviewMyBookingFragment.root.setOnClickListener {
            val action = MyBookingFragmentDirections.actionMyBookingFragmentToListingDetailFragment(listing)
            findNavController().navigate(action)
        }

        viewModel.cancelBookingRequest.observe(viewLifecycleOwner){result->
            when (result) {
                is Result.Success -> {
                    binding.myBookingProgressbar.visibility = View.GONE
                    createSnackBar(result.data)
                    findNavController().navigate(MyBookingFragmentDirections.actionMyBookingFragmentToListingsHomeFragment())

                }
                is Result.Failure -> {
                    binding.myBookingProgressbar.visibility = View.GONE
                    createSnackBar(result.message)
                }
            }
        }


        binding.addReviewViewMybookingFragment.addReviewButtonLayoutMyBookingAddReview.setOnClickListener {
            viewModel.submitRating(binding.addReviewViewMybookingFragment.ratingBar.rating,
            binding.addReviewViewMybookingFragment.addReviewEditTextLayoutMyBookingAddReview.text.toString(),
            listing)


        }




    }


    private fun createSnackBar(message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }

}