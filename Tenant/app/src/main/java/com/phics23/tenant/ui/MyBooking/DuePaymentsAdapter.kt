package com.phics23.tenant.ui.MyBooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.databinding.ItemPaymentDueMybookingBinding
import com.phics23.tenant.databinding.ItemSearchResultListingsHomeBinding

class DuePaymentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding : ItemPaymentDueMybookingBinding
    val duePayments = mutableListOf<Payment>()

    class  PaymentViewHolder(binding : ItemPaymentDueMybookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding  = ItemPaymentDueMybookingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.payment = duePayments[position]
    }

    override fun getItemCount(): Int {
        return duePayments.size
    }

    public fun setDuePayments(payments: List<Payment>)
    {
        duePayments.addAll(payments)
        duePayments.sortBy { payment -> payment.pcStartDate }
        notifyDataSetChanged()
    }

}