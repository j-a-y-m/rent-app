package com.phics23.tenant.data.repository

import com.phics23.tenant.data.service.listingsHome.SearchService
import com.phics23.tenant.util.Result
import javax.inject.Inject

class SearchRepository @Inject constructor(val searchService: SearchService) {

    private val _cities : MutableList<String> = mutableListOf<String>()

    suspend fun getCities(): Result<List<String>> {
        var result: Result<List<String>> = Result.Loading()
        if (_cities.isNotEmpty())
        {
            result = Result.Success(_cities)
        }else
        {
            when(val res = fetchCities())
            {
                    is Result.Success -> {
                        _cities.addAll(res.data)
                        result = Result.Success(_cities.distinct())
                    }
                    is Result.Failure -> {
                        result = Result.Success(_cities)
                    }

            }

        }
        return result
    }

    private suspend fun fetchCities(): Result<List<String>>
    {
        return searchService.getCities()
    }
}