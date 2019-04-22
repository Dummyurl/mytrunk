package com.car.portal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.application.AppConfig;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Company;
import com.car.portal.entity.PortalDriver;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.service.DriverService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.HTTPSTrustManager;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.widget.PickDialog;
import com.car.portal.widget.ShowAnimationDialogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okio.Buffer;

public class DriverAuthActivity extends TakePhotoActivity implements View.OnClickListener {

    @BindView(R.id.img_identify_return)
    ImageView imgIdentifyReturn;
    @BindView(R.id.driver_ident_found)
    Button driverIdentFound;
    @BindView(R.id.img_check_Join)
    ImageView imgCheckJoin;
    @ViewInject(R.id.driver_ident_img)
    private ImageView identImg;
    @ViewInject(R.id.driver_person_img)
    private ImageView personImg;
    @ViewInject(R.id.driver_driving_img)
    private ImageView drivingImg;
    @ViewInject(R.id.driver_drive_img)
    private ImageView driverImg;
    @ViewInject(R.id.driver_ident_comfirm)
    private Button confirm;
    @ViewInject(R.id.auth_text)
    private TextView auth_text;

    private View currentImg;
    private PortalDriver driver;
    private DriverService driverService;
    private Map<String, File> params = new HashMap<String, File>();
    private List<File> fs = new ArrayList<File>();
    private boolean has_sub = false;

    private static final int PICK_PHOTO = 0x1066;
    private Picasso picasso;
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
    private CropOptions cropOptions;//裁剪参数
    private CompressConfig compressConfig; //压缩参数
    private Uri imageUri;//图片保存路径\
    private EditText inputServer;
    private MyHttpUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_identify);
        ButterKnife.bind(this);
        x.view().inject(DriverAuthActivity.this);
        MyApplication.getContext().addActivity(this);
//		InitPicasso();
        initData();
        Intent intent = getIntent();
        driver = (PortalDriver) intent.getSerializableExtra("driver");
        driverService = new DriverService(DriverAuthActivity.this);
        //ToastUtil.show("下载图片中...", DriverAuthActivity.this);
        picasso = Picasso.get();
        initImg();
        identImg.setOnClickListener(this);
        personImg.setOnClickListener(this);
        drivingImg.setOnClickListener(this);
        driverImg.setOnClickListener(this);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (has_sub) {
                    return;
                }
                has_sub = true;
                if (params.size() > 0) {
                    LogUtils.e("DriverAuth", "confirm submit");
                    driverService.saveDriverImgs(params, callBack);
                } else {
                    ToastUtil.show("您未修改任何内容", DriverAuthActivity.this);

                }
            }
        });
    }


    private void initImg() {
        String url;
        if (driver != null && !StringUtil.isNullOrEmpty(driver.getIdentImage())) {
            url = driverService.getServer() + driver.getIdentImage();
            loadDriverImg(identImg, url);
        }
        if (driver != null && !StringUtil.isNullOrEmpty(driver.getPersonalImage())) {
            url = driverService.getServer() + driver.getPersonalImage();
            loadDriverImg(personImg, url);
        }
        if (driver != null && !StringUtil.isNullOrEmpty(driver.getDrivingImage())) {
            url = driverService.getServer() + driver.getDrivingImage();
            loadDriverImg(drivingImg, url);
        }
        if (driver != null && !StringUtil.isNullOrEmpty(driver.getDriverLicense())) {
            url = driverService.getServer() + driver.getDriverLicense();
            loadDriverImg(driverImg, url);
        }
        driverService.getAuthentication(getInfoBack);
    }


    public void loadDriverImg(final ImageView view, String url) {
        Log.e(">>>>>>", url);
        url = url.replaceAll("//", "/");
        url = url.replace("http:/", "http://");
        url = url.replace("https:/", "https://");
        /*String uid = driver.getUid() + "";
        File file = new File(AppConfig.UserSourceImg);
        if (!file.exists()) file.mkdirs();
        PicassoTarget picassoTarget = new PicassoTarget(view, new File(AppConfig.UserSourceImg, view.getTag() + uid + ".jpg"));
        view.setTag(picassoTarget);*/
        picasso.load(url).resize(DensityUtil.dip2px(140), DensityUtil.dip2px(110)).centerCrop().into(view);
    }


    //请求司机信息
    private HttpCallBack getInfoBack = new HttpCallBack(DriverAuthActivity.this) {
        @Override
        public void onSuccess(Object... objects) {
            BaseEntity<ArrayList> arg0 = (BaseEntity<ArrayList>) objects[0];
            //
            int auth = arg0.getResult();
            Log.e("cheer", "auth" + auth);
            if (auth == -1) {
                auth_text.setText("没有提交资料");
            } else if (auth == 0) {
                auth_text.setText("已提交待审核");
            } else if (auth == 1) {
                auth_text.setText("审核失败,请重新上传");
            } else if (auth == 2) {
                auth_text.setText("再次提交完成");
            } else if (auth == 3) {
                auth_text.setText("审核通过");
            }
        }
    };

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//			if (data != null && requestCode == PICK_PHOTO) {
//				ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//				ImageView img = (ImageView) currentImg;
//				String path = images.get(0).path;
//				Picasso.get().load(Uri.fromFile(new File(path))).networkPolicy(NetworkPolicy.OFFLINE).resize(DensityUtil.dip2px(280), DensityUtil.dip2px(210)).centerCrop().into(img);
//				//img.setImageURI(Uri.parse(path));
//				addParams(path, currentImg.getId());
//
//			}
//		}
//	}

    private void addParams(final String path, final int vid) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(path);
                String key = null;
                switch (vid) {
                    case R.id.driver_ident_img:
                        key = "identImg";
                        break;
                    case R.id.driver_person_img:
                        key = "personImg";
                        break;
                    case R.id.driver_driving_img:
                        key = "drivingImg";
                        break;
                    case R.id.driver_drive_img:
                        key = "driverImg";
                        break;
                }
                if (file.length() > BaseUtil.MAX_SIZE) {
                    try {
                        file = BaseUtil.compressImage(path, "cache_" + file.getName());
                        fs.add(file);
                    } catch (IOException e) {
                        ToastUtil.show("图片缓存失败，请在权限管理设置读写权限", DriverAuthActivity.this);
                        LogUtils.e("IMAGESIZE", file.getName() + "压缩失败：" + e.getMessage());
                    }
                }
                if (file != null) {
                    params.put(key, file);
                } else {
                    params.remove(key);
                }
            }
        }.start();
    }


	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			try {
				String [] proj={MediaStore.Images.Media.DATA, MediaStore.Images.Media.DESCRIPTION,MediaStore.Images.Media.DISPLAY_NAME};  
		        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
		        cursor.moveToFirst();  
		        final String path = cursor.getString(column_index);
		        cursor.close();
				File file = new File(path);
				final int vid = currentImg.getId();
				final String fname = file.getName();
				switch (vid) {
				case R.id.driver_ident_img:
					params.put("identImg", file); //身份证照片
					break;
				case R.id.driver_person_img:
					params.put("personImg", file);//手持身份证照片
					break;
				case R.id.driver_driving_img:
					params.put("drivingImg", file);// 营业执照
					break;
				case R.id.driver_drive_img:
					params.put("driverImg", file);//门面
					break;
				}
				if(file.length() > BaseUtil.MAX_SIZE ) {
					new Thread() {
						public void run() {
							File file1;
							String key = null;
							switch (vid) {
							case R.id.driver_ident_img:
								key = "identImg";
								break;
							case R.id.driver_person_img:
								key = "personImg";
								break;
							case R.id.driver_driving_img:
								key = "drivingImg";
								break;
							case R.id.driver_drive_img:
								key = "driverImg";
								break;
							}
							try {
								file1 = BaseUtil.compressImage(path, "cache_" + fname);
								fs.add(file1);
								x.image().bind((ImageView) currentImg, file1.getPath(), 
										new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build());
								LogUtils.e("string", key);
								params.put(key, file1);
							} catch (IOException e) {
								System.out.println(e.getMessage());
								LogUtils.e("IMAGESIZE", fname + "压缩失败：" + e.getMessage());
								Bundle bundle = new Bundle();
								bundle.putString("file", fname);
								Message msg = new Message();
								handler.sendMessage(msg);
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
				ToastUtil.show("图片选择失败", DriverAuthActivity.this);
			}
		}
	}*/

    private void InitPicasso() {
        try {
            InputStream inputStream = new Buffer().writeUtf8(AppConfig.cer).inputStream();
            //String key = "file:///android_asset/tomcat";
            SSLContext tls = SSLContext.getInstance("TLS");
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());    //创建一个keystore来管理密钥库
            keystore.load(null, null);
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
            keystore.setCertificateEntry("tomcat", certificate);
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
            TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init(keystore);
            tls.init(null, instance.getTrustManagers(), new SecureRandom());
            SSLSocketFactory socketFactory = tls.getSocketFactory();
            long maxSize = Runtime.getRuntime().maxMemory() / 8;
            File file = new File(AppConfig.UserSourceImg);
            if (!file.exists()) {
                file.mkdirs();
            }

            OkHttpClient client = new OkHttpClient.Builder()

                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .cache(new Cache(getFilesDir(), maxSize))
                    .sslSocketFactory(socketFactory, new HTTPSTrustManager())//https
                    .build();
            picasso = new Picasso.Builder(this).indicatorsEnabled(false).downloader(new OkHttp3Downloader(client)).build();
            if (Picasso.get() == null)
                Picasso.setSingletonInstance(picasso);
            picasso = Picasso.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result != null) {
            personImageFileName = result.getImage().getOriginalPath();
            ImageView img = (ImageView) currentImg;
            Picasso.get().load(Uri.fromFile(new File(personImageFileName))).networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(DensityUtil.dip2px(210), DensityUtil.dip2px(210)).centerCrop().into(img);
            addParams(personImageFileName, currentImg.getId());
//            if(has_sub) {
//                return;
//            }
//            has_sub = true;
//            addParams(personImageFileName, header.getId());
//            if (params.size() > 0) {
//			LogUtils.e("DriverAuth", "confirm submit");
//			driverService.savePersonImgs(params, callBack);
//            } else {
//                ToastUtil.show("您未修改任何内容", getActivity());
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
//		ToastUtil.show("放弃拍摄",this);
        Log.d("DriverAuthActivity：", "takeCancel: 取消操作");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.e("DriverAuthActivity：", "takeFail:" + msg);
    }


    private HttpCallBack callBack = new HttpCallBack(DriverAuthActivity.this) {
        @Override
        public void onSuccess(Object... objects) {
            LogUtils.i("uploadFile", "success");
            x.image().clearCacheFiles();
            x.image().clearMemCache();
            ToastUtil.show(objects[0].toString(), DriverAuthActivity.this);
            DriverAuthActivity.this.finish();
            has_sub = false;
            LogUtils.e("DriverAuth", "confirm finish");
            params.clear();
        }

        public void onFail(int result, String message, boolean show) {
            super.onFail(result, message, show);
            has_sub = false;
            if (result != -2) {
            }
        }

        public void onError(Object... objects) {
            super.onError(objects);
            has_sub = false;
        }
    };

    protected void onDestroy() {
        for (int i = fs.size() - 1; i >= 0; i--) {
            File file = fs.get(i);
            if (file != null) {
                file.delete();
            }
            fs.remove(i);
        }
        super.onDestroy();
//		handler.removeCallbacksAndMessages(null);
    }

    private static final int HANDMSG = 0x1345;

    @SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if(msg.what == HANDMSG) {
//				String file = msg.getData().getString("file");
//				ToastUtil.show(file + "压缩失败，无法上传该照片", DriverAuthActivity.this);
//			}
//		}
//	};

    @Override
    public void onClick(View view) {
        currentImg = view;
        CaptureMode();
//		Intent intent = new Intent(DriverAuthActivity.this, ImageGridActivity.class);
//		startActivityForResult(intent, PICK_PHOTO);
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

    private void initPermission() {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(DriverAuthActivity.this, deniedPermissions)) {


                //提示语
                AndPermission.defaultSettingDialog(DriverAuthActivity.this, 103)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(false);

        takePhoto.setTakePhotoOptions(builder.create());
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(3).setAspectY(4).setOutputY(300).setOutputX(300).setWithOwnCrop(false).create();
        cropOptions.setWithOwnCrop(true);
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(400 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);//设置为需要压缩
    }

    @OnClick({R.id.img_identify_return, R.id.driver_ident_found,R.id.img_check_Join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_identify_return:
                finish();
                break;
            case R.id.driver_ident_found:
                //				Findthecompany.gotofindcop(DriverAuthActivity.this);
                new MaterialDialog.Builder(DriverAuthActivity.this).title("请输入公司联系人电话号码")
                        .iconRes(R.drawable.ic_launcher)
                        .content("输入公司联系人号码后点击确认，会查询公司是否存在")
                        .widgetColorRes(R.color.colorPrimary)
                        .inputRangeRes(0, 15, R.color.colorAcceet)
                        .inputType(InputType.TYPE_CLASS_PHONE)
                        .input("电话号码", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                getcompany(input + "");
                            }
                        })
                        .positiveText("查找")
                        .show();
                break;
            case R.id.img_check_Join:
                JoincompanylistActivity.gotofindcop(this);
                break;
        }
    }


    private void getcompany(final String phone) {
        util = new MyHttpUtil(DriverAuthActivity.this);
        String url = util.getUrl(R.string.url_findCompanyByPhone) + "?phone=" + phone;
        Log.d("url", url);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<Object>>() {

            @Override
            public void onSuccess(BaseEntity baseEntity) {
           if (baseEntity.getResult() == 1) {
                    final Company company = new Company();
                    LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) baseEntity.getData(), company);
                    new MaterialDialog.Builder(DriverAuthActivity.this).title("查找成功")
                            .iconRes(R.drawable.ic_launcher)
                            .content(company.getName())
                            .contentColorRes(R.color.black)
                            .positiveText("申请")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    JoinCompany(company.getId().toString(), company.getDuration().toString(),phone, company.getAlias());
                                }
                            })
                            .show();

                } else {
                    new MaterialDialog.Builder(DriverAuthActivity.this).title("查询失败")
                            .iconRes(R.drawable.ic_launcher)
                            .content("未查找到该公司，请确认公司电话是否输入正确！")
                            .positiveText("确认")
                            .show();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError", arg0.toString());
                new MaterialDialog.Builder(DriverAuthActivity.this).title("查询失败")
                        .iconRes(R.drawable.ic_launcher)
                        .content("该用户未加入公司")
                        .positiveText("确认")
                        .show();
            }
        });
    }

    private void JoinCompany(String companyId, String toUid, String tel, String name) {
        String url = util.getUrl(R.string.url_applyJoinCompany);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("toUid",toUid);
        params.put("phone",tel);
        params.put("name",name);
        XUtil.Post(url, params, new MyCallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                Log.d("onsucc",baseEntity.getResult()+"");
                if(baseEntity.getResult()==1) {
                    Toast.makeText(DriverAuthActivity.this, "申请发送成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DriverAuthActivity.this, "发送失败，请勿重复申请", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.d("onsucc", throwable.toString());
            }
        });
    }

}
