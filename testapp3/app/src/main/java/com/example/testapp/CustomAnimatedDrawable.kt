package com.example.testapp

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import java.io.InputStream

class CustomAnimatedDrawable(
    private val inputStream: InputStream
): Drawable() {
    private val _movie by lazy {
        Movie.decodeStream(inputStream)
    }

    private var _movieStart = 0
    private var _loop = true
    private var _stop = false
    private var relativeMillisecond = 0

    override fun draw(canvas: Canvas) {
        canvas.apply {
            drawColor(Color.TRANSPARENT)
            scale(width / _movie.width().toFloat(),
                height / _movie.height().toFloat() )
        }
        val now = SystemClock.uptimeMillis()

        if (_movieStart == 0) {
            _movieStart = now.toInt()
        }

        relativeMillisecond = when {
            _stop -> {
                _movieStart = 0
                relativeMillisecond
            }
            _loop -> ((now - _movieStart) % _movie.duration()).toInt()
            else -> (now - _movieStart).toInt()
        }

        _movie.apply {
            setTime(relativeMillisecond)
            draw(canvas, 0f, 0f)
        }

        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    fun stop() {
        _stop = true
    }

    fun start() {
        _stop = false
        _movieStart = 0
    }

    fun isRunning() = !_stop
}