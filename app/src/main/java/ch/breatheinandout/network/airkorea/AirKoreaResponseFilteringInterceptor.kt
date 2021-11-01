package ch.breatheinandout.network.airkorea

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import kotlin.jvm.Throws

class AirKoreaResponseFilteringInterceptor: Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Logger.i("originalReq -> $request")

        return try {
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                throw IOException("No successful response found.")
            }

            val rawJsonStr: String = response.body?.string() ?: ""
            val newBody = removeRedundantElements(rawJsonStr)

            response.newBuilder()
                .message(response.message)
                .body(newBody.toResponseBody())
                .build()
        } catch (e: IOException) {
            throw IOException(e.message)
        }
    }

    private fun removeRedundantElements(input: String): String {
        if (input.length <= 1)
            throw IllegalArgumentException("Input Argument is wrong.")

        try {
            Logger.i("check input -> $input")
            if (input.startsWith("<")) {
                Logger.i("** [Warning] The response is XML format.")
                throw IOException("The response is XML format.")
            }

            val gson =gsonInstance(false)
            val typeToken = object: TypeToken<AirKoreaResponse<*>>() {}.type

            val result: AirKoreaResponse<*> = gson.fromJson(input, typeToken)
            val header = result.response.header

            if (header.resultCode != "00") {
                throw IOException("body content is not available now")
            }

            val body = result.response.body
            val onlyContents = gson.toJson(body.items)
            Logger.i("check newBody -> $onlyContents")
            return onlyContents
        } catch (e: JsonSyntaxException) {
            // When AirKorea server returns a XML format even though request was the JSON type.
            throw IOException("Failed to parse JSON response, ${e.message}")
        }
    }

    private fun gsonInstance(pretty: Boolean = false): Gson {
        if (pretty)
            return Gson().newBuilder().setPrettyPrinting().create()
        return Gson()
    }
}