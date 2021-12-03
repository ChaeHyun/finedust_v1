package ch.breatheinandout.screen.common.imageloader

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ImageLoader constructor(
    private val activity: AppCompatActivity
) {

    fun loadImage(imageUrl: String, container: ImageView) {
        GlideApp.with(activity)
            .asGif()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .load(imageUrl)
            .into(container)
    }
}