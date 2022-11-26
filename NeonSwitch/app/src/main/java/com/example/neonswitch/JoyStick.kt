package com.example.neonswitch

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class JoyStick (context: Context, attributeSet: AttributeSet): View(context, attributeSet), Runnable {

    var thread: Thread = Thread (this)
    lateinit var joystickCallback: OnJoystickMoveListener
    var DEFAULT_LOOP_INTERVAL : Int = 50
    var loopInterval: Long = 0
    var MOVE_TOLLERANCE: Int = 10
    var xPosition: Int = 0
    var yPosition: Int = 0
    var centerX: Int = 0
    var centerY: Int = 0
    var buttonDirection: Int = 0

    lateinit var onMultipleLongPressListener: OnMultipleLongPressListener

    init {
        var styledAttributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.JoyStick)

        var mRunnableMultipleLongPress: Runnable = Runnable() {
            override fun run() {
                if (mOnMultipleLongPressListener != null) {
                    mOnMultipleLongPressListener.onMultipleLongPress()
                }
            }
        }
    }

    interface OnJoystickMoveListener {
        fun onMove(angle: Int, strength: Int)
    }

    fun setOnJoystickMoveListener (listener: OnJoystickMoveListener) {
        setOnJoystickMoveListener(listener, DEFAULT_LOOP_INTERVAL)
    }

    fun setOnJoyStickMoveListener (listener: OnJoystickMoveListener, repeatInterval: Int) {
        this.joystickCallback = listener
        this.loopInterval = repeatInterval
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (buttonDirection < 0) {
            xPosition = centerX
        }
        else {
            if (event != null) {
                xPosition = event.x.toInt()
            }
        }

        if (buttonDirection > 0) {
            yPosition = centerY
        }
        else {
            if (event != null) {
                yPosition = event.y.toInt()
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            thread.interrupt()

            resetButtonPosition()

            if ()
        }
        return true
    }

    fun resetButtonPosition() {
        xPosition = centerX
        yPosition = centerY
    }

    fun getButtonDirection(): Int {
        return buttonDirection
    }

    fun getNormalizedX(): Int {
        if (getWidth() == 0) {
            return 50
        }
        return Math.round((xPosition - buttonRadius) * 100.0f / (getWidth() - buttonRadius*2))
    }

    override fun run() {
        while (!Thread.interrupted()) {
            post (Runnable() {
                fun run() {
                    if (joystickCallback != null) {
                        joystickCallback.onMove(getAngle(), getStrength())
                    }
                }
            })

            try {
                Thread.sleep(loopInterval)
            }

            catch (exception: InterruptedException) {
                break
            }
        }
    }
}