package com.car.portal.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.car.portal.entity.ApplicationConfig;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by arldr on 2018/2/13.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler  {
    private IWXAPI api;
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册API
        api = WXAPIFactory.createWXAPI(this, ApplicationConfig.getIntance().getWxAppId(),true);
        api.handleIntent(getIntent(), this);
        WXShare share = new WXShare(this);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtil.show("取消授权！", WXEntryActivity.this);
                finish();
                break;
            case  BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
                        // 获取微信传回的code
                        String code = newResp.code;
                            LogUtils.e("WXEntry", "    code：" + code);
                            Intent intent = new Intent();
                            intent.setAction("com.car.portal.wxAuthFinish");
                            intent.putExtra("code", code);
                            sendBroadcast(intent);
                            finish();
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        Log.d("WXEntry","ss");
                        finish();
                        break;
                }
                break;

        }

//        if (baseResp instanceof SendAuth.Resp) {
//            LogUtils.e("WXEntry", "           进如if" );
//            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
//            // 获取微信传回的code
//            String code = newResp.code;
//            if (code != null) {
//                LogUtils.e("WXEntry", "    code：" + code);
//                Intent intent = new Intent();
//                intent.setAction("com.car.portal.wxAuthFinish");
//                intent.putExtra("code", code);
//                sendBroadcast(intent);
//                finish();
//            } else {
//                ToastUtil.show("取消授权！", WXEntryActivity.this);
//                finish();
//            }
//        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}

