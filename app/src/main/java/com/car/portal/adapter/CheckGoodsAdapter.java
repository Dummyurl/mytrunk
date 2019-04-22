package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.car.portal.R;
import com.car.portal.entity.CheckGoodsData;
import com.car.portal.service.DriverService;
import com.car.portal.util.FormatCurrentData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CheckGoodsAdapter extends BaseQuickAdapter<CheckGoodsData.DataBean, BaseViewHolder> {


    public CheckGoodsAdapter(int layoutResId, @Nullable List<CheckGoodsData.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckGoodsData.DataBean item) {
        TextView startCity = helper.getView(R.id.start_city);
        TextView endCity = helper.getView(R.id.end_city);
        TextView describe = helper.getView(R.id.describe);
        RoundedImageView headImg = helper.getView(R.id.head_img);
        TextView name = helper.getView(R.id.name);
        TextView time = helper.getView(R.id.time);
        RatingBar ratingBar = helper.getView(R.id.rating_bar);
        helper.addOnClickListener(R.id.call);
        helper.addOnClickListener(R.id.local);


        startCity.setText(item.getStart_address());
        endCity.setText(item.getEnd_address());
        time.setText(FormatCurrentData.getTimeRange(item.getCreatetime()));
        String carLong = carLongData.get(item.getCarLenghtV());
        String bodType = bodyTypeData.get(item.getBodyTypeOptionV());
        String goodsTypesType = item.getGoodsType();
        ratingBar.setNumStars(item.getCreditLevel());
        name.setText(item.getContactsNames());
        double latS = item.getSx();
        double LngS = item.getSy();
        double Late = item.getEx();
        double Lnge = item.getEy();
        LatLng lat1=new LatLng(latS,LngS);
        LatLng lat2=new LatLng(Late,Lnge);
        LatLng latLng = BD09MCtoBD09LL(lat1);
        LatLng latLng1 = BD09MCtoBD09LL(lat2);
        double s= DistanceUtil.getDistance(latLng, latLng1);


        if (item.getPersonImage()!=null&&!item.getPersonImage().isEmpty()){
            DriverService driverService = null;
            if(driverService==null)
               driverService= new DriverService(this.mContext);
            Picasso.get().load(driverService.getServer()+item.getPersonImage()).into(headImg);
        }else {
            Picasso.get().load(R.drawable.head_img).into(headImg);
        }
        int quare = item.getQuare();
        int volume = item.getVolume();
        if (quare > 0) {
            describe.setText(carLong + bodType + goodsTypesType + " (" + quare + "吨)");
            return;
        }else if (volume > 0) {
            describe.setText(carLong + bodType + goodsTypesType + " (" + volume + "方)");
            return;
        }else {
            describe.setText(carLong + bodType + goodsTypesType);
        }
    }


    private static final HashMap<Integer, String> bodyTypeData = new HashMap<>();

    {
        bodyTypeData.put(0, "");
        bodyTypeData.put(1, "高低板 ");
        bodyTypeData.put(2, "高栏 ");
        bodyTypeData.put(3, "冷藏货柜 ");
        bodyTypeData.put(4, "平板 ");
        bodyTypeData.put(5, "高低高 ");
        bodyTypeData.put(6, "货柜 ");
        bodyTypeData.put(7, "超低板 ");
        bodyTypeData.put(8, "自缷车 ");
        bodyTypeData.put(9, "高栏平板 ");
        bodyTypeData.put(10, "高栏高低板 ");

    }

    private static final HashMap<Integer, String> carLongData = new HashMap<>();

    {
        carLongData.put(0, "");
        carLongData.put(1, "1.8米 ");
        carLongData.put(2, "4.2米 ");
        carLongData.put(3, "5米 ");
        carLongData.put(4, "6.8米 ");
        carLongData.put(5, "7.7米 ");
        carLongData.put(6, "8.2米 ");
        carLongData.put(7, "9.6米 ");
        carLongData.put(8, "11.7米 ");
        carLongData.put(9, "13米 ");
        carLongData.put(10, "13.5米 ");
        carLongData.put(11, "17.5米 ");
        carLongData.put(12, "18米以上 ");


    }


   /* private static final HashMap<Integer, String> goodsTypesData = new HashMap<>();

    {
        goodsTypesData.put(0, " ");
        goodsTypesData.put(1, "普货 ");
        goodsTypesData.put(2, "重货 ");
        goodsTypesData.put(3, "泡货 ");
        goodsTypesData.put(4, "设备 ");
        goodsTypesData.put(5, "配件 ");
        goodsTypesData.put(6, "百货 ");
        goodsTypesData.put(7, "建材 ");
        goodsTypesData.put(8, "食品 ");
        goodsTypesData.put(9, "饮料 ");
        goodsTypesData.put(10, "化工 ");
        goodsTypesData.put(11, "水果 ");
        goodsTypesData.put(12, "蔬菜 ");
        goodsTypesData.put(13, "木材 ");
        goodsTypesData.put(14, "煤炭 ");
        goodsTypesData.put(15, "石材 ");
        goodsTypesData.put(16, "家具 ");
        goodsTypesData.put(17, "树苗 ");
        goodsTypesData.put(18, "化肥 ");
        goodsTypesData.put(19, "粮食 ");
        goodsTypesData.put(20, "钢材 ");
        goodsTypesData.put(21, "小家电 ");
        goodsTypesData.put(22, "大家电 ");

    }*/

    public static LatLng BD09MCtoBD09LL(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BD09MC);
        converter.coord(sourceLatLng);
        return converter.convert();
    }
}
