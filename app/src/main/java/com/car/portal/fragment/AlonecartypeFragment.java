package com.car.portal.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.car.portal.R;
import com.car.portal.view.MyRadioGroup;

public class AlonecartypeFragment extends android.support.v4.app.DialogFragment {
    Dialog dialog;
    private String cartype, cartypeV,carLengthResult;
    public AlonecarlegnthFragment.OnDialogListener mlistener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        initListener();
        return dialog;
    }

    public static AlonecartypeFragment newInstance() {
        return new AlonecartypeFragment();
    }

    private void initListener() {
        final MyRadioGroup carType = dialog.findViewById(R.id.carType);
        carType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog
                        .findViewById(id);
                // 获取这个RadioButton的text内容
                cartype = choise.getText().toString();
                // 获取该货物的编号
                cartypeV = choise.getHint().toString();
                mlistener.onDialogClick(cartype,cartypeV,carLengthResult);
                dismiss();
            }
        });
    }


    public interface OnDialogListener {
        void onDialogClick(String a,String b,String c);
    }
    public void setOnDialogListener(AlonecarlegnthFragment.OnDialogListener dialogListener){
        this.mlistener = dialogListener;

    }

    private void initView() {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.setContentView(R.layout.dialog_cartype_alone);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
    }
}
