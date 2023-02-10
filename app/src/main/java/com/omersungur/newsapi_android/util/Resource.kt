package com.omersungur.newsapi_android.util

sealed class Resource<T>( //sealed class hiçbir sınıfın kendisinden türetilemeyeceğini ifade eder.
    val data : T? = null,
    val message : String? = null
) {

    class Success<T>(data : T) : Resource<T>(data)
    class Error<T>(message : String, data : T? = null) : Resource<T>(data,message)
    class Loading<T> : Resource<T>()
}