package io.palette.ui.detail

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.GeneratedPalette
import io.palette.utility.extentions.*
import kotlinx.android.synthetic.main.layout_detail.view.*
import kotlinx.android.synthetic.main.layout_detail_info.view.*

@SuppressLint("SetTextI18n")
class DetailAdapter(
        val context: Context,
        val name: String,
        val date: String,
        isLiked: Boolean,
        val showRgb: Boolean,
        val isUnsplash: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var palette: List<GeneratedPalette> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isLiked = isLiked
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var showWallIcon = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var likePalette: ((animator: ObjectAnimator) -> Unit)? = null
    var savePalette: (() -> Unit)? = null
    var sharePalette: (() -> Unit)? = null
    var setWallpaper: ((animator: ObjectAnimator) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.layout_detail_info -> ViewHolderDetailInfo(parent.inflate(R.layout.layout_detail_info))
        else -> ViewHolderDetail(parent.inflate(R.layout.layout_detail))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (position) {
        0 -> (holder as ViewHolderDetailInfo).bind(palette[position])
        else -> (holder as ViewHolderDetail).bind(palette[position - 1])
    }

    override fun getItemCount() = if (palette.isNotEmpty()) palette.size else 0

    override fun getItemViewType(position: Int) = when (position) {
        0 -> R.layout.layout_detail_info
        else -> R.layout.layout_detail
    }

    inner class ViewHolderDetailInfo(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

            itemView.llLike.visible = isUnsplash
            itemView.llWallpaper.visible = isUnsplash

            itemView.ivSave.setOnClickListener { savePalette?.invoke() }
            itemView.ivLike.setOnClickListener { likePalette?.invoke(itemView.ivLike.pulseAnimation()) }
            itemView.ivShare.setOnClickListener { sharePalette?.invoke() }
            itemView.ivWallpaper.setOnClickListener {
                itemView.vDownLine.visible = true
                itemView.ivWallpaper.setImage(R.drawable.ic_file_download_black_24dp)
                setWallpaper?.invoke(itemView.ivWallpaper.downloadAnimation())
            }
        }

        fun bind(palette: GeneratedPalette) {

            itemView.apply {
                infoRootLayout.setBackgroundColor(Color.parseColor("#${palette.hexCode}"))
                tvPhotographerName.text = name
                tvDate.text = date.dateConvert()

                ivLike.setImage(if (isLiked) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp)
                ivWallpaper.setImage(if (showWallIcon) R.drawable.ic_wallpaper_black_24dp else R.drawable.ic_file_download_black_24dp)

                vDownLine.apply {
                    visible = !showWallIcon
                    setBackgroundColor(Color.parseColor("#${palette.hexCode}"))
                }
                listOf(ivSave, ivLike, ivShare, ivWallpaper).forEach { it.setColorFilter(Color.parseColor("#${palette.hexCode}")) }
            }
        }
    }

    inner class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(palette: GeneratedPalette) {
            itemView.apply {
                rootLayout.setBackgroundColor(Color.parseColor("#${palette.hexCode}"))
                tvHexCode.text = "#${palette.hexCode.substring(2)}"

                tvRgb.visible = showRgb
                if (showRgb) tvRgb.text = "rgb${palette.rgb}"

                tvPopulation.text = palette.population.toString()
            }
        }
    }
}
