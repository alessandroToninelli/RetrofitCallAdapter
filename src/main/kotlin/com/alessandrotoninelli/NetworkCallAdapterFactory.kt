package com.alessandrotoninelli


import okhttp3.ResponseBody
import retrofit2.*
import java.lang.UnsupportedOperationException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkCallAdapterFactory private constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ResponseNetwork::class.java -> {
                    require(callType is ParameterizedType) { "resource must be parametrized" }
                    val resultType = getParameterUpperBound(0, callType)
                    val errorType = getParameterUpperBound(1, callType)
                    val errorBodyConverter =
                        retrofit.nextResponseBodyConverter<Any>(null, errorType, annotations)
                    ResponseNetworkAdapter<Any, Any>(resultType, errorBodyConverter)
                }
                else -> null
            }
        }
        else -> null
    }

    companion object {
        @JvmStatic
        fun create() = NetworkCallAdapterFactory()
    }

}

class ResponseNetworkAdapter<Success : Any, Error : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, Error>
) : CallAdapter<Success, Call<ResponseNetwork<Success, Error>>> {
    override fun responseType() = successType
    override fun adapt(call: Call<Success>): Call<ResponseNetwork<Success, Error>> =
        ResponseNetworkCall(call, errorBodyConverter)
}


internal class ResponseNetworkCall<S : Any, E : Any>(
    private val proxy: Call<S>,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : Call<ResponseNetwork<S, E>> {

    override fun enqueue(callback: Callback<ResponseNetwork<S, E>>) {
        return proxy.enqueue(object : Callback<S>{
            override fun onFailure(call: Call<S>, t: Throwable) {
                callback.onResponse(this@ResponseNetworkCall, Response.success(ResponseNetwork.create(t)))
            }

            override fun onResponse(call: Call<S>, response: Response<S>) {
                callback.onResponse(this@ResponseNetworkCall, Response.success(
                    ResponseNetwork.create(
                        response,
                        errorBodyConverter
                    )
                ))
            }

        })
    }

    override fun isExecuted() = proxy.isExecuted

    override fun clone() = ResponseNetworkCall(proxy.clone(), errorBodyConverter)

    override fun isCanceled() = proxy.isCanceled

    override fun cancel() = proxy.cancel()

    override fun execute(): Response<ResponseNetwork<S, E>> {
       throw UnsupportedOperationException("Response network doesn't support execute")
    }

    override fun request() = proxy.request()
    override fun timeout() = proxy.timeout()

}
