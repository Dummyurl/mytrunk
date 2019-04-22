package com.car.portal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.view.MyRadioGroup;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class AlonegoodtypeFragment extends DialogFragment {
    private Dialog dialog;
    private String chooseTop;   //上面选择结果
    private String  result; // 选择货物结果
    private String  resultV ;// 选择货物的编号

    private List<String> strings;
    public OnDialogListener mlistener;

    protected static SharedPreferenceUtil shareUtil = SharedPreferenceUtil
            .getIntence();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.setContentView(R.layout.dialog_goodtype_alone);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        initListener();
        return dialog;
    }

    public interface OnDialogListener {
        void onDialogClick(String a,String b);
    }

    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mlistener = dialogListener;

    }



    public void initListener(){
        // 货物类型
        MyRadioGroup goodsType = (MyRadioGroup) dialog.findViewById(R.id.goodsType);
        goodsType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog
                        .findViewById(id);
                // 获取这个RadioButton的text内容
                chooseTop = choise.getText().toString();
                // 获取该货物的编号
                resultV = choise.getHint().toString();

                if (chooseTop!=null && !chooseTop.toString().equals(""))
                {result = (result == null) ? chooseTop : result+chooseTop;}

                mlistener.onDialogClick(result,resultV);
                dismiss();
            }
        });

//        // 确定选择
//        Button chooseButton = dialog.findViewById(R.id.choose_goodsType);
//        chooseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }
}
