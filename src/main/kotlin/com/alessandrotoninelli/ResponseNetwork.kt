package com.alessandrotoninelli

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

sealed class ResponseNetwork<out S : Any, out E : Any> {

    companion object {

        fun create(error: Throwable): ResponseNetwork<Nothing, Nothing> {
            return when (error) {
                is IOException -> ResponseNetworkIOFailure(error)
                else -> ResponseNetworkUnknownError(-1, error.localizedMessage)
            }
        }

        fun <S : Any, E : Any> create(
            response: Response<S>,
            converter: Converter<ResponseBody, E>
        ): ResponseNetwork<S, E> {

            val body = response.body()
            val code = response.code()
            val error = response.errorBody()

            return if (response.isSuccessful) {
                body?.let { ResponseNetworkSuccess(it) } ?: ResponseNetworkSuccessEmpty(code)
            } else {
                val errorBody = when {
                    error == null -> null
                    error.contentLength() == 0L -> null
                    else -> kotlin.runCatching { converter.convert(error) }.getOrNull()
                }

                errorBody?.let { ResponseNetworkUserError(code, it) } ?: ResponseNetworkHttpError(
                    code,
                    response.raw().message()
                )

            }

        }
    }

}

data class ResponseNetworkSuccess<S : Any>(
    val body: S
) : ResponseNetwork<S, Nothing>()

data class ResponseNetworkSuccessEmpty(val code: Int) : ResponseNetwork<Nothing, Nothing>()

data class ResponseNetworkUserError<E : Any>(
    val code: Int,
    val body: E
) : ResponseNetwork<Nothing, E>()

data class ResponseNetworkHttpError(
    val code: Int,
    val msg: String
) : ResponseNetwork<Nothing, Nothing>()

data class ResponseNetworkIOFailure(val error: Throwable) :
    ResponseNetwork<Nothing, Nothing>()

data class ResponseNetworkUnknownError(val code: Int, val msg: String) :
    ResponseNetwork<Nothing, Nothing>()

