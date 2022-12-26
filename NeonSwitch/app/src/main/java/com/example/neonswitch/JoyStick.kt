package com.example.neonswitch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.logging.Handler
import kotlin.math.roundToInt

class JoyStick (context: Context, attributeSet: AttributeSet): View(context, attributeSet), Runnable {

    var thread: Thread = Thread (this)
    lateinit var joystickCallback: OnJoystickMoveListener
    lateinit var onMultipleLongPressListener: OnMultipleLongPressListener
    lateinit var handlerMultipleLongPress: Handler
    lateinit var runnableMultipleLongPress: Runnable

    var DEFAULT_LOOP_INTERVAL : Int = 50
    var MOVE_TOLERANCE: Int = 10
    var loopInterval: Long = 0
    var MOVE_TOLLERANCE: Int = 10
    var xPosition: Int = 0
    var yPosition: Int = 0
    var centerX: Int = 0
    var centerY: Int = 0
    var buttonDirection: Int = 0
    var lastPower: Int = 0
    var lastAngle: Int = 0
    var borderRadius: Int = 0
    var buttonRadius: Int = 0
    var buttonColor: Int = 0

    init {
        var styledAttributes: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.JoyStick)
        try {
            buttonColor = styledAttributes.getColor(R.styleable.JoyStick_buttonColor, R.color.neon)
            var buttonSizeRatio = styledAttributes.getFraction(R.styleable.JoyStick_buttonSizeRatio, 1, 1, 0.25f);
            buttonDirection = styledAttributes.getInteger(R.styleable.JoyStick_buttonDirection, 0)
        }
        finally {
            styledAttributes.recycle()
        }
        runnableMultipleLongPress = Runnable() {
            @Override fun run() {
                if (onMultipleLongPressListener != null) {
                    OnMultipleLongPressListener.onMultipleLongPress()
                }
            }
        }
    }

    interface OnJoystickMoveListener {
        fun onMove(angle: Int, strength: Int)
    }

    fun setOnJoystickMoveListener (listener: OnJoystickMoveListener) {
        setOnJoystickMoveListener(listener, this.DEFAULT_LOOP_INTERVAL)
    }

    fun setOnJoyStickMoveListener (listener: OnJoystickMoveListener, repeatInterval: Int) {
        this.joystickCallback = listener
        this.loopInterval = repeatInterval.toLong()
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

    override fun onTouchEvent (event: MotionEvent): Boolean {
        xPosition = buttonDirection > 0 ? centerX : event.getX()
        yPosition = buttonDirection < 0 ? centerY : event.getY()

        if (event.action == MotionEvent.ACTION_UP) {
            thread.interrupt();

            if ()
        }

    }

    fun resetButtonPosition() {
        xPosition = centerX
        yPosition = centerY
    }

    fun getAngle(): Int {
        var angle: Int = (Math.toDegrees(Math.atan2((yPosition - centerY).toDouble() , (xPosition - centerX).toDouble()))).toInt()
        return if (angle < 0) (angle + 360) else angle

    }

    fun getPower(): Int {
        var length = (((xPosition - centerX) * (xPosition - centerX)) + ((yPosition - centerY) * (yPosition - centerY))).toDouble()
        var power = (100 * (Math.sqrt(length) / borderRadius)).toInt()
        return power
    }

    fun getButtonDirection(): Int {
        return buttonDirection
    }

    fun setButtonDirection(direction: Int) {
        buttonDirection = direction
    }

    fun getNormalizedX(): Int {
        if (width == 0) {
            return 50
        }
        return ((xPosition - buttonRadius) * 100.0f / (width - buttonRadius * 2)).roundToInt()
    }

    fun getNormalizedY(): Int {
        if (height == 0) {
            return 50
        }
        return ((yPosition - buttonRadius) * 100.0f / (height - buttonRadius * 2)).roundToInt()
    }

    override fun run() {
        while (!Thread.interrupted()) {
            post (Runnable() {
                fun run() {
                    if (joystickCallback != null) {
                        joystickCallback.onMove(getAngle(), getPower())
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