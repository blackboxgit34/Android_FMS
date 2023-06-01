//# Appended 2015_0916 : Neha Nagpal
package com.humbhi.blackbox.util

import android.app.Activity
import android.content.Context
import android.text.TextUtils.TruncateAt
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView


class AnnouncementDetails(var c: Context, var chat_text_chat: TextView) {
    fun setText(str: String?) {
        chat_text_chat.text = str
    }

    init {


        setText(chat_text_chat.text.toString())
        chat_text_chat.ellipsize = TruncateAt.MARQUEE
        chat_text_chat.isFocusable = true
        chat_text_chat.isFocusableInTouchMode = true
        chat_text_chat.isSingleLine = true
        chat_text_chat.setHorizontallyScrolling(true)
        chat_text_chat.isSelected = true
        chat_text_chat.movementMethod = ScrollingMovementMethod()
        chat_text_chat.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        // takes the unconstrained width of the view
        val width = chat_text_chat.measuredWidth.toFloat()
        val height = chat_text_chat.measuredHeight.toFloat()

        // gets the screen width
        val screenWidth = (c as Activity).windowManager
            .defaultDisplay.width.toFloat()

        // performs the calculation
        var toXDelta = width - (screenWidth - 0)

        // sets toXDelta to -300 if the text width is smaller that the
        // screen size
        if (toXDelta < 0) {
//              toXDelta = 0 - screenWidth;// -300;
            toXDelta = 0 - screenWidth - toXDelta
            val mAnimation: Animation = TranslateAnimation(
                screenWidth,
                toXDelta, 0F, 0F
            )
            mAnimation.duration = 10000
            mAnimation.repeatMode = Animation.RESTART
            mAnimation.repeatCount = Animation.INFINITE
            chat_text_chat.animation = mAnimation
        } else {
            toXDelta = 0 - screenWidth - toXDelta // -300 - toXDelta;
        }
        // Animation parameters
    }
}