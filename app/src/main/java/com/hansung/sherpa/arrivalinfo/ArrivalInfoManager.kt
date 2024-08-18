package com.hansung.sherpa.arrivalinfo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * @param context Activity의 context
 */
class ArrivalInfoManager(val context: Context) {
    /**
     * 정류소별특정노선버스 도착예정정보 목록조회 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param request 요청할 정보 객체
     * @return LiveData<ArrivalInfoResponse>
     */
    fun getArrivalInfoList(request:ArrivalInfoRequest): LiveData<ArrivalInfoResponse> {
        val resultLiveData = MutableLiveData<ArrivalInfoResponse>()
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.bus_arrival_info_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ArrivalInfoService::class.java)

        service.getService(request.getMap()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()
                Log.d("explain", "responseBody: ${responseBody?.string()}")
                if (responseBody != null) {
                    val ArrivalInfoResponse = Gson().fromJson(
                        responseBody.string(),
                        ArrivalInfoResponse::class.java
                    )
                    resultLiveData.postValue(ArrivalInfoResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("explain", "onFailure: 실패")
                Log.d("explain", "message: ${t.message}")
            }
        })
        return resultLiveData
    }

    /**
     * 정류소별특정노선버스 도착예정정보 목록조회 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param request 요청할 정보 객체
     * @return ArrivalInfoResponse
     */
    fun getArrivalInfoList2(request: ArrivalInfoRequest): ArrivalInfoResponse? {
        var result: ArrivalInfoResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl(context.getString(R.string.bus_arrival_info_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    result = Gson().fromJson(response.body()!!.string(), ArrivalInfoResponse::class.java)
                } catch (e: IOException) {
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        return result
    }
}

