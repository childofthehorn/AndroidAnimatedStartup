package com.stacydevino.animatedstartup

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.stacydevino.animatedstartup.AnimState.FINISHED
import com.stacydevino.animatedstartup.AnimState.RUNNING
import com.stacydevino.animatedstartup.AnimState.STARTED
import com.stacydevino.animatedstartup.databinding.ViewStartupSplashAnimatedBinding

class StartupAnimatedView(context: Context) : BaseConstraintView(context) {

    private val favoriteAnimOutTime = 1100L
    private val androidAnimInTime = 1000L
    private val androidAnimOutTime = 1300L

    private val favoriteStartTime = 500L
    private val androidStartInTime = 400L

    private val decelerateFactor = 2f
    private val decelerateFactorAndroid = 0.8f

    private lateinit var binding : ViewStartupSplashAnimatedBinding
    lateinit var animListener: StateListener

    init {
        init(context)
    }

    private fun init(context: Context) {
        this.fitsSystemWindows = true
        // Sadly cannot use a `lazy` declaration here
        // or it will take a full second longer to inflate the view
        binding = ViewStartupSplashAnimatedBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun runAnimation() {
        animListener.onStateChanged(STARTED)
        //run the animations
        heartAnimation().start()
        androidAnimationIn().start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun heartAnimation(): AnimatorSet {

        //starts at scale 0.3
        val favoriteAnim = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(binding.favoriteLogo, View.SCALE_X, 0.0f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.favoriteLogo, View.SCALE_Y, 0.0f)
        scaleDownX.duration = favoriteAnimOutTime
        scaleDownY.duration = favoriteAnimOutTime
        scaleDownX.interpolator = DecelerateInterpolator(decelerateFactor)
        scaleDownY.interpolator = DecelerateInterpolator(decelerateFactor)
        scaleDownX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                animListener.onStateChanged(RUNNING)
                binding.constContainer.setBackgroundColor(Color.BLACK)
            }
            override fun onAnimationEnd(animation: Animator) {
                binding.favoriteLogo.visibility = View.GONE
            }
        })
        favoriteAnim.startDelay = favoriteStartTime
        favoriteAnim.playTogether(scaleDownX, scaleDownY)

        return favoriteAnim
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun androidAnimationIn(): AnimatorSet {
        //starts at scale 25 for covering all screen sizes/types
        val androidAnimIn = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(binding.androidLogo, View.SCALE_X, 0.6f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.androidLogo, View.SCALE_Y, 0.6f)
        scaleDownX.duration = androidAnimInTime
        scaleDownY.duration = androidAnimInTime
        scaleDownX.interpolator = DecelerateInterpolator(decelerateFactorAndroid)
        scaleDownY.interpolator = DecelerateInterpolator(decelerateFactorAndroid)
        scaleDownX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                binding.constContainer.setBackgroundColor(Color.BLACK)
                binding.androidLogo.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator) {
                androidAnimationOut().start()
            }
        })

        androidAnimIn.startDelay = androidStartInTime
        androidAnimIn.playTogether(scaleDownX, scaleDownY)

        return androidAnimIn
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun androidAnimationOut(): AnimatorSet {
        //starts at scale 25 for covering all screen sizes/types
        val androidAnimOut = AnimatorSet()

        val scaleDownX = ObjectAnimator.ofFloat(binding.androidLogo, View.SCALE_X, 0.0f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.androidLogo, View.SCALE_Y, 0.0f)
        scaleDownX.duration = androidAnimOutTime
        scaleDownY.duration = androidAnimOutTime
        scaleDownX.interpolator = AccelerateInterpolator(0.5f)
        scaleDownY.interpolator = AccelerateInterpolator(0.5f)

        val fadeOut = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0.0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = androidAnimOutTime

        scaleDownX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                animListener.onStateChanged(FINISHED)
            }
        })

        androidAnimOut.playTogether(scaleDownX, scaleDownY, fadeOut)

        return androidAnimOut
    }
}

