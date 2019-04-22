package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.car.portal.R;
import com.car.portal.view.BaseTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LocalMapActivity extends Activity {


    @ViewInject(R.id.base_title_view)
    BaseTitleView titleView;
    @ViewInject(R.id.map_view)
    MapView mapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mIconMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_map);
        x.view().inject(this);
        Intent intent = getIntent();
        float lat= intent.getFloatExtra("lat",0);
        double lng= intent.getDoubleExtra("lng",0);







        Log.e(">>>>",lat+"");
        Log.e(">>>>",lng+"");
        titleView.setTitle("定位");
        mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        LatLng point = new LatLng(lat,lng);

        //构建Marker图标

        //构建MarkerOption，用于在地图上添加Marker

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(mIconMaker);

        //在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);


        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }


}
