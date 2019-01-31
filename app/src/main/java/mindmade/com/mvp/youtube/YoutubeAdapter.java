package mindmade.com.mvp.youtube;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kotlin.mvp.R;

import java.util.List;

import mindmade.com.mvp.utils.AppConstants;
import mindmade.com.mvp.youtubePlayer.YoutubePlayerActivity;



public class YoutubeAdapter extends RecyclerView.Adapter {
    public Context mContext;
    private List<YouTubeDataModel> mData;
    public OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private boolean moreDataAvailable = false;

    public YoutubeAdapter(Context mContext, List<YouTubeDataModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == 1) {
            return new YoutubeViewHolder(inflater.inflate(R.layout.youtube_adapter, parent, false));
        } else {
            return new LoadingHolder(inflater.inflate(R.layout.bottom_loading, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType().equalsIgnoreCase(AppConstants.ADAPTER_DATA)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && moreDataAvailable && !isLoading && onLoadMoreListener != null) {
            isLoading = true;
            onLoadMoreListener.onLoadMore();
        }
        if (getItemViewType(position) == 1) {
            if (holder instanceof YoutubeViewHolder) {
                ((YoutubeViewHolder) holder).titleTV.setText(mData.get(position).getVideoTitle());
                ((YoutubeViewHolder) holder).contentTV.setText(mData.get(position).getVideoDescription());

                Glide.with(mContext)
                        .load(mData.get(position).getThumbnailMedium())
                        .fitCenter()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                //holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(((YoutubeViewHolder) holder).logoImg);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        onLoadMoreListener = loadMoreListener;
    }

   /* public boolean isLoading() {
        return isLoading;
    }*/

    public void notifyDataChanged() {
        isLoading = false;
        notifyDataSetChanged();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    private class YoutubeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView logoImg;
        TextView titleTV, contentTV;
        CardView cardView;

        YoutubeViewHolder(View itemView) {
            super(itemView);
            logoImg = (ImageView) itemView.findViewById(R.id.youtube_image);
            titleTV = (TextView) itemView.findViewById(R.id.youtube_titleTV);
            contentTV = (TextView) itemView.findViewById(R.id.youtube_contentTV);
            cardView = (CardView) itemView.findViewById(R.id.youtube_adapter_list_containor);

            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent playerIntent = new Intent(mContext, YoutubePlayerActivity.class);
            playerIntent.putExtra(AppConstants.INTENT_KEY, mData.get(getAdapterPosition()).getVideoID());
            mContext.startActivity(playerIntent);
        }
    }

    private static class LoadingHolder extends RecyclerView.ViewHolder {
        LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
