package com.pb.seenItAssignment.ui

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pb.seenItAssignment.R
import com.pb.seenItAssignment.data.model.Article
import com.pb.seenItAssignment.data.repository.NetworkState
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.news_list_item.view.*

class NewsPagedListAdapter(val context: Context): PagedListAdapter<Article, RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
     var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.news_list_item, parent, false)
            return NewsItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as NewsItemViewHolder).bind(getItem(position))
        } else {
            (holder as NetworkStateItemHolder).bind(networkState)
        }
    }




    class MovieDiffCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title.equals(newItem.title)
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount()+ if (hasExtraRow())1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position==itemCount-1){
            NETWORK_VIEW_TYPE
        }
        else{
            MOVIE_VIEW_TYPE
        }
    }



    class NewsItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(article: Article?) {
            itemView.cv_news_title.text = article?.title
            itemView.cv_news_description.text = article?.description
            itemView.link.apply {
                isClickable=true
               movementMethod=LinkMovementMethod.getInstance()
                val text="<a href='${article?.url}'> Tap to View </a>"
                setText(Html.fromHtml(text))
            }

            Glide.with(itemView.context)
                .load(article?.urlToImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.iv_poster)


        }
    }
    class NetworkStateItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            } else {
                itemView.progress_bar_item.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else
                if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                    itemView.error_msg_item.visibility = View.VISIBLE
                    itemView.error_msg_item.text = networkState.msg
                } else {
                    itemView.error_msg_item.visibility = View.GONE
                }

        }

    }

    fun setNetworkState1(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }
}