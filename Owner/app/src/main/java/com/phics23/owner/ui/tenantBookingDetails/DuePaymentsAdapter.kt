package com.phics23.owner.ui.tenantBookingDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.data.model.booking.Payment
import com.phics23.owner.databinding.ItemPaymentdueTenantDetailsBinding

class DuePaymentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding : ItemPaymentdueTenantDetailsBinding
    val duePayments = mutableListOf<Payment>()

    class  PaymentViewHolder(binding : ItemPaymentdueTenantDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding  = ItemPaymentdueTenantDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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