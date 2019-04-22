package com.car.portal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/3.
 */

public class RemarksFragment extends DialogFragment {
    Dialog dialog;
    private String loadType;// 装卸方式
    private String payType; //付款方式
    private String remakes; //备注
    private EditText remarkEditText; //备注
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.setContentView(R.layout.dialog_remarks);
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


    public void initListener(){

        // 装卸方式（多选）
        MyRadioGroup myRadioGroup = (MyRadioGroup) dialog.findViewById(R.id.loadingType);
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog
                        .findViewById(id);
                // 获取这个RadioButton的text内容
                loadType = choise.getText().toString();
            }
        });

        // 付款方式
        RadioGroup payTypeGroup = (RadioGroup) dialog.findViewById(R.id.payType);
        payTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog.findViewById(id);
                // 获取这个RadioButton的text内容
                payType = choise.getText().toString();
            }
        });

        // 备注
        RadioGroup remarkTypeGroup = (RadioGroup) dialog.findViewById(R.id.remarkType);
        remarkTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) dialog.findViewById(id);
                // 获取这个RadioButton的text内容
                remakes = choise.getText().toString();
            }
        });

        remarkEditText = (EditText) dialog.findViewById(R.id.remark_text);
        // 确定信息放回
        Button remarkChoosebutton = (Button) dialog.findViewById(R.id.choose_remark);
        remarkChoosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                if (getTargetFragment()== null){
                    return;
                }
                if (remarkEditText.getText().toString() != null && !remarkEditText.getText().toString().equals(""))
                {
                    remakes = (remakes == null) ? remarkEditText.getText().toString() :remakes + remarkEditText.getText().toString();
                }
                HashMap<String, String> map = new HashMap<>();
                if (loadType!=null && !loadType.equals(""))
                { map.put("loadType", loadType);}
                if (payType!=null && !payType.toString().equals(""))
                { map.put("payType", payType.toString());}
                if (remakes !=null && !remakes.toString().equals(""))
                {   map.put("remakes", remakes.toString());}

                SerializableHashMap myMap=new SerializableHashMap();
                myMap.setMap(map);//将hashmap数据添加到封装的myMap中
                Bundle bundle=new Bundle();
                bundle.putSerializable("object", myMap);
                intent.putExtras(bundle);

                getTargetFragment().onActivityResult(DeliverFragment.REMARKTYPE_CODE, Activity.RESULT_OK, intent);

            }
        });
    }

}


class SerializableHashMap implements Serializable {
    private HashMap map;
    public HashMap getMap() {
        return map;
    }
    public void setMap(HashMap map) {
        this.map= map;
        }
}