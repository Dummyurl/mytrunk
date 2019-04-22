package com.car.portal.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.view.PassEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PasswordcheckActivity extends AppCompatActivity {

    BottomSheetBehavior bottomSheetBehavior;
    @BindView(R.id.pass_edit)
    PassEditView passEdit;
    @BindView(R.id.Line_bom)
    LinearLayout LineBom;
    @BindView(R.id.text_num_1)
    TextView textNum1;
    @BindView(R.id.text_num_2)
    TextView textNum2;
    @BindView(R.id.text_num_3)
    TextView textNum3;
    @BindView(R.id.text_num_4)
    TextView textNum4;
    @BindView(R.id.text_num_5)
    TextView textNum5;
    @BindView(R.id.text_num_6)
    TextView textNum6;
    @BindView(R.id.text_num_7)
    TextView textNum7;
    @BindView(R.id.text_num_8)
    TextView textNum8;
    @BindView(R.id.text_num_9)
    TextView textNum9;
    @BindView(R.id.text_num_0)
    TextView textNum0;
    @BindView(R.id.text_num_d)
    RelativeLayout textNumD;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.line_pass_bottom)
    LinearLayout linePassBottom;
    boolean isshowPass = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordcheck);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.line_pass_bottom));
        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        passEdit.setInputFinishListener(new PassEditView.InputFinishListener() {
            @Override
            public void onFinish(String pwd) {
                Toast.makeText(PasswordcheckActivity.this, pwd, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.img_close,R.id.pass_edit, R.id.Line_bom, R.id.text_num_1, R.id.text_num_2, R.id.text_num_3, R.id.text_num_4, R.id.text_num_5, R.id.text_num_6, R.id.text_num_7, R.id.text_num_8, R.id.text_num_9, R.id.text_num_0, R.id.text_num_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pass_edit:
                isshowPass = true;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.Line_bom:
                isshowPass = false;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.text_num_1:
                passEdit.inputPassword(1);
                break;
            case R.id.text_num_2:
                passEdit.inputPassword(2);
                break;
            case R.id.text_num_3:
                passEdit.inputPassword(3);
                break;
            case R.id.text_num_4:
                passEdit.inputPassword(4);
                break;
            case R.id.text_num_5:
                passEdit.inputPassword(5);
                break;
            case R.id.text_num_6:
                passEdit.inputPassword(6);
                break;
            case R.id.text_num_7:
                passEdit.inputPassword(7);
                break;
            case R.id.text_num_8:
                passEdit.inputPassword(8);
                break;
            case R.id.text_num_9:
                passEdit.inputPassword(9);
                break;
            case R.id.text_num_0:
                passEdit.inputPassword(0);
                break;
            case R.id.text_num_d:
                passEdit.deletePassword();
                break;
            case R.id.img_close:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isshowPass){
                isshowPass = false;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
