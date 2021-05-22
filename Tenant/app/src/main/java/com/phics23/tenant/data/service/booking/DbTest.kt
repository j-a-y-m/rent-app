package com.phics23.tenant.data.service.booking

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.phics23.tenant.data.model.payments.Booking
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.model.payments.Transaction
import com.phics23.tenant.data.room.booking.BookingDao
import kotlinx.coroutines.channels.consumesAll
import javax.inject.Inject

class DbTest @Inject constructor(val bookingDao : BookingDao, val auth: FirebaseAuth, val firestore: FirebaseFirestore, val functions: FirebaseFunctions) {
    private  val TAG = "DbTest"
    suspend fun getCurrentBooking()
    {
        try{
            if (bookingDao.hasBooking())
            {
                Log.e(TAG, "getCurrentBooking: bookingDao.hasBooking "+true )
                Log.e(TAG, "getCurrentBooking: bookingDao.getCurrentBooking()"+bookingDao.getCurrentBooking() )
            }else
            {
                Log.e(TAG, "getCurrentBooking: no booking" )
            }
        }catch (e:Exception)
        {
            Log.e(TAG, "getCurrentBooking: ",e )
        }
    }


    suspend fun newBooking()
    {
        val booking = Booking("booking1","listing1","owner1",10,true)
        val payment = Payment("payment1",10,20,"5000",false,true,"transaction1",10,"booking1")
        val transaction = Transaction("transaction1",10,"5000",10,20,"payment1","booking1")

        bookingDao.insertBooking(booking)
        bookingDao.insertPayment(payment)
        bookingDao.insertTransaction(transaction)

    }

    suspend fun updatePaymentsList()
    {
        val payment = Payment("payment2",20,30,"5000",false,false,null,null,"booking1")
        bookingDao.insertPayment(payment)
    }

    suspend fun transactionSuccessful()
    {
        val payment  = bookingDao.getPayment("payment2")
        val transaction = Transaction("transaction2",22,"5000",payment.pcStartDate,payment.pcEndDate,"payment2","booking1")
        val paymentUpdate = Payment(payment.paymentId,payment.pcStartDate,payment.pcEndDate,"5000",false,true,transaction.transactionId,transaction.transactionDate,"booking1")
        bookingDao.insertTransaction(transaction)
        bookingDao.updatePayment(paymentUpdate)
    }

    suspend fun getPayments()
    {
        try {
            Log.e(TAG, "getPayments: "+bookingDao.getPaymentsWithTransactions() )
        }catch (e : java.lang.Exception)
        {

            Log.e(TAG, "getPayments: ",e )
        }
    }

    suspend fun addDuePayment(){
        val payment = Payment("payment3",30,40,"5000",false,false,null,null,"booking1")
        bookingDao.insertPayment(payment)
        getPayments()
    }


}