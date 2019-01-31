package mindmade.com.mvp.youtube;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.kotlin.mvp.R;

import java.util.ArrayList;
import java.util.List;

import mindmade.com.mvp.utils.AppConstants;
import mindmade.com.mvp.utils.Utility;

public class YoutubeActivity extends AppCompatActivity implements YoutubeView.TubeView {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    private TextView noDataFountTV;
    YoutubeAdapter adapter;
    List<YouTubeDataModel> dataModels;
    private int index = 0;
    YoutubePresenter presenter;
    String nextPageToken = "";
    public static String query = "";
    private EditText searchET;
    private ImageView ivSearch, ivCleartext;
    private Utility mUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataModels = new ArrayList<>();
        presenter = new YoutubePresenter(this);
        mUtility=new Utility();

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.mainProgressbar);
        searchET = (EditText) findViewById(R.id.searchET);
        ivCleartext= (ImageView) findViewById(R.id.ivCleartext);
        ivSearch= (ImageView) findViewById(R.id.ivSearch);
        noDataFountTV= (TextView) findViewById(R.id.noDataFountTV);

        query=searchET.getText().toString().trim();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        presenter.loadData(nextPageToken,query);
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
                        //   adapter.notifyDataChanged();
                        if (dataModels.size() < dataModels.get(0).getTotalResults()) {
                            presenter.loadData(nextPageToken,query);
                        } else {
                            adapter.setMoreDataAvailable(false);
                        }
                    }
                });
            }
        });


        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.w("Success","ActionIDDDD:::"+actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    if (searchET.getText().toString().trim().length() == 0) {
                        ivCleartext.performClick();
                    } else {
                        mUtility.hideKeyboard(YoutubeActivity.this, searchET);
                        query = searchET.getText().toString().trim();
                        dataModels.clear();
                        adapter.notifyDataChanged();
                        index = 0;
                        nextPageToken="";
                        presenter.loadData(nextPageToken,query);
                        ivCleartext.setVisibility(View.VISIBLE);
                        ivSearch.setVisibility(View.GONE);
                    }
                    //.hideKeyboard(mActivity, txtPastOrdersSearchText);
                    /*if (searchET.getText().toString().trim().length() >= 3) {
                        initializeHomeFragment();
                    } else {
                        *//*Snackbar.make(rootView, getString(R.string.searchTextInadequateLengthString), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*//*
                    }*/
                    return true;
                }
                return false;
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtility.hideKeyboard(YoutubeActivity.this, searchET);
                if (searchET.getText().toString().trim().length() >= 3) {
                    query = searchET.getText().toString().trim();
                    dataModels.clear();
                    adapter.notifyDataChanged();
                    index = 0;
                    nextPageToken="";
                    presenter.loadData(nextPageToken,query);
                    ivCleartext.setVisibility(View.VISIBLE);
                    ivSearch.setVisibility(View.GONE);
                } else {
                   /* Snackbar.make(rootView, getString(R.string.searchTextInadequateLengthString), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    Toast.makeText(YoutubeActivity.this, "Please give at least 3 characters...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCleartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Success", "Comes ivCleartext");
               mUtility.hideKeyboard(YoutubeActivity.this, searchET);
                //showProgressDialog(getString(R.string.fetchProductStr));
                searchET.setText("");
                dataModels.clear();
                adapter.notifyDataSetChanged();
                query = searchET.getText().toString().trim();
                index = 0;
                nextPageToken="";
                presenter.loadData(nextPageToken,query);
                ivSearch.setVisibility(View.VISIBLE);
                ivCleartext.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setAdapterData(List<YouTubeDataModel> data) {
        progressBar.setVisibility(View.GONE);
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
          //  nextPageToken = data.get(0).getNetPageToken();
            if (index > 0) {
                dataModels.remove(index);
            }
            dataModels.addAll(data);
            adapter.setMoreDataAvailable(false);
            adapter.notifyDataChanged();
        }
        if (dataModels.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noDataFountTV.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            noDataFountTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setError(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }
}
