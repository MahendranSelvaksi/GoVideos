package mindmade.com.mvp.youtube;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mindmade.com.mvp.utils.AppConstants;


public class YoutubePresenter implements YoutubeView.TubePresenter {
    private YoutubeView.TubeView mView;
    private String TAG = "error";

    public YoutubePresenter(YoutubeView.TubeView view) {
        mView = view;
    }

    @Override
    public void loadData(String pageKoken,String quesry) {

        ANRequest request = AndroidNetworking.get(AppConstants.BASE_URL)
                .addQueryParameter(AppConstants.PART_PARAM, AppConstants.PART_VALUE)
                .addQueryParameter(AppConstants.CHANNELID_PARAM, AppConstants.YOUTUBE_CHANNEL_ID)
                .addQueryParameter(AppConstants.KEY_PARAM, AppConstants.YOUTUBE_API_KEY)
                .addQueryParameter(AppConstants.MAX_RESULT_PARAM, AppConstants.MAX_RESULT_VALUE)
                .addQueryParameter(AppConstants.REGION_CODE_PARAM, AppConstants.REGION_CODE_VALUE)
                .addQueryParameter(AppConstants.ORDER_PARAM, AppConstants.ORDER_VALUE)
                .addQueryParameter(AppConstants.PAGE_TOKEN_PARAM, pageKoken)
                .addQueryParameter(AppConstants.QUERY_PARAM, quesry)
                .setTag("Youtube")
                .setPriority(Priority.HIGH)
                .build();
        Log.w("Success", "URL::: " + request.getUrl());
        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                String nextToken = "";
                List<YouTubeDataModel> innerData = new ArrayList<>();
                Log.d("Json", "" + response);
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    Log.d("jsonArray.length()", "" + jsonArray.length());
                    if ( jsonArray.length()>0) {
                        JSONObject pageinfoObj = response.getJSONObject("pageInfo");
                        if (response.has("nextPageToken")) {
                            nextToken = response.getString("nextPageToken");
                        } else {
                            nextToken = "";
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            YouTubeDataModel listData = new YouTubeDataModel(AppConstants.ADAPTER_DATA);
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            JSONObject jsonArray1 = jObj.getJSONObject("id");
                            if (jsonArray1.has("videoId")) {
                                listData.setVideoID(jsonArray1.getString("videoId"));
                                JSONObject jsonArray2 = jObj.getJSONObject("snippet");
                                listData.setChannelTitle(jsonArray2.getString("channelTitle"));
                                listData.setVideoTitle(jsonArray2.getString("title"));
                                listData.setVideoDescription(jsonArray2.getString("description"));
                                JSONObject jsonArray3 = jsonArray2.getJSONObject("thumbnails");
                                JSONObject jsonArray4 = jsonArray3.getJSONObject("default");
                                listData.setThumbnailDefault(jsonArray4.getString("url"));
                                JSONObject jsonArray5 = jsonArray3.getJSONObject("medium");
                                listData.setThumbnailMedium(jsonArray5.getString("url"));
                                JSONObject jsonArray6 = jsonArray3.getJSONObject("high");
                                listData.setThumbnailHigh(jsonArray6.getString("url"));
                                listData.setNetPageToken(nextToken);
                                listData.setTotalResults(pageinfoObj.getInt("totalResults"));
                                innerData.add(listData);
                            }
                        }
                        mView.setAdapterData(innerData);
                    }else{
                        Log.w("success","Comes else part"+innerData.size());
                        mView.setAdapterData(innerData);
                     //   mView.setAdapterData(new ArrayList<YouTubeDataModel>());
                    }

                } catch (Exception ex) {
                    Log.e("Json Exception", "" + ex);
                }
            }

            @Override
            public void onError(ANError error) {
                if (error.getErrorCode() != 0) {
                    Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                    Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                    Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                    mView.setError(error.getErrorBody());
                } else {
                    Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                }
            }
        });


      //  request.cancel(true);


    }
}
