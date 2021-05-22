package com.phics23.tenant.data.model.tenant


data class NewTenant(val email: String,
                    val password: String,
                    val name: String,
                    val address: String,
                    val phoneNumber: String)
