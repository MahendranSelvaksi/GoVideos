package mindmade.com.mvp.youtube;

public class YouTubeDataModel {

    private String videoID;
    private String videoTitle;
    private String videoDescription;
    private String thumbnailDefault;
    private String thumbnailMedium;
    private String thumbnailHigh;
    private String channelTitle;
    private String type;
    private String netPageToken;
    private int totalResults;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getNetPageToken() {
        return netPageToken;
    }

    public void setNetPageToken(String netPageToken) {
        this.netPageToken = netPageToken;
    }

    public  YouTubeDataModel(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getThumbnailDefault() {
        return thumbnailDefault;
    }

    public void setThumbnailDefault(String thumbnailDefault) {
        this.thumbnailDefault = thumbnailDefault;
    }

    public String getThumbnailMedium() {
        return thumbnailMedium;
    }

    public void setThumbnailMedium(String thumbnailMedium) {
        this.thumbnailMedium = thumbnailMedium;
    }

    public String getThumbnailHigh() {
        return thumbnailHigh;
    }

    public void setThumbnailHigh(String thumbnailHigh) {
        this.thumbnailHigh = thumbnailHigh;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
