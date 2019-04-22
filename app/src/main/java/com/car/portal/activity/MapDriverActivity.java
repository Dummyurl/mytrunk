package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.car.portal.R;
import com.car.portal.entity.Company;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.QueryTitleView;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapDriverActivity extends Activity {
    @ViewInject(R.id.title_query)
    private QueryTitleView queryTitleView;
    @ViewInject(R.id.bmapView)
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Point leftpt, rightpt;

    private BitmapDescriptor mIconMaker;
    private GeoCoder geoCoder = null;
    private UserService userService;
    private Company company = null;

    //查询条件
    private double length = 0;
    private double length2 = 0;
    private int typeId = 0;
    private String target;

    private boolean hasInit;
    private boolean hasFind;
    private InfoWindow mInfoWindow;

    private ArrayList<LinkedTreeMap> list;//司机列表
    private static final int REQUEST_CODE = 0x1458;
    private String searchX;
    private String searchY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        x.view().inject(this);
        initView();
        userService = new UserService(this);
        getCoordinate();
        mMapView.getMap().setOnMapStatusChangeListener(listener);
        mBaiduMap.setMaxAndMinZoomLevel(15, 9);
    }

    /**
     * 获取当前用户所在公司的city
     * 优先选择当前设定的位置，如果没有再去公司注册的所在地
     */
    public void initData(){
        if(userService.getAddress()==null) {
            userService.getMyCompany(new HttpCallBack(this) {
                @Override
                public void onSuccess(Object... objects) {
                    hasInit = true;
                    if (objects != null && objects.length > 0) {
                        company = (Company) objects[0];
                        target = userService.getAddress() == null ? "" : userService.getAddress().getCity();
                        getGeoCoder(company.getCity());
                    }
                }
            });
        }else {
            hasInit = true;
            getGeoCoder(userService.getAddress() == null ? "" : userService.getAddress().getCity());
        }
    }

    /**
     * 覆盖物Maker的点击事件
     */
    public void getMarker(){
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                double d = marker.getExtraInfo().getDouble("driverId");
                String name = marker.getExtraInfo().getString("driverName");
                Button button = new Button(getApplicationContext());button.setBackgroundResource(R.drawable.popup);
                button.setText(name);
                button.setTextColor(getResources().getColor(R.color.black));
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        mBaiduMap.hideInfoWindow();
                    }
                };
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), marker.getPosition(), -47, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initView(){//获取地图控件引用
        mBaiduMap=mMapView.getMap();
        queryTitleView.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapDriverActivity.this, MapDriverListActivity.class);
                intent.putExtra("length", length);
                intent.putExtra("length2", length2);
                intent.putExtra("typeId", typeId);
                //intent.putExtra("startAddress", "");
                intent.putExtra("searchX", searchX);
                intent.putExtra("searchY", searchY);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        queryTitleView.setOnQueryListener(new QueryTitleView.QueryClickListener() {
            @Override
            public void onQuery (String condition) {
                condition = (condition == null ? "" :condition);
                if(!StringUtil.isNullOrEmpty(condition)) {
                    char[] ches = condition.toCharArray();
                    StringBuffer bf = new StringBuffer();
                    for (char ch : ches) {
                        if(ch >= '\u4e00' || ch <= '\u9fa5' || ch == ',') {
                            bf.append(ch);
                        } else if (ch == ' ') {
                            bf.append(",");
                        }
                    }
                    condition = bf.toString();
                }
                if(!condition.equals(target)) {
                    target = condition;
                    getRange();
                }
            }
        });
        leftpt = new Point();
        rightpt = new Point();
        //构建Marker图标
        mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x1459 && data != null) {
            length = data.getDoubleExtra("length", 0);
            length2 = data.getDoubleExtra("length2", 0);
            typeId = data.getIntExtra("typeId", 0);
            target = data.getStringExtra("target");
        }
    }

    /**
     * 地理编码
     * @param s 城市
     */
    public void getGeoCoder(String s){
        if(geoCoder != null) {
            geoCoder.destroy();
        }
        geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtil.show("没有找到公司位置", MapDriverActivity.this);
                } else {
                    getLocation(result.getLocation());
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                //if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                //}
                //获取反向地理编码结果
            }
        };
        //给地理编码设置监听
        geoCoder.setOnGetGeoCodeResultListener(listener);
        //发起地理编码请求
        geoCoder.geocode(new GeoCodeOption().city(s).address(s));
    }

    /**
     * 手势操作地图
     */
    BaiduMap.OnMapStatusChangeListener listener = new BaiduMap.OnMapStatusChangeListener() {

        public void onMapStatusChangeStart(MapStatus status){
        }

        public void onMapStatusChange(MapStatus status){
        }

        public void onMapStatusChangeFinish(MapStatus status){
            handler.sendEmptyMessage(0x1457);
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
        }

    };
    /**
     * 添加覆盖物
     */
    private void addOverlay(){
        mBaiduMap.clear();
        if(list != null) {
            for (LinkedTreeMap map : list) {
                try{
                    double x = (Double) map.get("x");
                    double y = (Double) map.get("y");
                    LatLng latLng = new LatLng(y, x);
                    MarkerOptions overlayOptions = new MarkerOptions().position(latLng).icon(mIconMaker)
                            .draggable(false);
                    overlayOptions.title(map.get("driverName").toString());
                    Marker marker = (Marker) mBaiduMap.addOverlay(overlayOptions);
                    Bundle b = new Bundle();
                    b.putString("driverName", map.get("driverName").toString());
                    b.putInt("driverId", ((Double)map.get("driverId")).intValue());
                    marker.setExtraInfo(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        getMarker();
    }

    /**
     * 得到地图可见范围内的司机的经纬度集合
     */
    public void getRange(){
        if(hasFind) {
            return;
        }
        hasFind = true;
        try {
            LatLng leftll = mMapView.getMap().getProjection().fromScreenLocation(leftpt);
            LatLng rightll = mMapView.getMap().getProjection().fromScreenLocation(rightpt);
            searchX = leftll.longitude + "," + rightll.longitude;
            searchY = leftll.latitude + "," + rightll.latitude;
            DriverService userService = new DriverService(this);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("target", target);
            map.put("x", searchX);
            map.put("y", searchY);
            map.put("length", length);
            map.put("length2", length2);
            map.put("typeId", typeId);
            map.put("currentPage", 1);
            userService.getMapDriver(map, new HttpCallBack(this) {
                @Override
                public void onSuccess (Object... objects) {
                    hasFind = false;
                    if (objects != null && objects.length > 0) {
                        list = (ArrayList<LinkedTreeMap>) objects[0];
                        if (list != null) {
                            handler.sendEmptyMessage(0x1456);
                        } else {
                            ToastUtil.show("没有找到司机", MapDriverActivity.this);
                        }
                    }
                }

                @Override
                public void onError (Object... objects) {
                    hasFind = false;
                    super.onError(objects);
                }

                @Override
                public void onFail (int result, String message, boolean show) {
                    hasFind = false;
                    super.onFail(result, message, show);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前屏幕宽高，得到左下角和右上角的坐标
     */
    public void getCoordinate(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int Max_X = dm.widthPixels;
        int Max_Y = dm.heightPixels;
        leftpt.x = 0;
        leftpt.y = Max_Y;
        rightpt.y = 0;
        rightpt.x = Max_X;
    }


    // 指定地图显示
    public void getLocation(LatLng latLng) {
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        getRange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放地理编码
        if(geoCoder != null) {
        	geoCoder.destroy();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mMapView != null) {
        	mMapView.onDestroy();
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasInit) {
    		initData();
    	}
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x1456) {
                addOverlay();
            } else if(msg.what == 0x1457) {
                getRange();
            }
        }
    };
}
