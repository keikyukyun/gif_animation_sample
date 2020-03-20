package com.example.testapp

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.testapp.databinding.ActivityMainBinding
import java.lang.ClassCastException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val drawable = getGifAnimationDrawable()

            // アニメーションを開始する
            drawable.start()
            drawable
        } else {
            // Android P未満はここに処理を書く
            getGifAnimationDrawableLessThanP()
        }

        // アニメーションをセットする
        binding.imageView.setImageDrawable(drawable)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getGifAnimationDrawable(): AnimatedImageDrawable {
        val source = ImageDecoder.createSource(assets, ANIMATION_GIF_FILE_NAME)
        return ImageDecoder.decodeDrawable(source) as? AnimatedImageDrawable
            ?: throw ClassCastException()
    }

    private fun getGifAnimationDrawableLessThanP(): CustomAnimatedDrawable {
        val inputStream = assets.open(ANIMATION_GIF_FILE_NAME)
        return CustomAnimatedDrawable(inputStream)
    }

    companion object {
        private const val ANIMATION_GIF_FILE_NAME = "gif_anim_toy_poodle.gif"
    }
}
