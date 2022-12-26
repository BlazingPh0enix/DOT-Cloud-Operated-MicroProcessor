package com.example.neonswitch

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

class VerticalSeekbar(context: Context, attributeSet: AttributeSet): AppCompatSeekBar(context, attributeSet) {

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure (widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw (canvas: Canvas) {
        canvas.rotate(-90f)
        canvas.translate(-1f * height, 0f)

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                var i = max - (max * event.y / height).toInt()
                progress = i
                onSizeChanged(width, height, 0, 0)
            }
            MotionEvent.ACTION_CANCEL -> {}
        }
        return true
    }
}