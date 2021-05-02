package com.stacydevino.animatedstartup

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowInsets.Type.systemBars
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MostlyEmptyActivity : AppCompatActivity(), StateListener {

    var splashAnimRun = false
    lateinit var splashAnimation : StartupAnimatedView
    lateinit var animationTimeHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animationTimeHandler = Handler(Looper.getMainLooper())
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
                splashAnimation.animListener = this
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
                val wlp = WindowManager.LayoutParams()
                wlp.gravity = Gravity.CENTER
                wlp.height = WindowManager.LayoutParams.MATCH_PARENT
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT
                wlp.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                wlp.format = PixelFormat.TRANSLUCENT

                // Our animation is USING the system bars to an advantage here,
                // but may not in all animations (esp. coloring / fullscreen apps)
                if (VERSION.SDK_INT >= VERSION_CODES.R) {
                    wlp.setFitInsetsTypes(systemBars())
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR)
                }
                windowManager.addView(splashAnimation, wlp)
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

    override fun onStateChanged(state: AnimState) {
        if(state == AnimState.FINISHED){
            removeSplash()
        }
    }
}
