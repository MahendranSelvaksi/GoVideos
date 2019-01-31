package mindmade.com.mvp.kotlin.youtube

import mindmade.com.mvp.youtube.YouTubeDataModel


public interface YoutubeKotlinView {

    interface TubeKotlinView {
        fun setAdapterData(data: List<YouTubeDataModel> = ArrayList());
        fun setError(error: String);
    }

    interface TubeKotlinPresenter {
        fun loadData(nextPageToken: String);
    }
}