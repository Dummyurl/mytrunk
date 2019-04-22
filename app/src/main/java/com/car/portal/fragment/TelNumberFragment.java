package com.car.portal.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.car.portal.R;

/**
 * Created by Administrator on 2018/7/4.
 */

public class TelNumberFragment extends DialogFragment {
    Dialog dialog;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.setContentView(R.layout.dialog_telnumber);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        //initListener();
        return dialog;
    }

}
