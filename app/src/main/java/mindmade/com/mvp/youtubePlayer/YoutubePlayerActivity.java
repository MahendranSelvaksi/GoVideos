package mindmade.com.mvp.youtubePlayer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kotlin.mvp.R;

import java.util.ArrayList;
import java.util.List;


import mindmade.com.mvp.utils.AppConstants;
import mindmade.com.mvp.youtube.YouTubeDataModel;
import mindmade.com.mvp.youtube.YoutubeActivity;
import mindmade.com.mvp.youtube.YoutubeAdapter;
import mindmade.com.mvp.youtube.YoutubePresenter;
import mindmade.com.mvp.youtube.YoutubeView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YoutubeView.TubeView {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    YoutubeAdapter adapter;
    List<YouTubeDataModel> dataModels;
    private int index = 0;
    YoutubePresenter presenter;
    String nextPageToken = "";
    YouTubePlayerView playerView;
    private YouTubePlayer player;
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        /*getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);*/

        dataModels = new ArrayList<>();
        presenter = new YoutubePresenter(this);

        recyclerView = (RecyclerView) findViewById(R.id.detail_recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.detail_progressbar);
        playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        playerView.initialize(AppConstants.YOUTUBE_API_KEY, this);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        presenter.loadData(nextPageToken, YoutubeActivity.query);
        adapter = new YoutubeAdapter(this, dataModels);
        recyclerView.setAdapter(adapter);

        adapter.setLoadMoreListener(new YoutubeAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        index = dataModels.size();
                        Log.w("Success", "Size:::: " + index);
                        dataModels.add(new YouTubeDataModel(AppConstants.LOADING_DATA));
                        adapter.notifyItemChanged(dataModels.size() - 1);
                        if (dataModels.size() < dataModels.get(0).getTotalResults()) {
                            presenter.loadData(nextPageToken,YoutubeActivity.query);
                        } else {
                            adapter.setMoreDataAvailable(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        if (!b){
            youTubePlayer.cueVideo(getIntent().getStringExtra(AppConstants.INTENT_KEY));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setAdapterData(List<YouTubeDataModel> data) {
        if (data.size() > 0) {
            nextPageToken = data.get(0).getNetPageToken();
            Log.w("Success", "Next Page Token::: " + nextPageToken);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (index > 0) {
                dataModels.remove(index);
            }
            dataModels.addAll(data);
            adapter.setMoreDataAvailable(true);
            adapter.notifyDataChanged();
        } else {
            nextPageToken = data.get(0).getNetPageToken();
            if (index > 0) {
                dataModels.remove(index);
            }
            dataModels.addAll(data);
            adapter.setMoreDataAvailable(false);
            adapter.notifyDataChanged();
        }
    }

    @Override
    public void setError(String error) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    /* release ut when home button pressed. */
        if (player != null) {
            player.release();
        }
        player = null;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
    /* release ut when go to other fragment or back pressed */
        if (player != null) {
            player.release();
        }
        player = null;
        super.onStop();
    }

}
