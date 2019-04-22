package com.car.portal.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.car.portal.R;
import com.car.portal.activity.AboutActivity;
import com.car.portal.activity.ChangePassActivity;
import com.car.portal.activity.CompanyInfo;
import com.car.portal.activity.DriverAuthActivity;
import com.car.portal.activity.DriverInformActivity;
import com.car.portal.activity.DriverReChangeActivity;
import com.car.portal.activity.LoginActivity;
import com.car.portal.activity.MainActivity;
import com.car.portal.activity.MybankcardActivity;
import com.car.portal.activity.ProcommissActivty;
import com.car.portal.activity.QrcodeActivity;
import com.car.portal.activity.Wxautherization;
import com.car.portal.activity.service.MainService;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Company;
import com.car.portal.entity.MainscheduleEntity;
import com.car.portal.entity.Message;
import com.car.portal.entity.PortalDriver;
import com.car.portal.entity.User;
import com.car.portal.entity.UserDetail;
import com.car.portal.entity.VersionInfo;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.SessionStore;
import com.car.portal.http.XUtil;
import com.car.portal.model.MainscheduleModel;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.util.UpdateAppDialog;
import com.car.portal.view.BasegetdateFragment;
import com.car.portal.view.ICallback;
import com.car.portal.widget.PickDialog;
import com.car.portal.widget.ShowAnimationDialogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.mobiwise.materialintro.MaterialIntroConfiguration;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;


/**
 * Autor:敏捷科技 2016/03/17
 */
@SuppressLint("InflateParams")
public class DriverPersonFragment extends BasegetdateFragment {

    @ViewInject(R.id.driver_header)
    private ImageView header;
    @ViewInject(R.id.driver_username)
    private TextView username;
    @ViewInject(R.id.sum_of_money)
    private TextView sum_of_money;
    @ViewInject(R.id.driver_tel)
    private TextView phone;
    @ViewInject(R.id.driver_info)
    private LinearLayout userInfo;
    @ViewInject(R.id.push_sale)
    private LinearLayout mPushsale;
    @ViewInject(R.id.driver_account_rechange)
    private LinearLayout account_rechange;

    @ViewInject(R.id.driver_password)
    private LinearLayout pass;
    @ViewInject(R.id.company_info)
    private LinearLayout company_info;//单位资料
    @ViewInject(R.id.driver_check)
    private LinearLayout check;
    @ViewInject(R.id.driver_credit_card)
    private LinearLayout creditCard;
    @ViewInject(R.id.driver_aboutUs)
    private LinearLayout aboutus;
    @ViewInject(R.id.driver_checkUpdate)
    private LinearLayout updateLay;
    @ViewInject(R.id.driver_hasNewVersion)
    private View hasVersion;
    @ViewInject(R.id.driver_loginout)
    private Button logout;
    @ViewInject(R.id.linear_user)
    private RelativeLayout linear_user;
    @ViewInject(R.id.driver_bankcard)
    private LinearLayout driverbankcard;
    @ViewInject(R.id.wx_binding)
    private LinearLayout driver_wxbinding;
    @ViewInject(R.id.txt_wx_binding)
    private TextView txt_wx_binding;
    @ViewInject(R.id.txt_referrer_binding)
    private TextView txt_ref_binding;
    @ViewInject(R.id.driver_binding_referrer)
    private LinearLayout driver_binding_referrer;
    private User user;
    private UserService service;
    private DriverService driverService;
    private PortalDriver driver;
    private UserDetail userDetail;
    private MyHttpUtil util;
    private View currentImg;

    private Company company; //公司信息
    //2019
    //    拍照方式
    private String CaptureMode;
    //   图片路径
    private String imagePath;

    //    用户头像File
    private File personImage;
    //    用户头像
    private String personImageFileName;
    //    拍照
    //TakePhoto
    private TakePhoto takePhoto;
    String url = "";
    boolean istrue = false,isbinding = false;
    private View driver_user;
    private CropOptions cropOptions;//裁剪参数
    private CompressConfig compressConfig; //压缩参数
    private Uri imageUri;//图片保存路径\
    private Picasso picasso;
    private Map<String, File> params = new HashMap<String, File>();
    private List<File> fs = new ArrayList<File>();
    private MaterialIntroConfiguration config;
    private int REQUESTCODE =1000;
    private boolean isShowActivity = false;
    private HttpCallBack getInfoBack = new HttpCallBack(getActivity()) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                driver = (PortalDriver) objects[0];
                userDetail = (UserDetail) objects[1];
                if (userDetail != null) {
                    sum_of_money.setText(userDetail.getSumOfMoney());
                    username.setText(userDetail.getCname());
                    phone.setText(userDetail.getTel());
                    initImg();
                } else {
                    sum_of_money.setText("0.00");
                }
                if (isShowActivity) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new MainscheduleModel().ischeckuser(getActivity(), driver, userDetail, new ICallback<MainscheduleEntity>() {
                                @Override
                                public void onSucceed(MainscheduleEntity data) {
                                    if (data.isCompany_info() == true && data.isDriver_check() == true && data.isDriver_info() == true && data.isHeadImg() == true) {
                                        return;
                                    } else {
                                        isShowActivity=false;
                                        MainscheduleFragment fragment = MainscheduleFragment.newInstance(data);
                                        fragment.show(getFragmentManager(), "myAlert");
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                }
                            });
                        }
                    }).start();
                }
            }
        }

        @Override
        public void onError(Object... objects) {
            super.onError(objects);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverService = new DriverService(getActivity());
        service = new UserService(getActivity());
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (driver_user==null){
            driver_user = inflater.inflate(R.layout.driver_user, null);
            x.view().inject(DriverPersonFragment.this, driver_user);
            if (service == null) {
                service = new UserService(getContext());
            }
            user = service.getLoginUser();
            getDriverInfo();
            init();
        }else {
            ViewGroup parent = (ViewGroup) driver_user.getParent();
            if (parent != null){
                parent.removeView(driver_user);
            }
        }
        return driver_user;
    }

    private void initImg() {
        url =  driverService.getServer()+userDetail.getPersonImage();
        if(url!=""&&istrue==false&&userDetail!=null && userDetail.getPersonImage()!=null && !"".equals(userDetail.getPersonImage())) {
            Glide.with(getActivity()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(header);
            istrue = true;
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void getData() {
        isShowActivity = true;
    }

    private void init() {
//        header.setImageResource(R.drawable.head);

        if (user != null) {
            username.setText(user.getCname()==null|| "".equals(user.getCname())?user.getUsername():user.getCname());
            phone.setText(user.getPhone());
            if(user.getWx_openId()!=null&&!"".equals(user.getWx_openId())){
                txt_wx_binding.setText("已绑定");
            }else {
                txt_wx_binding.setText("未绑定");
            }

            if(user.getInvtier()!=null&&!"".equals(user.getInvtier())){
                if(!"999".equals(user.getInvtier())){
                    txt_ref_binding.setText("已绑定");
                }
            }
        }
        header.setOnClickListener(listener);
        pass.setOnClickListener(listener);
        check.setOnClickListener(listener);
        userInfo.setOnClickListener(listener);
        logout.setOnClickListener(listener);
        creditCard.setOnClickListener(listener);
        aboutus.setOnClickListener(listener);
        updateLay.setOnClickListener(updateListener);
        company_info.setOnClickListener(listener);
        account_rechange.setOnClickListener(listener);
        mPushsale.setOnClickListener(listener);
        linear_user.setOnClickListener(listener);
        driverbankcard.setOnClickListener(listener);
        driver_wxbinding.setOnClickListener(listener);
        driver_binding_referrer.setOnClickListener(listener);
        initIsUpdate();
        config = new MaterialIntroConfiguration();
        config.setFocusGravity(FocusGravity.CENTER);
        config.setFocusType(Focus.MINIMUM);
        config.setDelayMillis(500);

//
//        new MaterialIntroView.Builder(getActivity())
//                .enableDotAnimation(true)
//                .enableIcon(false)
//                .setConfiguration(config)
//                .enableFadeAnimation(true)
//                .performClick(false)
//                .setListener(new MaterialIntroListener() {
//                    @Override
//                    public void onUserClicked(String s) {
//
//                        new MaterialIntroView.Builder(getActivity())
//                                .enableDotAnimation(true)
//                                .enableIcon(false)
//                                .setConfiguration(config)
//                                .performClick(false)
//                                .setListener(new MaterialIntroListener() {
//                                    @Override
//                                    public void onUserClicked(String s) {
//                                        new MaterialIntroView.Builder(getActivity())
//                                                .enableDotAnimation(true)
//                                                .enableIcon(false)
//                                                .setConfiguration(config)
//                                                .performClick(false)
//                                                .setListener(new MaterialIntroListener() {
//                                                    @Override
//                                                    public void onUserClicked(String s) {
//                                                        new MaterialIntroView.Builder(getActivity())
//                                                                .enableDotAnimation(true)
//                                                                .enableIcon(false)
//                                                                .setConfiguration(config)
//                                                                .performClick(false)
//                                                                .setListener(new MaterialIntroListener() {
//                                                                    @Override
//                                                                    public void onUserClicked(String s) {
//
//                                                                    }
//                                                                })
//                                                                .setInfoText("最后，完善您的个人信息，好啦，当您完成以上步骤，您就可以尽情享受货运的便利啦")
//                                                                .setTarget(userInfo)
//                                                                .setUsageId("mian5")
//                                                                .show();
//                                                    }
//                                                })
//                                                .setInfoText("点击‘单位资料’，完善您的公司信息")
//                                                .setTarget(company_info)
//                                                .setUsageId("mian4")
//                                                .show();
//                                    }
//                                })
//                                .setInfoText("点击‘资料认证’，上传您的公司相关图片")
//                                .setTarget(check)
//                                .setUsageId("mian3")
//                                .show();
//                    }
//                })
//                .setInfoText("请先完善您的个人信息，在这里，您可以更换您的头像")
//                .setTarget(header)
//                .setUsageId("mian2")
//                .show();

    }

    //获取司机跟公司信息
    public void getDriverInfo() {
        if(driverService==null) driverService = new DriverService(getContext());
        driverService.getDriverInfo(getInfoBack);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent;
            Bundle bundle;
            switch (id) {
                case R.id.driver_header:
//                    更换头像
                    currentImg=v;
                      CaptureMode();
                    break;
                case R.id.driver_info:
                    if (driver != null) {
                        intent = new Intent(getActivity(), DriverInformActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable("driver", driver);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,REQUESTCODE);
                    }
                    break;
                case R.id.driver_password:
                    if (user == null)
                        user = service.getSavedUser();
                    intent = new Intent(getActivity(), ChangePassActivity.class);
                    intent.putExtra("phone", user.getPhone());
                    startActivity(intent);
                    break;
                case R.id.driver_check:
                    intent = new Intent(getActivity(), DriverAuthActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("driver", driver);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.driver_credit_card:
                    // TODO
                    break;
                case R.id.driver_loginout:
                    service.logout(new HttpCallBack(getActivity()) {
                        @Override
                        public void onSuccess(Object... objects) {

                        }
                    });
                    try {
                        //SessionStore.resetSessionId();
                        NotificationManager manger = (NotificationManager) getActivity()
                                .getSystemService(Activity.NOTIFICATION_SERVICE);
                        manger.cancel(MainService.NOTIFICAT_ID);
                    } catch (Exception e) {}
                   /* Intent intent2 = new Intent(getActivity(), MainService.class);
                    getActivity().stopService(intent2);
*/
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case R.id.driver_aboutUs:
                    intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.company_info: //上传单位资料
                    intent = new Intent(getActivity(), CompanyInfo.class);
                    bundle = new Bundle();
                    bundle.putParcelable("company",company);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.driver_account_rechange:
                    intent = new Intent(getActivity(), DriverReChangeActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.push_sale:
                    intent=new Intent(getActivity(),ProcommissActivty.class);
                    startActivity(intent);
                    break;
                case R.id.linear_user:
                    intent=new Intent(getActivity(),QrcodeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.driver_bankcard:
                    intent = new Intent(getActivity(),MybankcardActivity.class);
                    startActivity(intent);
                    break;
                case R.id.wx_binding:
                    if(user.getWx_openId()==null||"".equals(user.getWx_openId())){
                        if(isbinding == false) {
                            intent = new Intent(getActivity(), Wxautherization.class);
                            startActivityForResult(intent, REQUESTCODE);
                        }
                    }
                    break;
                case R.id.driver_binding_referrer:
                    if(user.getInvtier()==null||"".equals(user.getInvtier())) {
                        new MaterialDialog.Builder(getActivity())
                                .title("提示")
                                .content("检测到您未绑定推荐人，是否绑定推荐人电话号码？")
                                .contentColor(getActivity().getResources().getColor(R.color.title_black))
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        new MaterialDialog.Builder(getActivity()).title("绑定推荐人")
                                                .iconRes(R.drawable.ic_launcher)
                                                .content("请输入推荐人手机号码")
                                                .widgetColorRes(R.color.colorPrimary)
                                                .inputRangeRes(0, 15, R.color.light_red)
                                                .inputType(InputType.TYPE_CLASS_PHONE)
                                                .input("电话号码", null, new MaterialDialog.InputCallback() {
                                                    @Override
                                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                        if(input.toString().equals(user.getPhone())){
                                                            Toast.makeText(getActivity(), "不能绑定自己为推荐人哦", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        getreferrer(input.toString());
                                                    }
                                                })
                                                .positiveText("查找")
                                                .show();
                                    }
                                })
                                .show();

                    }
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE){
            if(resultCode==Activity.RESULT_OK){
                txt_wx_binding.setText("已绑定");
                isbinding = true;
            }else if(5==resultCode){
                driverService.getDriverInfo(getInfoBack);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private View.OnClickListener updateListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            VersionInfo info = service.getVersionInfo();
            if (MyApplication.serverVersion > MyApplication.localVersion && info != null) {
                hasNewVersionDo(info.getDownUrl());
            } else {
                service.getCurrentVersion(MyApplication.localVersion, new MyCallBack<BaseEntity>() {

                    public void onError(Throwable arg0, boolean arg1) {
                        BaseUtil.writeFile("DriverPersonFragment", arg0);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(BaseEntity arg0) {
                        if (arg0.getResult() == 1) {
                            VersionInfo info = new VersionInfo();
                            LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) arg0
                                    .getData(), info);
                            MyApplication.serverVersion = info.getVersion();
                            if (MyApplication.localVersion < info.getVersion()) {
                                hasNewVersionDo(info.getDownUrl());
                            }else {
                                Toast.makeText(getActivity(), "已是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
            LogUtils.e("DriverPersonFragment", "     version:" + info);

        }
	};

	private void hasNewVersionDo(final String url) {
        new MaterialDialog.Builder(getActivity())
                .title("版本更新")
                .content("发现新版本,建议立即更新使用")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BaseUtil.Downloadapk(getActivity(),url);
                    }
                }).show();
	}


	private void initIsUpdate() {
		if (MyApplication.serverVersion > MyApplication.localVersion) {
			hasVersion.setVisibility(View.VISIBLE);
		} else {
			hasVersion.setVisibility(View.GONE);
		}
	}


    //拍照
    private void CaptureMode() {
        final PickDialog selectPhotoDialog = ShowAnimationDialogUtil.showDialog(getActivity(),
                R.layout.dialog_generating_photos);
        View view = selectPhotoDialog.getView();
        TextView cameraTV = view.findViewById(R.id.cameraTV);
        TextView photoTV = view.findViewById(R.id.photoTV);
        TextView cancel_tv = view.findViewById(R.id.cancel_tv);
        cameraTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                CaptureMode = "1";
                // 3、调用拍照方法
                initPermission();
                imageUri = getImageCropUri();
                //拍照并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                selectPhotoDialog.dismiss();
            }
        });
        photoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片
                // 3、调用从图库选取图片方法

                CaptureMode = "2";
                initPermission();
                imageUri = getImageCropUri();
                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                selectPhotoDialog.dismiss();
            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                selectPhotoDialog.dismiss();
            }
        });
    }
//获取图片成功回调
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        personImageFileName=null;
        imagePath=null;
        if (result != null) {
            personImageFileName= result.getImage().getOriginalPath();

//            imagePath=result.getImage().getCompressPath();
            if (personImageFileName!=null){
                addParams(personImageFileName,header.getId());
            }
       }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
//        ToastUtil.show("操作被取消",getActivity());
        Log.d("DriverPersonFragment：", "takeCancel: 取消操作");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.e("DriverPersonFragment","takeFail:"+ msg);
    }


    /**
     * 上传图片网络请求
     */
    private HttpCallBack callBack = new HttpCallBack(getActivity()) {
        @Override
        public void onSuccess(Object... objects) {
            LogUtils.i("uploadFile", objects[0].toString());
            x.image().clearCacheFiles();
            x.image().clearMemCache();
            ToastUtil.show(objects[0].toString(),getActivity());
            ImageView img = (ImageView) currentImg;
            if (personImageFileName!=null){
                Picasso.get().load(Uri.fromFile(new File(personImageFileName))).networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(DensityUtil.dip2px(100), DensityUtil.dip2px(100)).centerCrop().into(img);
            }
            LogUtils.e("DriverAuth", "confirm finish");
            params.clear();
        }

        public void onFail(int result, String message, boolean show) {
            super.onFail(result, message, show);
            if (result != -2) {
                Log.i("上传失败了", "onFail: "+message);
            }
        }

        public void onError(Object... objects) {
            super.onError(objects);
            Log.i("有错误", "onError: "+objects.toString());
        }
    };

    private void addParams(final String path, final int vid) {
        new Thread() {
            @Override
            public void run() {
                personImage= new File(path);
                String key = null;
                switch (vid) {
                    case R.id.driver_header:
                        key = "personImage";
                        break;
                }
                if (personImage.length() > BaseUtil.MAX_SIZE) {
                    try {
                        personImage= BaseUtil.compressImage(path, "cache_" +personImage.getName());
                        fs.add(personImage);
                    } catch (IOException e) {
                        ToastUtil.show("图片缓存失败，请在权限管理设置读写权限", getActivity());
                        LogUtils.e("IMAGESIZE", personImage.getName() + "压缩失败：" + e.getMessage());
                    }
                }
                if (personImage != null) {
                    params.put(key, personImage);
                    if (params.size() == 1) {

                        if (personImageFileName != null) {
                            driverService.savePersonImgs(params, callBack);
                        }
                    }
                } else {
                    params.remove(key);
                }
            }
        }.start();
    }
    private void initPermission() {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }
    //权限申请
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                // TODO 相应代码。
            }
        }
        @Override
        public void onFailed ( int requestCode, List<String > deniedPermissions) {
            // 权限申请失败回调。


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {


                //提示语
                AndPermission.defaultSettingDialog(getActivity(), 103)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };
    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setOutputX(300).setOutputY(300).setWithOwnCrop(false).create();
        //设置压缩参数
        compressConfig=new CompressConfig.Builder().setMaxSize(400*1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig,true);//设置为需要压缩
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }



    private static class MyAsyncTask extends AsyncTask<String,Void,String> {
        private WeakReference<DriverPersonFragment> activityReference;

        MyAsyncTask(DriverPersonFragment driverPersonFragment) {
            activityReference = new WeakReference<>(driverPersonFragment);
        }

        @Override
        protected String doInBackground(final String... strings) {
            MyHttpUtil util = new MyHttpUtil(activityReference.get().getActivity());
            String url=util.getUrl(R.string.url_bindbyphone);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("invtierPhone", strings[0]);
            XUtil.Post(url, params,new MyCallBack<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> stringBaseEntity) {
                    if(stringBaseEntity.getResult()==1){
                        Toast.makeText(activityReference.get().getActivity(), stringBaseEntity.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    Toast.makeText(activityReference.get().getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DriverPersonFragment fragment = activityReference.get();
            if(fragment.getActivity()==null||fragment.getActivity().isFinishing()||fragment.getActivity().isDestroyed()){
                return ;
            }
            fragment.txt_ref_binding.setText(s);
            fragment.user.setInvtier(s);
        }
    }


    public void getreferrer(final String phone){
        util = new MyHttpUtil(getActivity());
        String url = util.getUrl(R.string.url_findCompanyByPhone) + "?phone=" + phone;
        Log.d("url", url);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<Object>>() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                if (baseEntity.getResult() == 1) {
                    final Company company = new Company();
                    LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) baseEntity.getData(), company);
                    if(company.getName()==null||"".equals(company.getName())){
                        Toast.makeText(getActivity(), "该用户未通过审核，无法绑定", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new MaterialDialog.Builder(getActivity()).title("查找成功")
                            .iconRes(R.drawable.ic_launcher)
                            .content(company.getName()+"  "+company.getAlias()+"\n"+company.getTel())
                            .contentColorRes(R.color.black)
                            .positiveText("申请")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    new MyAsyncTask(DriverPersonFragment.this).execute(phone);
                                }
                            })
                            .show();

                } else {
                    new MaterialDialog.Builder(getActivity()).title("查询失败")
                            .iconRes(R.drawable.ic_launcher)
                            .content("未查找到该用户，请确认手机号是否输入正确！")
                            .positiveText("确认")
                            .show();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError", arg0.toString());
                new MaterialDialog.Builder(getActivity()).title("查询失败")
                        .iconRes(R.drawable.ic_launcher)
                        .content("网络异常，请稍后重试")
                        .positiveText("确认")
                        .show();
            }
        });
    }

}
