package mindmade.com.mvp.kotlin.youtube

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kotlin.mvp.R

import mindmade.com.mvp.utils.AppConstants

import mindmade.com.mvp.youtube.YouTubeDataModel
import mindmade.com.mvp.youtube.YoutubeAdapter
import mindmade.com.mvp.youtubePlayer.YoutubePlayerActivity


class YoutubeKoltinAdapter(context: Context, dataModel: List<YouTubeDataModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext: Context? = context
    var mData: List<YouTubeDataModel>? = dataModel
    var onLoadMoreListener:YoutubeAdapter.OnLoadMoreListener? = null
    private var isLoading = false
    private var moreDataAvailable = false
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position >= itemCount - 1 && moreDataAvailable && !isLoading && onLoadMoreListener != null) {
            isLoading = true
            onLoadMoreListener!!.onLoadMore()
        }
        if (getItemViewType(position) == 1) {
            if (holder is YoutubeViewHolder) holder.bindItems(mData!!.get(position), mContext!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.youtube_adapter, parent, false)
            return YoutubeViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.bottom_loading, parent, false)
            return LoadingHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return mData?.size as Int
    }


    override fun getItemViewType(position: Int): Int {
        if (mData?.get(position)?.type.equals(AppConstants.ADAPTER_DATA, ignoreCase = true)) {
            return 1
        } else {
            return 2
        }
    }

    fun setLoadMoreListener(loadMoreListener: YoutubeAdapter.OnLoadMoreListener) {
        onLoadMoreListener = loadMoreListener
    }

    fun notifyDataChanged() {
        isLoading = false
        notifyDataSetChanged()
    }

    fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        this.moreDataAvailable = moreDataAvailable
    }

    class YoutubeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(dataModel: YouTubeDataModel, context: Context) {
            val logoImg = itemView.findViewById(R.id.youtube_image) as ImageView
            val titleTV = itemView.findViewById(R.id.youtube_titleTV) as TextView
            val contentTV = itemView.findViewById(R.id.youtube_contentTV) as TextView
            val cardView = itemView.findViewById(R.id.youtube_adapter_list_containor) as CardView

            titleTV.setText(dataModel.getVideoTitle())
            contentTV.setText(dataModel.getVideoDescription())

            Glide.with(context)
                    .load(dataModel.getThumbnailMedium())
                    .fitCenter()
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(logoImg)

            cardView.setOnClickListener {
                val playerIntent = Intent(context, YoutubePlayerActivity::class.java)
                playerIntent.putExtra(AppConstants.INTENT_KEY, dataModel.getVideoID())
                context.startActivity(playerIntent)
            }
        }
    }

    class LoadingHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}
