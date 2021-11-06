package ch.breatheinandout.network.airkorea.airquality

import ch.breatheinandout.domain.airquality.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AirQualityDto(
    @SerializedName("dataTime")
    @Expose
    val dataTime: String,
    @SerializedName("mangName")
    @Expose
    val mangName: String,


    @SerializedName("khaiGrade")
    @Expose
    val khaiGrade: String?,
    @SerializedName("khaiValue")
    @Expose
    val khaiValue: String,
    @SerializedName("pm10Grade")
    @Expose
    val pm10Grade: String?,
    @SerializedName("pm10Value")
    @Expose
    val pm10Value: String,
    @SerializedName("pm25Grade")
    @Expose
    val pm25Grade: String?,
    @SerializedName("pm25Value")
    @Expose
    val pm25Value: String,
    @SerializedName("o3Grade")
    @Expose
    val o3Grade: String?,
    @SerializedName("o3Value")
    @Expose
    val o3Value: String,
    @SerializedName("no2Grade")
    @Expose
    val no2Grade: String?,
    @SerializedName("no2Value")
    @Expose
    val no2Value: String,
    @SerializedName("coGrade")
    @Expose
    val coGrade: String?,
    @SerializedName("coValue")
    @Expose
    val coValue: String,
    @SerializedName("so2Grade")
    @Expose
    val so2Grade: String?,
    @SerializedName("so2Value")
    @Expose
    val so2Value: String,

    @SerializedName("pm10Flag")
    @Expose
    val pm10Flag: String?,
    @SerializedName("pm25Flag")
    @Expose
    val pm25Flag: String?,
    @SerializedName("o3Flag")
    @Expose
    val o3Flag: String?,
    @SerializedName("no2Flag")
    @Expose
    val no2Flag: String?,
    @SerializedName("coFlag")
    @Expose
    val coFlag: String?,
    @SerializedName("so2Flag")
    @Expose
    val so2Flag: String?,

    @SerializedName("pm10Grade1h")
    @Expose
    val pm10Grade1h: String?,
    @SerializedName("pm10Value24")
    @Expose
    val pm10Value24: String,
    @SerializedName("pm25Grade1h")
    @Expose
    val pm25Grade1h: String?,
    @SerializedName("pm25Value24")
    @Expose
    val pm25Value24: String
) {
    fun mapToDomain() : AirQuality {
        val details = HashMap<AirQualityType, AirQualityDetail>()

        details.apply {
            put(PM10, AirQualityDetail(
                pm10Flag ?: "-",
                pm10Grade ?: "-",
                pm10Value,
                pm10Value24,
                pm10Grade1h ?: "-"
            ))

            put(
                PM25, AirQualityDetail(
                pm25Flag ?: "-",
                pm25Grade ?: "-",
                pm25Value,
                pm25Value24,
                pm25Grade1h ?: "-"
            ))

            put(O3, AirQualityDetail(o3Flag ?: "-", o3Grade ?: "-", o3Value))
            put(CO, AirQualityDetail(coFlag ?: "-", coGrade ?: "-", coValue))
            put(NO2, AirQualityDetail(no2Flag ?: "-", no2Grade ?: "-", no2Value))
            put(SO2, AirQualityDetail(so2Flag ?: "-", so2Grade ?: "-", so2Value))
        }

        return AirQuality(dataTime, khaiGrade ?: "-", khaiValue, details)
    }
}
