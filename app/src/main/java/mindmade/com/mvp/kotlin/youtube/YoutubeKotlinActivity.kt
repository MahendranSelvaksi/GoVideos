package mindmade.com.mvp.kotlin.youtube

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.kotlin.mvp.R
import mindmade.com.mvp.utils.AppKotlinConstants
import mindmade.com.mvp.youtube.YouTubeDataModel
import mindmade.com.mvp.youtube.YoutubeAdapter
import mindmade.com.mvp.youtube.YoutubeView
import java.util.*

class YoutubeKotlinActivity : AppCompatActivity(), YoutubeView.TubeView, YoutubeKotlinView.TubeKotlinView {

    internal var recyclerView: RecyclerView? = null
    internal var progressBar: ProgressBar? = null

    internal var adapter: YoutubeKoltinAdapter? = null
    internal var dataModels: MutableList<YouTubeDataModel>? = null
    private var index = 0
    internal var presenter: YoutubeKotlinPresenter? = null
    internal var nextPageToken = ""
    val constants: AppKotlinConstants? = null
    private var query="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataModels = ArrayList<YouTubeDataModel>()
        presenter = YoutubeKotlinPresenter(this)

        recyclerView = findViewById(R.id.main_recyclerview) as RecyclerView
        progressBar = findViewById(R.id.mainProgressbar) as ProgressBar
        (recyclerView as RecyclerView).setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager

        presenter!!.loadData(nextPageToken,query)
        adapter = YoutubeKoltinAdapter(this, dataModels!!)
        (recyclerView as RecyclerView).adapter = adapter

        (adapter as YoutubeKoltinAdapter).setLoadMoreListener(YoutubeAdapter.OnLoadMoreListener {
            (recyclerView as RecyclerView).post {
                index = dataModels!!.size
                Log.w("Success", "Size:::: " + index)
                dataModels?.add(YouTubeDataModel(constants?.LOADING_DATA))
                adapter?.notifyItemChanged(dataModels!!.size - 1)
                //   adapter.notifyDataChanged();
                if (dataModels!!.size < dataModels!![0].totalResults) {
                    presenter!!.loadData(nextPageToken,query)
                } else {
                    adapter!!.setMoreDataAvailable(false)
                }
            }
        })
    }

    override fun setAdapterData(data: List<YouTubeDataModel>) {
        if (data.isNotEmpty()) {
            nextPageToken = data.get(0).netPageToken
            Log.w("Success", "Next Page Token::: " + nextPageToken)
            progressBar?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            if (index > 0) {
                dataModels?.removeAt(index)
            }
            dataModels?.addAll(data)
            adapter?.setMoreDataAvailable(true)
            adapter?.notifyDataChanged()
        } else if (data.isEmpty()) {
            nextPageToken = data.get(0).netPageToken
            if (index > 0) {
                dataModels?.removeAt(index)
            }
            dataModels?.addAll(data)
            adapter?.setMoreDataAvailable(false)
            adapter?.notifyDataChanged()
        }
    }

    override fun setError(error: String) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show()
    }

}
