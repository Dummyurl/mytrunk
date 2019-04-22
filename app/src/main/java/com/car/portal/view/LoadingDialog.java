package com.car.portal.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.car.portal.R;
import com.car.portal.application.MyApplication;

/**
 * Created by Admin on 2016/6/14.
 */
public class LoadingDialog {
    private Context context;
    private static PopupWindow popupDialog = null;
    private RelativeLayout layout;
    private static RelativeLayout layout_bg;
    private static View circleView;
    private RotateAnimation rotateAnim = null;
    private static boolean isShow = false;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    private void initAnimation(){
        /** 设置旋转动画 */
        rotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setRepeatCount(-1);//设置动画循环进行
        rotateAnim.setInterpolator(new LinearInterpolator());//旋转速率 ，设置为匀速旋转
        rotateAnim.setDuration(2000);
    }

    /**
     * 判断popupwindow当前是否是显示状态
     * @return
     */
    public static boolean isShowing() {
        if (popupDialog != null && popupDialog.isShowing()) {
            isShow = true;
        } else {
            isShow = false;
        }
        return isShow;
    }
    /**
     * 显示
     */
    public void show() {
        if (MyApplication.getNetState()) {
            dismiss();
            initAnimation();
            isShow = true;
            layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.loading_dialog,
                    null);
            circleView = (View) layout.findViewById(R.id.loading_dialog);
            layout_bg = (RelativeLayout) layout.findViewById(R.id.bgLayout);
            popupDialog = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

            View parentView = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            layout.getBackground().setAlpha(100);
            popupDialog.showAtLocation(parentView, Gravity.CENTER, 0, 0);
            circleView.startAnimation(rotateAnim);

        }
    }

    /**
     * 动画结束，popup结束
     */
    public static void dismiss() {
        if (popupDialog != null && popupDialog.isShowing()) {
            layout_bg.clearAnimation();
            circleView.clearAnimation();
            popupDialog.dismiss();
            isShow = false;
        } else {
            return;
        }
    }
}
