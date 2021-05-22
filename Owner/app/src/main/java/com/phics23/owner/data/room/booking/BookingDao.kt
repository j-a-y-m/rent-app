package com.phics23.tenant.data.room.booking


import androidx.room.*
import androidx.room.Transaction
import com.phics23.tenant.data.model.payments.*


@Dao
interface BookingDao {


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBooking(booking : Booking)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTransaction(transaction : com.phics23.tenant.data.model.payments.Transaction)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTransactions(transactions : List<com.phics23.tenant.data.model.payments.Transaction>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPayment(payment: Payment)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPayments(payments: List<Payment>)

        @Update
        suspend fun updatePayment(payment: Payment)

        @Update
        suspend fun updatePayments(payments: List<Payment>)


        @Query("select * from Payment where paymentId=:paymentId")
        suspend fun getPayment(paymentId : String) : Payment

        @Query("select EXISTS(select * from Booking where isActive)")
        suspend fun hasBooking() : Boolean

        @Query("select * from Booking where isActive")
        suspend fun getCurrentBooking() : Booking

        @Query("select * from Booking where bookingId=:bookingId")
        suspend fun getBooking(bookingId : String) : Booking

        @Query("select * from Payment where bookingId=:bookingId AND isPaid=1 ORDER BY paidDate DESC")
        suspend fun getPreviousPayments(bookingId : String) : List<Payment>

        @Query("select * from Payment where bookingId=:bookingId AND isPaid=0 ORDER BY pcEndDate DESC")
        suspend fun getDuePayments(bookingId : String) : List<Payment>

        @Transaction
        @Query("select * from Booking where isActive=1")
        suspend fun CurrentBookingWithPayments() : List<BookingWithPayments>

        @Transaction
        @Query("select * from Payment where bookingId=(select bookingId from Booking where isActive=1)")
        suspend fun getPaymentsWithTransactions() : List<PaymentWithTransaction>

        @Transaction
        @Query("select * from Booking where bookingId=:bookingId")
        suspend fun getBookingWithPayments(bookingId : String) : List<BookingWithPayments>

        @Transaction
        @Query("select * from Payment where bookingId=:bookingId")
        suspend fun getPaymentsWithTransactions(bookingId: String) : List<PaymentWithTransaction>

        @Query("select * from Payment where bookingId=:bookingId")
        suspend fun getPayments(bookingId : String) : List<Payment>


//        @Query("select * from BookingWithTransactions where bookingId=:bookingId")
//        suspend fun getTran(bookingId : String) : BookingWithTransactions

//        @Query("select * from `Transaction` where bookingId= :bookingId ")
//        suspend fun getTransactions(bookingId : String) : List<Transaction>

//        @Insert
//        suspend fun insertMovies(movies : List<Movie>)
//
//        @Query("select * from Movie")
//        suspend fun getMovies() : List<Movie>
//
//        @Query("select COUNT(*) from Movie")
//        suspend fun movieCount() : Int
//
//        @Query("select * from Movie where ID = :id")
//        suspend fun getMovie(id : Int) : Movie


//    @Query("SELECT * from user WHERE region IN (:regions)")
//    suspend fun loadUsersByRegion(regions: List<String>): List<User>

}