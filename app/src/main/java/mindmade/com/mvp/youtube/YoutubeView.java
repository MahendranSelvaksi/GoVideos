package mindmade.com.mvp.youtube;

import java.util.List;



public interface YoutubeView {

    public interface TubeView{
        void setAdapterData(List<YouTubeDataModel> data);
        void setError(String error);
    }
    interface TubePresenter{
        void loadData(String pageKoken,String quesry);
    }
}
