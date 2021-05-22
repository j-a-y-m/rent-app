package com.phics23.tenant.ui.listingsHome

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.phics23.tenant.R
import com.phics23.tenant.databinding.FragmentListingsHomeListingsfilterBottomsheetBinding
import com.phics23.tenant.viewModels.ListingsHomeSharedViewModel

class ListingFilterListDialogFragment() : BottomSheetDialogFragment() {
    lateinit var binding: FragmentListingsHomeListingsfilterBottomsheetBinding

    private val TAG = "ListingFilterListDialog"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //dialog?.getWindow()?.getDecorView()?.setBackgroundDrawable(ColorDrawable(Color.CYAN))
        binding = FragmentListingsHomeListingsfilterBottomsheetBinding.inflate(inflater,container,false)
        //setStyle(STYLE_NORMAL,R.style.BottomSheetDialog)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view.rootView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val sharedViewModel = ViewModelProvider(requireActivity()).get(ListingsHomeSharedViewModel::class.java)



        binding.buttonFilter.setOnClickListener {
            val listingBhkId = binding.chipGroupListingBhk.checkedChipId
            val filter = mutableMapOf<String,String>(
                "minArea" to binding.areaTextView.text.toString(),
                "priceMin" to  binding.priceMinTextView.text.toString(),
                "priceMax" to binding.priceMaxTextView.text.toString(),
                "maxOccupants" to binding.occupantsTextView.text.toString(),
                "listingBhk" to if(listingBhkId== View.NO_ID) "" else binding.root.findViewById<Chip>(listingBhkId).text.toString()
            )
            sharedViewModel.setFilter(filter)
            dismiss()

        }




    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}