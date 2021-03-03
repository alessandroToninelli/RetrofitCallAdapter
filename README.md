# Kotlin-Retrofit-CallAdapter
Adapter for Retrofit written in Kotlin. Adapter usable with Kotlin coroutines

Usage
-----

Add `NetoworkCallAdapter` as a `Call` adapter when building your `Retrofit` instance:
```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(NetworkCallAdapterFactory.create())
    .build()
```

Your service methods can now use `ResponseNetwork<SuccessType, ErrorType>` as their return type.
```kotlin
interface MyService {
  @GET("/user")
  suspend fun getUser(): ResponseNetwork<User, ErrorResponse>
  }

data class User(val id: Int, val name: String, val email: String)

data class ErrorResponse(val type: String, val message: String)
```
Can return 5 instances of ResponseNetwork

```kotlin
api.getUser().run  {
             when (this) {
                
                //error response with body as ErrorResponse Type
                
                is ResponseNetworkError -> //TODO
                
                //unknow error with exception
                
                is ResponseNetworkUnknownError -> //TODO
                
                //success response with body as User Type

                is ResponseNetworkSuccess -> //TODO
                
                //success empty response and status code
                
                is ResponseNetworkSuccessEmpty -> //TODO

                //network error for example connection error

                is ResponseNetworkIOFailure -> //TODO
             }
        }
```


Download
--------

```groovy
implementation 'com.alessandrotoninelli:RetrofitCallAdapter:1.0.1'
```
