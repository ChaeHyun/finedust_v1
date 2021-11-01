package ch.breatheinandout.network.airkorea

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** This is a general format when AirKorea server response. */
data class AirKoreaResponse<T>(
    @SerializedName("response")
    @Expose
    val response: Response<T>
)

data class Response<T>(
    @SerializedName("header")
    @Expose
    val header: Header,
    @SerializedName("body")
    @Expose
    val body: Body<T>
)

data class Body<T>(
    @SerializedName("items")
    @Expose
    val items: List<T>,
    @SerializedName("totalCount")
    @Expose
    val totalCount: String,
    @SerializedName("pageNo")
    @Expose
    val pageNo: String,
    @SerializedName("numOfRows")
    @Expose
    val numOfRows: String

)

data class Header(
    @SerializedName("resultMsg")
    @Expose
    val resultMsg: String,
    @SerializedName("resultCode")
    @Expose
    val resultCode: String
)