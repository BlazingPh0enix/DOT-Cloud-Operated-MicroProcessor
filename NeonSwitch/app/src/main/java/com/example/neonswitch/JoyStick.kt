package com.example.neonswitch

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View

class JoyStick (context: Context, attributeSet: AttributeSet): View(context, attributeSet), Runnable {

    var thread: Thread = Thread (this)
    var DEFAULT_LOOP_INTERVAL : Long = 50
    var MOVE_TOLLERANCE: Int = 10
    var xPosition: Int = 0
    var yPosition: Int = 0
    var centerX: Int = 0
    var centerY: Int = 0

    lateinit var mCallBack: OnMoveListener
    lateinit var mOnMultipleLongPressListener: OnMultipleLongPressListener

    init {
        var mRunnableMultipleLongPress: Runnable = Runnable() {
            override fun run() {
                if (mOnMultipleLongPressListener != null) {
                    mOnMultipleLongPressListener.onMultipleLongPress()
                }
            }
        }
    }

    interface OnMoveListener {
        fun onMove(angle: Int, strength: Int)
    }

    interface OnMultipleLongPressListener {
        fun onMultipleLongPress()
    }



    fun initPosition() {
        xPosition = getWidth() / 2
        centerX = xPosition
        yPosition = getHeight() / 2
        centerY = yPosition
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        initPosition()

        var d: Int = Math.min(w, h)
        mButtonRadius = d / 2 * 0.25
        mBorderRadius = d / 2 * mBackgroundSizeRatio
        xPosition = width / 2
        yPosition = height / 2

        buttonRadius = (d / 2 * 0.25).toInt()

        mBackgroundRadius = mBorderRadius - (mPaintCircleBorder.getStrokeWidth() / 2)

        if (mButtonBitmap != null) {
            mButtonBitmap = Bitmap.createScaledBitmap(mButtonBitmap, mButtonRadius * 2, mButtonRadius * 2, true)
        }
    }

    override fun run() {
        while (!Thread.interrupted()) {
            post (Runnable() {
                fun run() {
                    if (mCallback != null) {
                        mCallBack.onMove(getAngle(), getStrength())
                    }
                }
            })

            try {
                Thread.sleep(DEFAULT_LOOP_INTERVAL)
            }

            catch (exception: InterruptedException) {
                break
            }
        }
    }
}