package ch.breatheinandout.screen.forecast

import ch.breatheinandout.domain.forecast.*
import javax.inject.Inject


/** It maps List<Forecast> into ForecastInfoGroup
 * List<Forecast> contains all pm10, pm25, o3 combined in its list.
 * So it divide items by it's informCode and group them separately.
 * */
class ForecastInfoGroupMapper @Inject constructor() {

    fun toGroup(forecastList: List<Forecast>) : ForecastInfoGroup {
        // Group by it's inform code.
        val pm10 = mutableListOf<Forecast>()
        val pm25 = mutableListOf<Forecast>()
        val o3 = mutableListOf<Forecast>()
        val forecastGroupBy: List<List<Forecast>> = listOf(pm10, pm25, o3)
        val infoList = arrayOfNulls<ForecastInfo>(3)    // Array with initial size
        forecastList.forEach {
            when (it.informCode) {
                PM10.code -> pm10.add(it)
                PM25.code -> pm25.add(it)
                O3.code -> o3.add(it)
            }
        }

        forecastGroupBy.mapIndexed { index, list ->
            if (list.size >= 2) {
                infoList[index] = mapToForecastInfo(list)
            }
        }

        return ForecastInfoGroup(infoList[0], infoList[1], infoList[2])
    }

    private fun mapToForecastInfo(list: List<Forecast>): ForecastInfo {
        if (list.size < 2)
            throw IllegalArgumentException()

        // informGrade -> Map<sidoName, sidoGrade>
        val grade = list[0].informGrade.split(",")
        val pair = mutableMapOf<String, String>()
        grade.map {
            val temp = it.split(":")
            val key = temp[0].trim()
            val data = temp[1].trim()
            pair[key] = data
        }

        return ForecastInfo(
            list[0].dataTime, list[0].informCode,
            list[0].informData, list[0].informCause,
            list[0].informOverall, list[1].informOverall,
            list[0].imageUrl
        ).apply { grades.putAll(pair) }
    }
}