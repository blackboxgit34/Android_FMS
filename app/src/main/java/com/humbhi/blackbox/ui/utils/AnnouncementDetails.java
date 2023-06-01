//# Appended 2015_0916 : Neha Nagpal

package com.humbhi.blackbox.ui.utils;
 
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class AnnouncementDetails {

	Context c;
	TextView chat_text_chat;
	SharedPreferences mPref;

	public AnnouncementDetails(Context context ,TextView chat_text_chat){

		this.c=context;
		this.chat_text_chat=chat_text_chat;
		mPref=PreferenceManager.getDefaultSharedPreferences(context);
		String text=mPref.getString("announcement", "");
             
		setText(text);
		chat_text_chat.setEllipsize(TruncateAt.MARQUEE);
		chat_text_chat.setFocusable(true);
		chat_text_chat.setFocusableInTouchMode(true);
		chat_text_chat.setSingleLine(true);
		chat_text_chat.setHorizontallyScrolling(true);
		chat_text_chat.setSelected(true);
		chat_text_chat.setMovementMethod(new ScrollingMovementMethod());
		
		chat_text_chat.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);

          // takes the unconstrained width of the view
          float width = chat_text_chat.getMeasuredWidth();
          float height = chat_text_chat.getMeasuredHeight();

          // gets the screen width
          float screenWidth = ((Activity) c).getWindowManager()
                  .getDefaultDisplay().getWidth();

          // performs the calculation
          float toXDelta = width - (screenWidth - 0);

          // sets toXDelta to -300 if the text width is smaller that the
          // screen size
          if (toXDelta < 0) {
//              toXDelta = 0 - screenWidth;// -300;
        	  toXDelta = 0 - screenWidth - toXDelta;
              Animation mAnimation = new TranslateAnimation(screenWidth,
                      toXDelta, 0, 0);
              mAnimation.setDuration(10000);
              mAnimation.setRepeatMode(Animation.RESTART);
              mAnimation.setRepeatCount(Animation.INFINITE);
              chat_text_chat.setAnimation(mAnimation);
              
              
          } else {
              toXDelta = 0 - screenWidth - toXDelta;// -300 - toXDelta;
          }
          // Animation parameters

	}


	public void setText(String str){

		chat_text_chat.setText(str);

	}

}