package com.example.neonswitch

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class Controller: AppCompatActivity() {

    var pitch: Int = 0
    var roll: Int = 0

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.controller)
        joyStickView.setOnJoystickMoveListener(JoyStick.OnJoystickMoveListener() {
            fun onMove(angle: Int, strength: Int) {
                if (strength != 0) {
                    if (angle > -10 && angle < 10) {
                        pitch = Math.round((strength / 10.0f).toDouble())
                    }
                    else if (angle < -170 || angle > 170) {
                        pitch = -1 * (Math.round((strength / 10.0f).toDouble()))
                    }
                    else if (angle < -80 && angle > -100) {
                        roll = -1 * (Math.round((strength / 10.0f).toDouble()))
                    }
                    else if (angle > 80 && angle < 100) {
                        roll = Math.round((strength / 10.0f).toDouble())
                    }
                    else {
                         if (angle > 0 && angle < 90) {
                             roll = Math.round(strength * ((angle / 90.0f).toFloat()))
                             pitch = Math.round(strength - roll)
                        } else if (angle > 90 && angle < 180) {
                             angle = angle - 90
                             pitch = Math.round(strength * ((angle / 90.0f).toFloat()))
                             roll = Math.round(strength - pitch)
                             pitch = -1 * pitch
                         } else if (angle < 0 && angle > -90) {
                             angle = -1 *  angle
                             roll = Math.round(strength * ((angle / 90.0f).toFloat()))
                             pitch = Math.round(strength - roll)
                             roll = -1 * roll
                         } else {
                             angle = (-1 * angle) - 90
                             pitch = Math.round(strength * ((angle / 90.0f).toFloat()))
                             roll = Math.round(strength - pitch)
                             pitch = -1 * pitch
                         }

                    }
                }
            }
        })
    }
}