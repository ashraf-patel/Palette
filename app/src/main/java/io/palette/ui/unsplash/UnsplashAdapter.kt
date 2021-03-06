package io.palette.ui.unsplash

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.palette.R
import io.palette.data.models.Unsplash
import io.palette.utility.PaginatedAdapter
import io.palette.utility.extentions.getRandom
import io.palette.utility.extentions.inflate
import io.palette.utility.extentions.maintainAspectRatio
import kotlinx.android.synthetic.main.layout_unsplash.view.*
import javax.inject.Inject

class UnsplashAdapter @Inject constructor(context: Context, val callBack: Callback, retryCallback: () -> Unit) : PaginatedAdapter<Unsplash>(
        context,
        R.string.unsplash_loading,
        R.string.unsplash_error_loading,
        retryCallback
) {

    var likedPalettes: List<Unsplash> = listOf()

    override fun onCreateItemViewHolder(parent: ViewGroup) = UnplashViewHolder(parent.inflate(R.layout.layout_unsplash))

    inner class UnplashViewHolder(itemView: View) : NetworkBaseViewHolder(itemView) {

        init {
            itemView.ivImage.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                getItem(adapterPosition) ?: return@setOnClickListener
                callBack.openDetail(itemView.ivImage, getItem(adapterPosition)!!, likedPalettes.contains(getItem(adapterPosition)))
            }
        }

        override fun bind(item: Unsplash) {
            super.bind(item)
            Glide.with(context)
                    .setDefaultRequestOptions(RequestOptions().apply {
                        placeholder(listOf(R.color.colorBlack, R.color.colorBlackTwo, R.color.colorBlackThree).getRandom())
                        error(R.color.colorBlackTwo)
                    })
                    .asBitmap()
                    .load(item.urls?.regular)
                    .into(itemView.ivImage)
            itemView.ivImage.maintainAspectRatio(item.width, item.height)
        }
    }

    interface Callback {
        fun openDetail(view: View, unsplash: Unsplash, isLiked: Boolean)
    }
}