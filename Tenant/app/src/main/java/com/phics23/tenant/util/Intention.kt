package com.phics23.tenant.util

import com.phics23.tenant.data.model.listing.Listing

sealed class Intention<T> {

    class NewBooking(val listing : Listing) : Intention<NewBooking>()
    class makePayment(val listing : Listing) : Intention<makePayment>()
}