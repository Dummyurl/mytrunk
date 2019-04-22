package com.car.portal.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.application.AppConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Company;
import com.car.portal.entity.PortalDriver;
import com.car.portal.entity.Qrcodebeen;
import com.car.portal.entity.ReChangeData;
import com.car.portal.entity.UserDetail;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.service.DriverService;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.view.DividerItemDecoration;
import com.car.portal.wxapi.WXShare;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrcodeActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.img_qrcode_user_hp)
    RoundedImageView imgUserHp;
    @BindView(R.id.it_qrcode_user_name)
    TextView it_user_name;
    @BindView(R.id.image_user_qrcode)
    ImageView imageUserQrcode;
    @BindView(R.id.tv_qrcode_hint)
    TextView tvQrcodeHint;
    @BindView(R.id.wx_sharp)
    TextView wxSharp;
    private UserDetail userDetail;
    private DriverService driverService;
    private PortalDriver driver;
    private String url;
    private MyHttpUtil util;
    private List<ReChangeData> list;
    private Qrcodebeen qrcodebeen;
    SharedPreferences sp;
    String imgurl, imgQr,sharetitle;
    private WXShare wxShare;
    private Dialog bottomDialog;

    private HttpCallBack getInfoBack = new HttpCallBack(this) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                driver = (PortalDriver) objects[0];
                userDetail = (UserDetail) objects[1];
                if (userDetail != null) {
                    getQrcode(userDetail.getUid());
                    url = driverService.getServer() + userDetail.getPersonImage();
                    if (url != null) {
                        Glide.with(QrcodeActivity.this).load(url).into(imgUserHp);
                    }
                    getcompany(userDetail.getTel());
                }
            }
        }

        @Override
        public void onError(Object... objects) {
            super.onError(objects);
            Glide.with(QrcodeActivity.this)
                    .load(imgQr)
                    .into(imageUserQrcode);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        sp = getSharedPreferences("SP", MODE_PRIVATE);
        imgQr = sp.getString("imgurl", "-1");
        initView();
        initdata();
    }

    private void initdata() {
        wxShare = new WXShare(this);
        if (driverService == null) {
            driverService = new DriverService(this);
            driverService.getDriverInfo(getInfoBack);
        }
    }

    private void getQrcode(int uid) {
        util = new MyHttpUtil(this);
        String url = util.getUrl(R.string.findMyQrLimitCode) + "?uid=" + uid;
        XUtil.Post(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                qrcodebeen = new Gson().fromJson(s, Qrcodebeen.class);
                imgurl = AppConfig.WX_QR_CODE_URL + qrcodebeen.getData();
                if (qrcodebeen.getResult() == 1) {
                    Glide.with(QrcodeActivity.this).load(imgurl).skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageUserQrcode);
                    if (!imgurl.equals(imgQr)) {
                        Log.d("succ", "下载二维码");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FutureTarget<File> target = Glide.with(getApplicationContext())
                                            .load(imgurl)
                                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                    target.get();
                                    SharedPreferences.Editor e = sp.edit();
                                    e.putString("imgurl", imgurl);
                                    e.commit();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else if (qrcodebeen.getResult() == -1) {
                    Glide.with(QrcodeActivity.this).load(R.drawable.qrcode_error).into(imageUserQrcode);
                    tvQrcodeHint.setText("您还未绑定推广二维码！");
                } else if (qrcodebeen.getResult() == 0) {
                    Glide.with(QrcodeActivity.this).load(R.drawable.qrcode_error).into(imageUserQrcode);
                    tvQrcodeHint.setText("无法获得您的登录信息，请重新登录！");
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }
        });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("推广二维码");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

    }

    /**
     *
     *@作者 Administrator
     *@时间 2019/3/6 0006 下午 6:02
     *   获得用户所在的公司,如果用户还没加入公司，需要进行判断
     */

    private void getcompany(final String phone) {
        util = new MyHttpUtil(QrcodeActivity.this);
        String url = util.getUrl(R.string.url_findCompanyByPhone) + "?phone=" + phone;
        Log.d("url", url);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<Object>>() {

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onSuccess(BaseEntity baseEntity) {
                if (baseEntity.getResult() == 1) {
                    final Company company = new Company();
                    LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) baseEntity.getData(), company);
                    String name ;
                    if(company.getName().equals("")){
                        name = userDetail.getCname();
                    }else {
                        name = company.getName()+"-"+userDetail.getCname();
                    }
                    it_user_name.setText(name);
                    sharetitle =name;
                }
            }

        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        wxShare.register();
    }

    @Override
    protected void onDestroy() {
        wxShare.unregister();
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    /**
     *
     *@作者 Administrator
     *@时间 2019/3/6 0006 下午 5:44
     *  显示底部的列表
     */
    private void showBottomDialog(final List<String> menuList) {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        bottomDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_menu, null);
        final RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        BottomDialogMenuAdapter dialogMenuAdapter = new BottomDialogMenuAdapter(R.layout.item_bottom_dialog_menu, menuList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_dis_button, null);
        dialogMenuAdapter.setFooterView(view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager));
        dialogMenuAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(dialogMenuAdapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

/**
 *
 *@作者 Administrator
 *@时间 2019/3/6 0006 下午 5:45
 *  要调用此方法首先得继承BaseQuickAdapter的点击接口
 */
    @OnClick(R.id.wx_sharp)
    public void onViewClicked() {
        List list=new ArrayList();
        list.add("微信好友");
        list.add("朋友圈");
        list.add("微信收藏");
        list.add("复制连接");
        showBottomDialog(list);

    }


    /**
     *
     *@作者 Administrator
     *@时间 2019/3/6 0006 下午 5:46
     *   根据flag的不同来判断好友、朋友圈还是收藏、
     *
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position){
            case 0:
                wxShare.shareUrl(0,getApplicationContext(),imgurl,sharetitle,"来自"+sharetitle+"的推广二维码，请打开后长按二维码，选择识别图中二维码关注公众号进行用户注册");
                bottomDialog.dismiss();
                break;
            case 1:
                wxShare.shareUrl(1,getApplicationContext(),imgurl,sharetitle,"来自"+sharetitle+"的推广二维码，请打开后长按二维码，选择识别图中二维码关注公众号进行用户注册");
                bottomDialog.dismiss();
                break;
            case 2:
                wxShare.shareUrl(2,getApplicationContext(),imgurl,sharetitle,"来自"+sharetitle+"的推广二维码，请打开后长按二维码，选择识别图中二维码关注公众号进行用户注册");
                bottomDialog.dismiss();
                break;
            case 3:
                ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("url", imgurl);
                clip.setPrimaryClip(mClipData);
                Toast.makeText(this, "复制成功，请粘贴到浏览器打开", Toast.LENGTH_SHORT).show();
                bottomDialog.dismiss();
                break;
        }
    }
}
