package com.zybooks.retailpriceestimator

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.ImageView

class HelpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_help, container, false)

//        val promoImage = parentView.findViewById<ImageView>(R.id.promoImage)
//        val animator = ValueAnimator.ofFloat(-100f, 200f)
//        animator.duration = 1000
//        animator.interpolator = BounceInterpolator()
////        animator.addUpdateListener { animation: ValueAnimator ->
////            promoImage.baseline = animation.animatedValue as Int
////        }
//
//        animator.start()

        //Rotation for promoImage
//        val animator = ObjectAnimator.ofFloat(promoImage, "rotation", 0f, 360f)
//        animator.duration = 500
//
//        animator.start()

        val bounceAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_animation)
        val promoImage = parentView.findViewById<ImageView>(R.id.promoImage)

        promoImage.startAnimation(bounceAnimation)

        return parentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
}