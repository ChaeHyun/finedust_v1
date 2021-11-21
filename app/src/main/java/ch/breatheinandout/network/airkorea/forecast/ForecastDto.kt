package ch.breatheinandout.network.airkorea.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @Expose
    @SerializedName("dataTime")
    val dataTime: String,
    @Expose
    @SerializedName("informData")
    val informData: String,
    @Expose
    @SerializedName("informCode")
    val informCode: String,
    @Expose
    @SerializedName("informGrade")
    val informGrade: String,
    @Expose
    @SerializedName("informCause")
    val informCause: String,
    @Expose
    @SerializedName("informOverall")
    val informOverall: String,
    @Expose
    @SerializedName("imageUrl7")
    val imageUrlPm10: String,
    @Expose
    @SerializedName("imageUrl8")
    val imageUrlPm25: String,
    @Expose
    @SerializedName("imageUrl9")
    val imageUrlO3: String
)