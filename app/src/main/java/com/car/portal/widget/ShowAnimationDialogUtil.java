package com.car.portal.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.car.portal.R;


public class ShowAnimationDialogUtil {

    /**
     * 显示从下至上弹出对话框
     *
     * @param activity
     * @param resId    自定义dialog资源文件id
     * @return
     */
    public static PickDialog showDialog(Activity activity, int resId,boolean isInit) {
        PickDialog dialog = new PickDialog(activity, R.style.PickDialog,
                resId);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.PickDialogAnimation); // 添加动画
        dialog.show();
        dialog.hide();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
    /**
     * 显示从下至上弹出对话框
     *
     * @param activity
     * @param resId    自定义dialog资源文件id
     * @return
     */
    public static PickDialog showDialog(Activity activity, int resId) {
        PickDialog dialog = new PickDialog(activity, R.style.PickDialog,
                resId);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.PickDialogAnimation); // 添加动画
        dialog.show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
    /**
     *
     * @param activity
     * @param resId
     * @param
     *            ，单位为dp
     * @return
     */
    public static PickDialog showDialog(Activity activity, int resId, int height) {
        PickDialog dialog = new PickDialog(activity, R.style.PickDialog,
                resId);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.PickDialogAnimation); // 添加动画
        dialog.show();

        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        lp.height = dip2px(activity, height);
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
