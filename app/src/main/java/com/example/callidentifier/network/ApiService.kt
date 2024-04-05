package com.example.callidentifier.network

import com.example.callidentifier.pojo.NetworkResModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService
{
    @GET("search")
    @Headers("Authorization: Bearer a1i04--lJZ1W--TFsn2i-58VSD5JDoDE3Fh3KngvLCx4946qrrORNebxR7AQmalE")
    fun searchNoDetails(@Query("q") q:String,
                              @Query("countryCode") countryCode:String = "+91",
                              @Query("type") type:Int = 4,) : Call<NetworkResModel>

    //a1i01--lJUtQA-m-pvLnlhiW4_EZTIWXzT1PKHxWnzI2WlcqcAsVkMuJx7AQX1Ou
}