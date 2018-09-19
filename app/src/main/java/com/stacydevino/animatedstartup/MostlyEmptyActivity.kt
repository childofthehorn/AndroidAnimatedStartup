package com.stacydevino.animatedstartup

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.WindowManager

class MostlyEmptyActivity : AppCompatActivity() {

    var splashAnimRun = false
    lateinit var splashAnimation : StartupAnimatedView
    lateinit var animationTimeHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animationTimeHandler = Handler()
        runSplashAnimation()
        setContentView(R.layout.activity_mostly_empty)
    }


    fun runSplashAnimation() {
        val windowManager = windowManager
        runSplashAnimation(windowManager)
    }

    fun runSplashAnimation(windowManager: WindowManager) {
        if (!splashAnimRun) {
            runOnUiThread {
                splashAnimRun = true
                splashAnimation = StartupAnimatedView(this)
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
                val wlp = WindowManager.LayoutParams()
                wlp.gravity = Gravity.CENTER
                wlp.height = WindowManager.LayoutParams.MATCH_PARENT
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT
                wlp.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                wlp.format = PixelFormat.TRANSLUCENT
                windowManager.addView(splashAnimation, wlp)
                if (Build.VERSION.SDK_INT >= 22) { // LOLLIPOP MR
                    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR)
                }
                //We have to put a 1000ms delay because the view at startup takes appx. 1 second to load from this point on most devices.
                animationTimeHandler.postDelayed(splashAnimationStartRunnable, 1000)
            }
        }
    }

    fun removeSplash() {
            windowManager.removeView(splashAnimation)
    }

    private val splashAnimationStartRunnable = object : Runnable {
        override fun run() {
            splashAnimation.runAnimation()
            animationTimeHandler.removeCallbacks(this)
        }
    }
}
