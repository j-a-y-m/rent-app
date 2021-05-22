package com.phics23.owner.ui.tenantBookingDetails


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.databinding.DialogCheckoutRequestTenantDetailsBinding
import com.phics23.owner.ui.viewModels.TenantBookingDetailsViewModel
import com.phics23.owner.ui.viewModels.TenantDetailsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckoutRequestDialog : DialogFragment() {
    lateinit var binding : DialogCheckoutRequestTenantDetailsBinding

    val args: CheckoutRequestDialogArgs by navArgs()
    lateinit var tenantId : String
    private  val TAG = "CheckoutRequestDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCheckoutRequestTenantDetailsBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val sharedViewModel = ViewModelProvider(requireActivity()).get(TenantDetailsSharedViewModel::class.java)
        binding.submitButton.setOnClickListener {
            val message = binding.checkoutRequestMessageTextview.text.toString()
            dismiss()
            sharedViewModel.requestCheckout(message)
        }
    }
}