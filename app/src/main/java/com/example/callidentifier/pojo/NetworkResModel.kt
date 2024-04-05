package com.example.callidentifier.pojo

import com.google.gson.annotations.SerializedName

data class NetworkResModel(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("stats")
    val stats: Stats
)

data class Data(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imId")
    val imId: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("access")
    val access: String,
    @SerializedName("enhanced")
    val enhanced: Boolean,
    @SerializedName("phones")
    val phones: List<Phone>,
    @SerializedName("addresses")
    val addresses: List<Address>,
    @SerializedName("internetAddresses")
    val internetAddresses: List<Any>,
    @SerializedName("badges")
    val badges: List<String>,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("cacheTtl")
    val cacheTtl: Int,
    @SerializedName("sources")
    val sources: List<Any>,
    @SerializedName("searchWarnings")
    val searchWarnings: List<Any>,
    @SerializedName("surveys")
    val surveys: List<Survey>,
    @SerializedName("commentsStats")
    val commentsStats: CommentsStats,
    @SerializedName("manualCallerIdPrompt")
    val manualCallerIdPrompt: Boolean,
    @SerializedName("ns")
    val ns: Int
)

data class Phone(
    @SerializedName("e164Format")
    val e164Format: String,
    @SerializedName("numberType")
    val numberType: String,
    @SerializedName("nationalFormat")
    val nationalFormat: String,
    @SerializedName("dialingCode")
    val dialingCode: Int,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("carrier")
    val carrier: String,
    @SerializedName("type")
    val type: String
)

data class Address(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("timeZone")
    val timeZone: String,
    @SerializedName("type")
    val type: String
)

data class Survey(
    @SerializedName("id")
    val id: String,
    @SerializedName("frequency")
    val frequency: Int,
    @SerializedName("passthroughData")
    val passthroughData: String,
    @SerializedName("perNumberCooldown")
    val perNumberCooldown: Int,
    @SerializedName("dynamicContentAccessKey")
    val dynamicContentAccessKey: String
)

data class CommentsStats(
    @SerializedName("showComments")
    val showComments: Boolean
)

data class Stats(
    @SerializedName("sourceStats")
    val sourceStats: List<Any>
)

