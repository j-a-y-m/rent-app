package com.phics23.tenant.ui.MyBooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.databinding.ItemPreviousPaymentMybookingBinding

class PreviousPaymentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding : ItemPreviousPaymentMybookingBinding
    var prevPayments = listOf<Payment>()

    class  PaymentViewHolder(binding : ItemPreviousPaymentMybookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding  = ItemPreviousPaymentMybookingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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