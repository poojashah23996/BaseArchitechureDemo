package w.com.myapplication

import okhttp3.Interceptor
import okhttp3.Response
import w.com.myapplication.baseClasses.KEY_ACCESS_TOKEN
import w.com.myapplication.baseClasses.PreferenceHelper
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
//        val formBody = FormBody.Builder()
        val requestBuilder = original.newBuilder()
            .method(original.method(), original.body())
        requestBuilder.addHeader(KEY_ACCESS_TOKEN,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMjgiLCJhdWQiOiJVU0VSIiwiaWF0IjoxNTczMzI5NTYxLCJleHAiOjE1NzM5MzQzNjF9.f3P4PoElCNPNV3q-WSEBaGWGOg4zKpdTMYvLJeI1417aBlVHM74aIw6msXljtYTf_M3ivxNeHN9mkuGh0rKPTw")
        //requestBuilder.addHeader("Accept", "application/json")

//        var postBodyString = original.body()?.bodyToString()

        /*if (SharedPrefsManager.containsKey(GlobalKeys.KEY_USER_ID)) {
            //requestBuilder.addHeader(GlobalKeys.KEY_USER_ID, SharedPrefsManager.getString(GlobalKeys.KEY_USER_ID))
            formBody.add(GlobalKeys.KEY_USER_ID, SharedPrefsManager.getString(GlobalKeys.KEY_USER_ID))
        }*/


        //       postBodyString += (if (postBodyString?.length ?: 0 > 0) "&" else "") + formBody.build().bodyToString()

//        requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString.toString()))
        return chain.proceed(requestBuilder.build())
    }


    /*private fun RequestBody.bodyToString(): String {
        val buffer = Buffer()
        this.writeTo(buffer as BufferedSink)
        return buffer.readUtf8()
    }*/
}
