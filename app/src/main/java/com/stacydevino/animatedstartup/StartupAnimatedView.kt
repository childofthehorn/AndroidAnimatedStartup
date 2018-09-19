package com.stacydevino.animatedstartup

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.view_startup_splash_animated.view.*

class StartupAnimatedView(context: Context) : BaseConstraintView(context) {

    private val favoriteAnimOutTime = 1100L
    private val androidAnimInTime = 1000L
    private val androidAnimOutTime = 1300L

    private val favoriteStartTime = 500L
    private val androidStartInTime = 400L

    private val decelerateFactor = 2f
    private val decelerateFactorAndroid = 0.8f

    init {
        init(context)
    }

    private fun init(context: Context) {
        this.fitsSystemWindows = true
        View.inflate(context, R.layout.view_startup_splash_animated, this)
    }

    fun runAnimation() {
        //run the animations
        heartAnimation().start()
        androidAnimationIn().start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun heartAnimation(): AnimatorSet {

        //starts at scale 0.3
        val favoriteAnim = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(favoriteLogo, "scaleX", 0.0f)
        val scaleDownY = ObjectAnimator.ofFloat(favoriteLogo, "scaleY", 0.0f)
        scaleDownX.duration = favoriteAnimOutTime
        scaleDownY.duration = favoriteAnimOutTime
        scaleDownX.interpolator = DecelerateInterpolator(decelerateFactor)
        scaleDownY.interpolator = DecelerateInterpolator(decelerateFactor)
        scaleDownX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                constContainer.setBackgroundColor(Color.BLACK)
            }

            override fun onAnimationEnd(animation: Animator) {
                favoriteLogo.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        favoriteAnim.startDelay = favoriteStartTime
        favoriteAnim.playTogether(scaleDownX, scaleDownY)

        return favoriteAnim
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun androidAnimationIn(): AnimatorSet {
        //starts at scale 25 for covering all screen sizes/types
        val androidAnimIn = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(androidLogo, "scaleX", 0.6f)
        val scaleDownY = ObjectAnimator.ofFloat(androidLogo, "scaleY", 0.6f)
        scaleDownX.duration = androidAnimInTime
        scaleDownY.duration = androidAnimInTime
        scaleDownX.interpolator = DecelerateInterpolator(decelerateFactorAndroid)
        scaleDownY.interpolator = DecelerateInterpolator(decelerateFactorAndroid)
        scaleDownX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                constContainer.setBackgroundColor(Color.BLACK)
                androidLogo.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                androidAnimationOut().start()

            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        androidAnimIn.startDelay = androidStartInTime
        androidAnimIn.playTogether(scaleDownX, scaleDownY)

        return androidAnimIn
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun androidAnimationOut(): AnimatorSet {
        //starts at scale 25 for covering all screen sizes/types
        val androidAnimOut = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(androidLogo, "scaleX", 0.0f)
        val scaleDownY = ObjectAnimator.ofFloat(androidLogo, "scaleY", 0.0f)
        scaleDownX.duration = androidAnimOutTime
        scaleDownY.duration = androidAnimOutTime
        scaleDownX.interpolator = AccelerateInterpolator(0.5f)
        scaleDownY.interpolator = AccelerateInterpolator(0.5f)

        val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = androidAnimOutTime

        scaleDownX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                (context as MostlyEmptyActivity).removeSplash()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        androidAnimOut.playTogether(scaleDownX, scaleDownY, fadeOut)

        return androidAnimOut
    }


}

