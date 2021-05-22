package com.phics23.owner.ui.myListingManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.data.model.Tenant

import com.phics23.owner.databinding.ItemMylistingmanagementTenantlistingcardBinding


class TenantsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var binding : ItemMylistingmanagementTenantlistingcardBinding
    var tenantsList = listOf<Tenant>()

    private val _tenantListSize: MutableLiveData<Int> = MutableLiveData<Int>()
    public val tenantListSize: LiveData<Int> by this::_tenantListSize

    private val _clickedTenant: MutableLiveData<Tenant> = MutableLiveData<Tenant>()
    public val clickedTenant: LiveData<Tenant> by this::_clickedTenant

    private val _clickedContactTenant: MutableLiveData<String> = MutableLiveData<String>()
    public val clickedContactTenant: LiveData<String> by this::_clickedContactTenant

    class viewHolder(binding: ItemMylistingmanagementTenantlistingcardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemMylistingmanagementTenantlistingcardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding.tenant=tenantsList[position]
        binding.root.setOnClickListener { listingItemView->
            _clickedTenant.postValue(tenantsList[position])
        }
        binding.contactTenantButton.setOnClickListener {
            _clickedContactTenant.postValue(tenantsList[position].phoneNumber)
        }
    }

    override fun getItemCount(): Int {
        _tenantListSize.postValue(tenantsList.size)
        return tenantsList.size
    }

    fun setTenants(tenants: List<Tenant>)
    {
        tenantsList = tenants
        notifyDataSetChanged()
    }
}



