package com.phics23.owner.ui.tenantBookingDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.data.model.booking.Payment
import com.phics23.owner.databinding.ItemPaymentdueTenantDetailsBinding


class PreviousPaymentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding : ItemPaymentdueTenantDetailsBinding
    var prevPayments = listOf<Payment>()

    class  PaymentViewHolder(binding : ItemPaymentdueTenantDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding  = ItemPaymentdueTenantDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.payment = prevPayments[position]
    }

    override fun getItemCount(): Int {
        return prevPayments.size
    }

    public fun setPreviousPayments(payments: List<Payment>)
    {
        prevPayments = payments.sortedBy { payment -> payment.pcStartDate }
        //prevPayments.sortBy {  }
        notifyDataSetChanged()
    }
}