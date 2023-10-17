package com.example.photobuilder.feature.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.photobuilder.R

class RoundedImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val path = Path()
    private val rect = RectF()
    private val paint = Paint()

    private var cornerRadius: Float = 0f

    init {
        paint.isAntiAlias = true

        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
        cornerRadius = typedArray.getDimension(R.styleable.RoundedImageView_cornerRadius, 0f)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        path.reset()
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}
