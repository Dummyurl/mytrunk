package com.car.portal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.car.portal.R;
import com.car.portal.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class CarlegnthFragment extends android.support.v4.app.DialogFragment {
    Dialog dialog;
    private String useCarType; //用车类型选择
    private String carLength;  //车长选择
    private String carType;    //车型选择
    private String carLenghtV;//车长类型ID集合
    private String carLengthResult ;   //显示在TextView里面的车长类型
    private String bodyTypeOptionV = null;//车型选择编号
    private EditText editText_carLength;//输入其他车长类型
    private CheckBox clType1 ,clType2,clType3,clType4,clType5,clType6,clType7,clType8,clType9,clType10
            ,clType11,clType12;

    private List<CheckBox> carLengthList = new ArrayList<CheckBox>();
    private CheckBox carType1 ,carType2,carType3,carType4,carType5,carType6,carType7,carType8,carType9
            ,carType10;
    private List<CheckBox> carTypeList = new ArrayList<CheckBox>();


    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.setContentView(R.layout.dialog_carlength);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        initView();
        initListener();
        return dialog;
    }

    public void initView(){
        clType1 = (CheckBox) dialog.findViewById(R.id.clType1);
        clType2 = (CheckBox) dialog.findViewById(R.id.clType2);
        clType3 = (CheckBox) dialog.findViewById(R.id.clType3);
        clType4 = (CheckBox) dialog.findViewById(R.id.clType4);
        clType5 = (CheckBox) dialog.findViewById(R.id.clType5);
        clType6 = (CheckBox) dialog.findViewById(R.id.clType6);
        clType7 = (CheckBox) dialog.findViewById(R.id.clType7);
        clType8 = (CheckBox) dialog.findViewById(R.id.clType8);
        clType9 = (CheckBox) dialog.findViewById(R.id.clType9);
        clType10  = (CheckBox) dialog.findViewById(R.id.clType10);
        clType11  = (CheckBox) dialog.findViewById(R.id.clType11);
        clType12  = (CheckBox) dialog.findViewById(R.id.clType12);
        carLengthList.add(clType1);
        carLengthList.add(clType2);
        carLengthList.add(clType3);
        carLengthList.add(clType4);
        carLengthList.add(clType5);
        carLengthList.add(clType6);
        carLengthList.add(clType7);
        carLengthList.add(clType8);
        carLengthList.add(clType9);
        carLengthList.add(clType10);
        carLengthList.add(clType11);
        carLengthList.add(clType12);

        carType1 = (CheckBox) dialog.findViewById(R.id.carType1);
        carType2 = (CheckBox) dialog.findViewById(R.id.carType2);
        carType3 = (CheckBox) dialog.findViewById(R.id.carType3);
        carType4 = (CheckBox) dialog.findViewById(R.id.carType4);
        carType5 = (CheckBox) dialog.findViewById(R.id.carType5);
        carType6 = (CheckBox) dialog.findViewById(R.id.carType6);
        carType7 = (CheckBox) dialog.findViewById(R.id.carType7);
        carType8 = (CheckBox) dialog.findViewById(R.id.carType8);
        carType9 = (CheckBox) dialog.findViewById(R.id.carType9);
        carType10 = (CheckBox) dialog.findViewById(R.id.carType10);

        carTypeList.add(carType1);
        carTypeList.add(carType2);
        carTypeList.add(carType3);
        carTypeList.add(carType4);
        carTypeList.add(carType5);
        carTypeList.add(carType6);
        carTypeList.add(carType7);
        carTypeList.add(carType8);
        carTypeList.add(carType9);
        carTypeList.add(carType10);

        editText_carLength = (EditText)dialog.findViewById(R.id.editText_carLength);

    }


    public void initListener(){
        // 用车类型
        final RadioGroup useCarTypeGroup = (RadioGroup) dialog.findViewById(R.id.userCarType);
        useCarTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog
                        .findViewById(id);
                // 获取这个RadioButton的text内容
                useCarType = choise.getText().toString();
            }
        });


        // 确定(数据返回)
        Button chooseCar = (Button) dialog.findViewById(R.id.choose_carType);
        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                if (getTargetFragment()== null){
                    return;
                }
                //遍历集合中的checkBox,判断是否选择，获取选中的文本
                for (CheckBox checkbox : carLengthList) {
                    if (checkbox.isChecked()) {
                        String value = checkbox.getText().toString();
                        String code = checkbox.getHint().toString();
                        carLength = (carLength == null) ? value : carLength + "," + value;
                        carLenghtV = (carLenghtV==null) ? code  : carLenghtV +","+code;
                        carLengthResult = (carLengthResult==null) ? value+"米"  : carLengthResult +","+value + "米";
                    }
                }

                //遍历集合中的checkBox,判断是否选择，获取选中的文本
                for (CheckBox checkbox : carTypeList) {
                    if (checkbox.isChecked()){
                        String value = checkbox.getText().toString();
                        String code = checkbox.getHint().toString();
                        carType = (carType==null) ? value : carType+","+value;
                        bodyTypeOptionV = (bodyTypeOptionV==null)? code: bodyTypeOptionV +","+code;
                    }
                }

                if(editText_carLength.getText().toString() !=null && !editText_carLength.getText().toString().equals(""))
                {
                    String value = editText_carLength.getText().toString();

                    //遍历集合中的checkBox,判断是否选择，获取选中的文本
                    for (CheckBox checkbox : carLengthList) {
                        String checkBoxValue = checkbox.getText().toString();
                        if (checkBoxValue.equals(value))
                        {
                            ToastUtil.show("该长度已经存在,请点击选择",getActivity());
                            setnull();
                            return;
                        }
                    }
                    carLength = (carLength == null) ? value : carLength + "," + value;
                    carLenghtV = (carLenghtV==null) ? "11"  : carLenghtV +","+"11";
                    carLengthResult = (carLengthResult==null) ? value+"米" : carLengthResult +","+value +"米";

                }

                // 用车类型三项都要至少选择一项
                if (useCarType== null   || carLength== null  || carType== null )
                {
                    ToastUtil.show("三项都至少选择一项", getActivity());
                    setnull();
                    return;
                }
                // 用车类型三项都要至少选择一项
                if (useCarType.equals("")  || carLength.equals("") || carType.equals(""))
                {
                    ToastUtil.show("三项都至少选择一项", getActivity());
                    setnull();
                    return;
                }

                intent.putExtra("usecarType",useCarType); //用车类型
                intent.putExtra("carLength",carLength); //车长类型
                intent.putExtra("carType",carType); //车型类型
                intent.putExtra("bodyTypeOptionV",bodyTypeOptionV); //车型类型ID
                intent.putExtra("carLenghtV",carLenghtV); //车长类型ID
                intent.putExtra("carLengthResult",carLengthResult); //车长显示结果

                getTargetFragment().onActivityResult(DeliverFragment.CARTYPE_CODE, Activity.RESULT_OK, intent);

            }
        });

    }

    //  设置回初始值
    public void setnull(){
        carLength =null;
        carType =null;
        carLenghtV =null;
        carLengthResult = null;
        bodyTypeOptionV =null;
    }

}
