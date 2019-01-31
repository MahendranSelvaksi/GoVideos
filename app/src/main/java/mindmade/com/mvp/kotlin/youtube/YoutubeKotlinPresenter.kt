package mindmade.com.mvp.kotlin.youtube

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import mindmade.com.mvp.utils.AppConstants
import mindmade.com.mvp.youtube.YouTubeDataModel
import mindmade.com.mvp.youtube.YoutubeView
import org.json.JSONObject


class YoutubeKotlinPresenter(view: YoutubeKotlinView.TubeKotlinView) : YoutubeView.TubePresenter {
    val mView: YoutubeKotlinView.TubeKotlinView = view;
    override fun loadData(pageKoken: String?,query: String?) {
        val request = AndroidNetworking.get(AppConstants.BASE_URL)
                .addQueryParameter(AppConstants.PART_PARAM, AppConstants.PART_VALUE)
                .addQueryParameter(AppConstants.CHANNELID_PARAM, AppConstants.YOUTUBE_CHANNEL_ID)
                .addQueryParameter(AppConstants.KEY_PARAM, AppConstants.YOUTUBE_API_KEY)
                .addQueryParameter(AppConstants.MAX_RESULT_PARAM, AppConstants.MAX_RESULT_VALUE)
                .addQueryParameter(AppConstants.REGION_CODE_PARAM, AppConstants.REGION_CODE_VALUE)
                .addQueryParameter(AppConstants.ORDER_PARAM, AppConstants.ORDER_VALUE)
                .addQueryParameter(AppConstants.PAGE_TOKEN_PARAM, pageKoken)
                .addQueryParameter(AppConstants.QUERY_PARAM, query)
                .setTag("Youtube")
                .setPriority(Priority.HIGH)
                .build()
        Log.w("Success", "URL::: " + request.url)
        request.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                try {
                    Log.w("Success", "Response::: " + response)
                    val innerData: MutableList<YouTubeDataModel> = ArrayList()
                    val nextToken = response!!.getString("nextPageToken")
                    val pageinfoObj = response.getJSONObject("pageInfo")
                    val jsonArray = response.getJSONArray("items")
                    Log.w("Success", "Length::: " + jsonArray.length())
                    for (i in 0..jsonArray.length()-1) {
                        val listData = YouTubeDataModel(AppConstants.ADAPTER_DATA)
                        val jObj = jsonArray.getJSONObject(i)
                        val jsonArray1 = jObj.getJSONObject("id")
                        if (jsonArray1.has("videoId")) {
                            listData.videoID = jsonArray1.getString("videoId")
                            val jsonArray2 = jObj.getJSONObject("snippet")
                            listData.channelTitle = jsonArray2.getString("channelTitle")
                            listData.videoTitle = jsonArray2.getString("title")
                            listData.videoDescription = jsonArray2.getString("description")
                            val jsonArray3 = jsonArray2.getJSONObject("thumbnails")
                            val jsonArray4 = jsonArray3.getJSONObject("default")
                            listData.thumbnailDefault = jsonArray4.getString("url")
                            val jsonArray5 = jsonArray3.getJSONObject("medium")
                            listData.thumbnailMedium = jsonArray5.getString("url")
                            val jsonArray6 = jsonArray3.getJSONObject("high")
                            listData.thumbnailHigh = jsonArray6.getString("url")
                            listData.netPageToken = nextToken
                            listData.totalResults = pageinfoObj.getInt("totalResults")
                            innerData.add(listData)
                        }
                    }
                    mView.setAdapterData(innerData)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(anError: ANError?) {
            }
        })


    }
}

