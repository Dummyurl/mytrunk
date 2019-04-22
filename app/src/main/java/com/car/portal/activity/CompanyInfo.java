package com.car.portal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.City;
import com.car.portal.entity.Company;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.widget.PickDialog;
import com.car.portal.widget.ShowAnimationDialogUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/9.
 */

public class CompanyInfo extends TakePhotoActivity {

    @ViewInject(R.id.company_info_name)
    private EditText company_info_name;//单位名称

    @ViewInject(R.id.company_info_alias)
    private EditText company_info_alias;//单位简称

    @ViewInject(R.id.company_info_address)
    private EditText company_info_address;//单位地址

    @ViewInject(R.id.company_info_city)
    private TextView company_info_city;//公司地址

    @ViewInject(R.id.company_info_officetel)
    private EditText company_info_officetel;//固话

    @ViewInject(R.id.company_info_fax)
    private EditText company_info_fax;//传真号码

    @ViewInject(R.id.company_info_orgid)
    private EditText company_info_orgid;//统一信用号码

    @ViewInject(R.id.company_info_legaler)
    private EditText company_info_legaler;//负责人

    @ViewInject(R.id.company_info_legalertel)
    private EditText company_info_legalertel;//负责人电话

    @ViewInject(R.id.company_info_decs)
    private EditText company_info_decs;//简介

    @ViewInject(R.id.company_info_logo)
    private ImageView company_info_logo;//选择图片

    @ViewInject(R.id.company_info_update)
    private Button company_info_update;//更新按钮


    @ViewInject(R.id.img_found_return)
    private ImageView img_found_return;
    private Company company = new Company(); //公司信息
    private View currentImg;
    private File imgfile;//图片地址信息
    private boolean has_sub = false; //判断是否已经更新过一次信息
    private UserService userService;
    private DriverService driverService;
    private Company companyInfo ;
    private String logo; // 传过来的公司logo信息

    private int cityCode;// 公司地址编码，公司地址取TextView的值
    private String currentTime ;//当前时间
    // 图片保存信息
    private Map<String, File> params = new HashMap<String, File>();

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
    private CropOptions cropOptions;//裁剪参数
    private CompressConfig compressConfig; //压缩参数
    private Uri imageUri;//图片保存路径\
    private Picasso picasso;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);
        x.view().inject(this);
        userService = new UserService(this);
        driverService = new DriverService(this);
        currentTime = formatDate();
        initTextView();
        initListener();
        initData();
    }

    // 根据后台获取的公司信息来初始化TextView内容
    public void initTextView(){



        // 获取公司资料初始化
        userService.getMyCompany(new HttpCallBack(CompanyInfo.this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    companyInfo = (Company) objects[0];
                    //单位名称
                    company_info_name.setText(companyInfo.getName());
                    //单位简称
                    company_info_alias.setText(companyInfo.getAlias());
                    //单位地址
                    company_info_address.setText(companyInfo.getAddress());
                    //公司城市
                    company_info_city.setText(companyInfo.getCity());
                    //公司编号(//不需要显示,但是保存如果没有选择的时候需要一个参数来保存)
                    cityCode = companyInfo.getCityCode();
                    //固话
                    company_info_officetel.setText(companyInfo.getOffice_tel());
                    //传真号码
                    company_info_fax.setText(companyInfo.getFax());
                    //统一信用号码
                    company_info_orgid.setText(companyInfo.getOrg_id());
                    //负责人
                    company_info_legaler.setText(companyInfo.getLegaler());
                    //负责人电话
                    company_info_legalertel.setText(companyInfo.getTel());
                    //简介
                    company_info_decs.setText(companyInfo.getDecs());

                    // logo 照片
                    if(!StringUtil.isNullOrEmpty(companyInfo.getLogo())) {
                        String url = driverService.getServer() + companyInfo.getLogo();
                        driverService.loadDriverImg(company_info_logo, url,getApplicationContext());
                        company_info_logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        logo = companyInfo.getLogo();
                    }

                }
            }
        });
    }

    public void initListener()
    {
        //选择要上传的Logo图片
        company_info_logo.setOnClickListener(imgClickListener);
        // 更新公司资料
        company_info_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil shareUtil = SharedPreferenceUtil.getIntence();
                User u = shareUtil.getUserFromShared(MyApplication.getContext());
                if(u.getAuth()==0) {
                    if (has_sub) {
                        return;
                    } else {
                        // 更新单位资料齐全更新新
                        has_sub = true;
                        // 上传图片的接口
                        if (params != null) {
                            driverService.saveCompanyLogoImg(params, callBack);
                        }
                        if (isCanSend()) {
                            userService.saveCompanyInfo(company, new HttpCallBack(CompanyInfo.this) {
                                @Override
                                public void onSuccess(Object... objects) {
                                    ToastUtil.show("保存成功", CompanyInfo.this);
                                    CompanyInfo.this.finish();
                                    has_sub = false;
                                }

                                @Override
                                public void onError(Object... objects) {
                                    super.onError(objects);
                                    has_sub = false;
                                }
                            });
                        } else {
                            ToastUtil.show("请先完善您的信息", CompanyInfo.this);
                            has_sub = false;
                        }
                    }
                }else{
                    ToastUtil.show("资料审核了，不能变更，如更新需退出再次登录\n\n（请联系客服务人员）！", CompanyInfo.this);
                }
            }
        });

        // 地址TextView 显示跟发货一样的发货地点选择
        company_info_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDepart();
            }
        });
    }

    private HttpCallBack callBack = new HttpCallBack(CompanyInfo.this) {
        @Override
        public void onSuccess(Object... objects) {
            LogUtils.i("uploadFile", "success");
            x.image().clearCacheFiles();
            x.image().clearMemCache();
            has_sub = false;
            params.clear();
        }

        public void onFail(int result, String message, boolean show) {
            super.onFail(result, message, show);
            has_sub = false;
            ToastUtil.show("图片保存 失败",CompanyInfo.this);
        }

        public void onError(Object... objects) {
            super.onError(objects);
            ToastUtil.show("图片保存 发生错误",CompanyInfo.this);
            has_sub = false;
        }
    };

    /**
     * 得到当前时间
     * @return
     */
    public String formatDate(){
        String time="";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date(System.currentTimeMillis());
        time=simpleDateFormat.format(date);
        return time;
    }

    //判断输入框里面的信息是否已经输入完毕
    private Boolean isCanSend()
    {
        boolean isCansend = false;
//        if ((company_info_name!=null&&!company_info_name.getText().toString().equals(""))
//            &&(company_info_alias!=null&&!company_info_alias.getText().toString().equals(""))
//             &&(company_info_officetel!=null&&!company_info_officetel.getText().toString().equals(""))
//                &&(company_info_fax!=null&&!company_info_fax.getText().toString().equals(""))
//                &&(company_info_orgid!=null&&!company_info_orgid.getText().toString().equals(""))
//                &&(company_info_legaler!=null&&!company_info_legaler.getText().toString().equals(""))
//                &&(company_info_legalertel!=null&&!company_info_legalertel.getText().toString().equals(""))
//                &&(company_info_decs!=null&&!company_info_decs.getText().toString().equals(""))
//                &&(company_info_address!=null&&!company_info_address.getText().toString().equals(""))
//                &&(company_info_city!=null&&!company_info_city.getText().toString().equals(""))
//                &&(logo!=null || imgfile!=null) //logo信息取原来的值 或者是 选择的图片信息
//                )
//        {
            isCansend = true;
            company.setName(company_info_name.getText().toString());
            company.setAlias(company_info_alias.getText().toString());
            company.setAddress(company_info_address.getText().toString());
            company.setOffice_tel(company_info_officetel.getText().toString());
            company.setFax(company_info_fax.getText().toString());
            company.setOrg_id(company_info_orgid.getText().toString());
            company.setLegaler(company_info_legaler.getText().toString());
            company.setTel(company_info_legalertel.getText().toString());
            company.setDecs(company_info_decs.getText().toString());
            if(imgfile != null){
                company.setLogo(currentTime+imgfile.toString());
            }else if(logo != null)
            {
                company.setLogo(logo);
            }

            company.setCity(company_info_city.getText().toString());

            company.setCityCode(cityCode);

        return isCansend;
    }

    private static final int HANDMSG = 0x1345;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if(msg.what == HANDMSG) {
                String file = msg.getData().getString("file");
                ToastUtil.show(file + "压缩失败，无法上传该照片", CompanyInfo.this);
            }
        }
    };

    private View.OnClickListener imgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentImg = v;
//            Intent intent = new Intent();
//			/* 开启Pictures画面Type设定为image */
//            intent.setType("image/*");
//			/* 使用Intent.ACTION_GET_CONTENT这个Action */
//            intent.setAction(Intent.ACTION_PICK);
//			/* 取得相片后返回本画面 */
//            startActivityForResult(intent, 1);
            CaptureMode();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE) {
            City c = data.getParcelableExtra("city");
            if (c != null && c.getShortName() != null) {
                company_info_city.setText(c.getShortName());
            }
            cityCode = c.getCode();
        }
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            ContentResolver cr = this.getContentResolver();
//            try {
//                String [] proj={MediaStore.Images.Media.DATA, MediaStore.Images.Media.DESCRIPTION,MediaStore.Images.Media.DISPLAY_NAME};
//                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                final String path = cursor.getString(column_index);
//                cursor.close();
//                File file = new File(path);
//                final int vid = currentImg.getId();
//                final String fname = file.getName();
//                switch (vid) {
//                    case R.id.company_info_logo:
//                        params.put("drivingImg", file);
//                        imgfile = file;
//                        break;
//                }
//                if(file.length() > BaseUtil.MAX_SIZE ) {
//                    new Thread() {
//                        public void run() {
//                            File file1;
//                            String key = null;
//                            switch (vid) {
//                                case R.id.company_info_logo:
//                                    key = "drivingImg";
//                                    break;
//                            }
//                            try {
//                                file1 = BaseUtil.compressImage(path, "cache_" + fname);
//                                x.image().bind((ImageView) currentImg, file1.getPath(),
//                                        new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build());
//                                LogUtils.e("string", key);
//                                imgfile = file1;
//                                params.put(key, file1);
//                            } catch (IOException e) {
//                                System.out.println(e.getMessage());
//                                LogUtils.e("IMAGESIZE", fname + "压缩失败：" + e.getMessage());
//                                Bundle bundle = new Bundle();
//                                bundle.putString("file", fname);
//                                Message msg = new Message();
//                                handler.sendMessage(msg);
//                                imgfile = null;
//                                params.remove(key);
//                            }
//                        }
//                    }.start();
//                }
//                x.image().bind((ImageView) currentImg, file.getPath(),
//                        new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build());
//                LogUtils.e("IMAGESIZE", file.length() + "         after");
//            } catch (Exception e) {
//                LogUtils.e("Exception", e.getMessage(), e);
//                ToastUtil.show("图片选择失败", CompanyInfo.this);
//            }
//        }else if (requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE){
//            City c = data.getParcelableExtra("city");
//            if(c != null && c.getShortName() != null) {
//                company_info_city.setText(c.getShortName());
//            }
//            cityCode = c.getCode();
//        }
    }

    // 选择发货地点
    protected void selectDepart() {
        Intent intent = new Intent(CompanyInfo.this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 4);
        intent.putExtra("isMutiple", false);
        startActivityForResult(intent, AddOrderActivity.SELECT_CITY);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result != null) {
            personImageFileName = result.getImage().getOriginalPath();
            ImageView img = (ImageView) currentImg;
            Picasso.get().load(Uri.fromFile(new File(personImageFileName))).networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(DensityUtil.dip2px(210), DensityUtil.dip2px(210)).centerCrop().into(img);
                try {

                    File file = new File(personImageFileName);
                    final int vid = currentImg.getId();
                    final String fname = file.getName();
                    switch (vid) {
                        case R.id.company_info_logo:
                            params.put("drivingImg", file);
                            imgfile = file;
                            break;
                    }
                    if(file.length() > BaseUtil.MAX_SIZE ) {
                        new Thread() {
                            public void run() {
                                File file1;
                                String key = null;
                                switch (vid) {
                                    case R.id.company_info_logo:
                                        key = "drivingImg";
                                        break;
                                }
                                try {
                                    file1 = BaseUtil.compressImage(personImageFileName, "cache_" + fname);
                                    x.image().bind((ImageView) currentImg, file1.getPath(),
                                            new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build());
                                    LogUtils.e("string", key);
                                    imgfile = file1;
                                    params.put(key, file1);
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                    LogUtils.e("IMAGESIZE", fname + "压缩失败：" + e.getMessage());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("file", fname);
                                    Message msg = new Message();
                                    handler.sendMessage(msg);
                                    imgfile = null;
                                    params.remove(key);
                                }
                            }
                        }.start();
                    }
                    x.image().bind((ImageView) currentImg, file.getPath(),
                            new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build());
                    LogUtils.e("IMAGESIZE", file.length() + "         after");
                } catch (Exception e) {
                    LogUtils.e("Exception", e.getMessage(), e);
                    ToastUtil.show("图片选择失败", CompanyInfo.this);
                }
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
//		ToastUtil.show("放弃拍摄",this);
        Log.d("CompanyImfo：", "takeCancel: 取消操作");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.e("CompanyImfo：","takeFail:"+ msg);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    //拍照
    private void CaptureMode() {
        final PickDialog selectPhotoDialog = ShowAnimationDialogUtil.showDialog(this,
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
                compressConfig = new CompressConfig.Builder().setMaxSize(400 * 1024).setMaxPixel(800).create();
                takePhoto.onEnableCompress(compressConfig, true);//设置为需要压缩
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
                compressConfig = new CompressConfig.Builder().setMaxSize(400 * 1024).setMaxPixel(800).create();
                takePhoto.onEnableCompress(compressConfig, true);//设置为需要压缩
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

            }
        }
        @Override
        public void onFailed ( int requestCode, List<String > deniedPermissions) {
            // 权限申请失败回调。


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(CompanyInfo.this, deniedPermissions)) {


                //提示语
                AndPermission.defaultSettingDialog(CompanyInfo.this, 103)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };
    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
    private void initData() {
        ////获取TakePhoto实例
        img_found_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        takePhoto = getTakePhoto();
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(false);

        takePhoto.setTakePhotoOptions(builder.create());
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(3).setAspectY(4).setOutputY(300).setOutputX(300).setWithOwnCrop(false).create();
        cropOptions.setWithOwnCrop(true);
        //设置压缩参数
        compressConfig=new CompressConfig.Builder().setMaxSize(400*1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig,true);//设置为需要压缩
    }
}
