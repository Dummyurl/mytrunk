package com.car.portal.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.WxPayResult;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private MyHttpUtil util;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        util = new MyHttpUtil(this);
        api = WXAPIFactory.createWXAPI(this, ApplicationConfig.getIntance().getWxAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:

                    //finish();
                    //Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    Map<String,Object>map=new HashMap<>();
                    map.put("out_trade_no", Hawk.get("out_trade_no"));
                    map.put("noncestr", Hawk.get("nonceStr"));
                    ObtainPayResult(map, new HttpCallBack(this) {
                        @Override
                        public void onSuccess(Object... objects) {
                            WxPayResult wxPayResult= (WxPayResult) objects[0];
                            Toast.makeText(WXPayEntryActivity.this, wxPayResult.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    break;
                case -1:
                     Toast.makeText(this, "支付失败-1", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                    Toast.makeText(this, "支付取消-2", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }

        } else {
            Toast.makeText(this, "暂时只支付微信支付", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void ObtainPayResult(Map<String,Object> map, final HttpCallBack back) {
        String url = util.getUrl(R.string.findPayOrderResultCompany);
        XUtil.Post(url, map, new MyCallBack<WxPayResult>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
                finish();
            }

            @Override
            public void onSuccess(WxPayResult arg0) {
                back.onSuccess(arg0);

            }



        });
    }

}