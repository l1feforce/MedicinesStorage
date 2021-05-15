package ru.spbstu.gusev.medicinesstorage.data.network.helpers

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.spbstu.gusev.gpstracker.data.network.wrapper.ErrorResponse
import ru.spbstu.gusev.gpstracker.data.network.wrapper.ResultWrapper
import java.io.IOException
import java.nio.charset.Charset


suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(Dispatchers.IO) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Log.d("test", "safeApiCall: ${throwable.printStackTrace()}\n${throwable}")
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val jacksonAdapter = jacksonObjectMapper()
            jacksonAdapter.readValue<ErrorResponse>(it.readString(Charset.defaultCharset()))
        }
    } catch (exception: Exception) {
        null
    }
}