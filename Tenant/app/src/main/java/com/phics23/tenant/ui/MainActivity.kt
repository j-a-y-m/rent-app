package com.phics23.tenant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.phics23.tenant.R
import com.phics23.tenant.data.repository.PaymentRepository
import com.phics23.tenant.data.service.booking.DbTest
import com.phics23.tenant.databinding.ActivityMainBinding
import com.phics23.tenant.ui.viewModels.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: PaymentViewModel by viewModels()
    @Inject lateinit var paymentRepository: PaymentRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.IO)
        {
            paymentRepository.updateCurrentPayments()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = binding.mainNavHostFragmentContainer.findNavController()
        binding.bottomNavBar.setupWithNavController(navController)

    }
}