package com.car.portal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.XmlHandlers.CitiesModel;
import com.car.portal.XmlHandlers.CityParserHandler;
import com.car.portal.XmlHandlers.DistrictModel;
import com.car.portal.XmlHandlers.DistrictParserHandler;
import com.car.portal.XmlHandlers.ProvinceModel;
import com.car.portal.XmlHandlers.ProvinceParserHandler;
import com.car.portal.activity.service.MainService;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.GoodsTypes;
import com.car.portal.entity.MyTypes;
import com.car.portal.entity.User;
import com.car.portal.entity.VersionInfo;
import com.car.portal.entity.imgBean;
import com.car.portal.fragment.DeliverFragment;
import com.car.portal.fragment.DeliverTabFragment;
import com.car.portal.fragment.DriverInfoFragment;
import com.car.portal.fragment.DriverPersonFragment;
import com.car.portal.fragment.GoodInfoFragment;
import com.car.portal.fragment.MainBaseFragment;
import com.car.portal.fragment.MainscheduleFragment;
import com.car.portal.fragment.MineFragment;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.model.MainscheduleModel;
import com.car.portal.service.BaseService;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.DownloadCompleteReceiver;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.car.portal.util.UpdateAppDialog;
import com.car.portal.view.TabItemView;
import com.google.gson.internal.LinkedTreeMap;

import org.litepal.LitePal;
import org.xml.sax.SAXException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import co.mobiwise.materialintro.MaterialIntroConfiguration;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;


@SuppressLint("NewApi")
@SuppressWarnings("rawtypes")
public class MainActivity extends AppCompatActivity implements OnPageChangeListener {

    @ViewInject(R.id.homepager)
    private ViewPager viewpage;
//    @ViewInject(R.id.main_title)
//    private HomeTitleView titleView;
    private long exitTime = 0;
    private imgBean imgBean;
    private TabItemView person;
    private List<Fragment> tabs = new ArrayList<>();
    private FragmentPagerAdapter adapter;
    private String[] titles = new String[5];
    private int[] un_res = new int[]{R.drawable.chat, R.drawable.person_list, R.drawable.delivery, R.drawable.find, R.drawable.personal};
    private int[] ck_res = new int[]{R.drawable.chat_check, R.drawable.person_list_check, R.drawable.delivery_check, R.drawable.find_check,
            R.drawable.personal_check};
    private List<TabItemView> tabIndicator = new ArrayList<TabItemView>(); //下面fragment显示的条件
    private OnClickListener tabIndicaterClick;
    private int currentPage = 0;
    private static final String TAG = "MainActivity";
    private SharedPreferences sp;
    private UserService userService;
    private User user;
    private boolean isCheckUpdate;
    ImageView imageView; //发货图标
    private Toolbar toolbar;
    private MaterialIntroConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getContext().addActivity(this);
        setContentView(R.layout.main);
        x.view().inject(MainActivity.this);
        Log.e("MainActivity", "onCreate");
        imageView = (ImageView) findViewById(R.id.deliver_img);
        userService = new UserService(this);
        user = userService.getSavedUser();
        init(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("货满车货主");
        setSupportActionBar(toolbar);
        power();
        NotificationEnabled();
        SaveAddressToDataBase();
    }

    private void NotificationEnabled() {
        boolean b = BaseUtil.isNotificationEnabled(MainActivity.this);
        if(b){

        }else {
             new MaterialDialog.Builder(MainActivity.this)
                    .title("通知设置")
                    .content("检测到您关闭了通知消息，这样会使您错过物流通知的消息推送，请前往打开。")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            BaseUtil.gotoNotificationSetting(MainActivity.this);
                        }
                    }).show();
        }
    }


    private void power() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(MainActivity.this.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("电池优化")
                        .content("货满车货主需要您打开电池优化，以便手机能及时收到货物的通知推送信息，是否前往打开？")
                        .positiveText("确定")
                        .negativeText("以后再说")
                        .contentColorRes(R.color.black)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ignoreBatteryOptimization(MainActivity.this);
                            }
                        })
                        .show();
            }
        }
    }


    /**
     * 忽略电池优化
     */
    public void ignoreBatteryOptimization(Activity activity) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+activity.getPackageName()));
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.d(TAG, "Market client not available.");
            }
    }


    public void SaveAddressToDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InitProvinceData();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                InitCity();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                InitDistrict();
            }
        }).start();
    }

    private void InitProvinceData() {
        try {
            int count = LitePal.count(ProvinceModel.class);
            if (count < 34) {
                LitePal.deleteAll(ProvinceModel.class);


                InputStream inputStream = getAssets().open("Provinces.xml");
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser parser = spf.newSAXParser();
                ProvinceParserHandler handler = new ProvinceParserHandler();
                parser.parse(inputStream, handler);
                inputStream.close();
                LitePal.saveAll(handler.getDataList());
                Log.e(">>>>", "InitProvinceData" + handler.getDataList().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void InitCity() {
        try {
            int count = LitePal.count(CitiesModel.class);
            if (count < 391) {
                LitePal.deleteAll(CitiesModel.class);

                InputStream inputStream = getAssets().open("Cities.xml");
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser parser = spf.newSAXParser();
                CityParserHandler handler = new CityParserHandler();
                parser.parse(inputStream, handler);
                inputStream.close();
                LitePal.saveAll(handler.getDataList());
                Log.e(">>>>", "InitCity" + handler.getDataList().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void InitDistrict() {
        try {
            int count = LitePal.count(DistrictModel.class);
            if (count < 3677) {
                LitePal.deleteAll(DistrictModel.class);

                InputStream inputStream = getAssets().open("Districts.xml");
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser parser = spf.newSAXParser();
                DistrictParserHandler handler = new DistrictParserHandler();
                parser.parse(inputStream, handler);
                inputStream.close();
                LitePal.saveAll(handler.getDataList());
                Log.e(">>>>", "InitDistrict" + handler.getDataList().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void getTypes() {
        final GoodsService service = new GoodsService(this);
        service.getGoodsTypes(new MyCallBack<BaseEntity<ArrayList>>() {
            public void onError(Throwable throwable, boolean b) {
                BaseUtil.writeFile("GoodsRegester", throwable);
            }

            public void onSuccess(BaseEntity<ArrayList> arg0) {
                if (arg0.getResult() == 1) {
                    ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
                    ArrayList<MyTypes> types = new ArrayList<MyTypes>();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            MyTypes goodtypes = new MyTypes();
                            LinkMapToObjectUtil.getObject(list.get(i), goodtypes);
                            GoodsTypes.addTypes(goodtypes);
                        }
                    }
                } else {
                    LogUtils.e(TAG, "getTypes():" + arg0.getMessage());
                }
            }
        });
    }

    private void init(Bundle savedInstanceState) {
        titles[0] = getResources().getString(R.string.tab_home);
        titles[1] = getResources().getString(R.string.tab_driver);
        titles[2] = getResources().getString(R.string.tab_delivery);
        titles[3] = getResources().getString(R.string.tab_info);
        titles[4] = getResources().getString(R.string.tab_person);
        GoodInfoFragment frag1 = new GoodInfoFragment();
        tabs.add(frag1);
        DriverInfoFragment frag2 = new DriverInfoFragment();
        tabs.add(frag2);
        // 发货
        DeliverTabFragment frg3 = new DeliverTabFragment();
        tabs.add(frg3);
        MineFragment frag4 = new MineFragment();
        tabs.add(frag4);
        DriverPersonFragment frag5 = new DriverPersonFragment();
        tabs.add(frag5);

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return tabs.get(arg0);
            }
        };
        tabIndicaterClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.pager_home:
                        viewpage.setCurrentItem(0, false);
                        break;
                    case R.id.pager_list:
                        viewpage.setCurrentItem(1, false);
                        break;
                    case R.id.deliver_img:
                        viewpage.setCurrentItem(2, false);
                        break;
                    case R.id.pager_find:
                        viewpage.setCurrentItem(3, false);
                        break;
                    case R.id.pager_person:
                        viewpage.setCurrentItem(4, false);
                        break;
                    // 发货
                }
            }
        };
        initTabIndicator();
        viewpage.setAdapter(adapter);
        viewpage.addOnPageChangeListener(this);
        String isnewuser = getIntent().getStringExtra("isnewuser");
        if(isnewuser!=null&&!"".equals(isnewuser)){
            userService.bindingreferrer(MainActivity.this,false);
        }

//        if(user.getWx_openId()==null||user.getWx_openId().equals("")) {
//            Intent intent = new Intent(MainActivity.this,Wxautherization.class);
//            intent.putExtra("name",getIntent().getStringExtra("name"));
//            intent.putExtra("pwd",getIntent().getStringExtra("pwd"));
//            intent.putExtra("AuthType", -1);
//            intent.putExtra("type","1");
//            startActivity(intent);
//        }

//        new MaterialIntroView.Builder(MainActivity.this)
//                .enableDotAnimation(true)
//                .enableIcon(false)
//                .setConfiguration(config)
//                .enableFadeAnimation(true)
//                .performClick(false)
//                .setListener(new MaterialIntroListener() {
//                    @Override
//                    public void onUserClicked(String s) {
//
//                    }
//                })
//                .setInfoText("欢迎来到货满车,请先点击我的到个人信息页面。")
//                .setTarget(person)
//                .setUsageId("mian")
//                .show();
    }

    private void initTabIndicator() {
        TabItemView home =  findViewById(R.id.pager_home);
        TabItemView list =  findViewById(R.id.pager_list);
        TabItemView find =  findViewById(R.id.pager_find);
         person = findViewById(R.id.pager_person);
        TabItemView divery = new TabItemView(this, null);//无实质意义只是为了填充好实现滑动效果
        tabIndicator.add(home);
        tabIndicator.add(list);
        tabIndicator.add(divery);
        tabIndicator.add(find);
        tabIndicator.add(person);
        home.setTitle(titles[0]);
        list.setTitle(titles[1]);
        find.setTitle(titles[3]);
        person.setTitle(titles[4]);
        home.checked(ck_res[0]);
        list.unChecked(un_res[1]);
        find.unChecked(un_res[3]);
        person.unChecked(un_res[4]);
        home.setOnClickListener(tabIndicaterClick);
        list.setOnClickListener(tabIndicaterClick);
        find.setOnClickListener(tabIndicaterClick);
        person.setOnClickListener(tabIndicaterClick);
        imageView.setOnClickListener(tabIndicaterClick);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*LogUtils.i(TAG, "position:" + position + "   positionOffset:" + positionOffset + "     " +
                "positionOffsetPixels:" + positionOffsetPixels);*/
    }

    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == 2) {
            imageView.setImageResource(R.drawable.delivery_check);
            toolbar.setVisibility(View.GONE);
            tabIndicator.get(currentPage).unChecked(un_res[currentPage]);
//            titleView.setVisibility(View.GONE);
            currentPage = arg0;
        } else {
            imageView.setImageResource(R.drawable.delivery);
            tabIndicator.get(arg0).checked(ck_res[arg0]);
            tabIndicator.get(currentPage).unChecked(un_res[currentPage]);
//            titleView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            currentPage = arg0;
            if (currentPage == 0) {
//                titleView.setSearchVisible(true);
            } else {
//                titleView.setSearchVisible(false);
//                titleView.setEditVisible(false);
            }
           // LogUtils.e("mainActivity", arg0 + " :" + System.currentTimeMillis());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isCheckUpdate = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (GoodsTypes.getSize() == 0) {
                getTypes();
            }
            if (!isCheckUpdate) {
                isCheckUpdate = true;
                checkUpdate();
            }
            if (!isTopActivy(MainService.class.getName())) {
                Intent intent = new Intent(this, MainService.class); // 后台消息接收
                startService(intent);
            }
        }
    }


    public boolean isTopActivy(String serviceName) {
        ActivityManager amanager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = amanager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isExit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 是否退出程序
     */
    public void isExit() {
        moveTaskToBack(true);
    }

    private boolean checkSDPermission() {
        if (MyApplication.osVersion < 23) {
            return true;
        } else {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * 检查更新版本
     */
    public void alertUpdate(final String url) {
        LogUtils.e(TAG, url);
        LogUtils.e(TAG, "local Version: " + MyApplication.localVersion + "\tserverVersion:" +
                MyApplication.serverVersion);
        if (!checkSDPermission()) {
            ToastUtil.show("发现新版本，但您已将存储权限设为禁止，请手动下载更新", MainActivity.this);
            return;
        }
        if (MyApplication.localVersion < MyApplication.serverVersion) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("版本更新");
            builder.setMessage("发现新版本,建议立即更新使用");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    BaseUtil.Downloadapk(MainActivity.this,url);
                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("请稍后");
                    dialog.setMessage("新版本正在下载中，请等待安装");
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });
            builder.show();
        } else {
            File f = new File(BaseUtil.DOWNLOAD_LOCATION, BaseUtil.APK_FILE_NAME);
            if (f.exists()) {
                f.delete();
            }
        }
    }


    public void checkUpdate() {
        if (MyApplication.localVersion <= 0) {
            try {
                MyApplication.localVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        if (MyApplication.serverVersion <= 0) {
            userService.getCurrentVersion(MyApplication.localVersion, new MyCallBack<BaseEntity>() {

                public void onError(Throwable arg0, boolean arg1) {
                    BaseUtil.writeFile(TAG, arg0);
                }

                @Override
                public void onSuccess(BaseEntity arg0) {
                    if (arg0.getResult() == 1) {
                        VersionInfo info = new VersionInfo();
                        LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) arg0.getData(), info);
                        MyApplication.serverVersion = info.getVersion();
                        userService.saveVersionInfo(info);
                        alertUpdate(info.getDownUrl());
                    }
                }
            });
        } else if (MyApplication.serverVersion > MyApplication.localVersion) {
            VersionInfo info = userService.getVersionInfo();
            if (info != null) {
                MyApplication.serverVersion = info.getVersion();
                alertUpdate(info.getDownUrl());
            }
        }
    }

    public void setPage(int index) {
        viewpage.setCurrentItem(index, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_men,menu);
        return true;
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addgoods:
                Intent intent = new Intent(MainActivity.this, GoodsRegisterAcriviry.class);
                startActivity(intent);
                break;
            case R.id.more:
                Intent inten2 = new Intent(MainActivity.this, NotificationActivity.class);
				startActivity(inten2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}