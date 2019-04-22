package com.car.portal.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.car.portal.R;
import com.car.portal.view.MyRadioGroup;

public class AlonecarlegnthFragment extends DialogFragment {
    Dialog dialog;
    private String carLength, carLenghtV,carLengthResult;
//    private EditText editText_carLength;
    public OnDialogListener mlistener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        initData();
        return dialog;
    }

    public static AlonecarlegnthFragment newInstance() {
        return new AlonecarlegnthFragment();
    }

    private void initData() {
        MyRadioGroup goodsType = dialog.findViewById(R.id.clType_Radio);
        goodsType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog
                        .findViewById(id);
                // 获取这个RadioButton的text内容
                carLength = choise.getText().toString();
                // 获取该货物的编号
                carLenghtV = choise.getHint().toString();
                mlistener.onDialogClick(carLength,carLenghtV,carLengthResult);
                dismiss();
            }
        });





        Button chooseCar = dialog.findViewById(R.id.alone_carType);
        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (editText_carLength.getText().toString() != null && !editText_carLength.getText().toString().equals(""))
//                {
//                    carLength = editText_carLength.getText().toString();
//                    carLenghtV = "13";
//                }

            }

        });

    }


    public interface OnDialogListener {
        void onDialogClick(String a,String b,String c);
    }
    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mlistener = dialogListener;

    }




    private void initView() {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.setContentView(R.layout.dialog_carlength_alone);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

//        editText_carLength = (EditText)dialog.findViewById(R.id.editText_carLength);
    }


    public void setnull(){
        carLength =null;
        carLenghtV =null;
        carLengthResult = null;
    }

}
