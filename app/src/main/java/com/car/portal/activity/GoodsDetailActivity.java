package com.car.portal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.entity.CheckGoodsData;
import com.car.portal.util.FormatCurrentData;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailActivity extends Activity {


    @BindView(R.id.base_title_view)
    BaseTitleView baseTitleView;
    @BindView(R.id.start_city)
    TextView startCity;
    @BindView(R.id.end_city)
    TextView endCity;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.local)
    TextView local;
    @BindView(R.id.goods_type)
    TextView goodsType;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.breakbulk)
    TextView breakbulk;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.car_long)
    TextView carLong;
    @BindView(R.id.car_type)
    TextView carType;
    @BindView(R.id.body_type)
    TextView bodyType;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.head_img)
    RoundedImageView headImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.call)
    TextView call;
    private Dialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        baseTitleView.setTitle("货源详情");
        Intent intent = getIntent();
        final CheckGoodsData.DataBean dataBean=intent.getParcelableExtra("data");
        startCity.setText(dataBean.getStart_address());
        endCity.setText(dataBean.getEnd_address());
        time.setText(FormatCurrentData.getTimeRange(dataBean.getCreatetime()));
        //距离
        //地图
        goodsType.setText(dataBean.getGoodsType());
        int quare = dataBean.getQuare();
        int volume = dataBean.getVolume();
        if (quare > 0) {
            weight.setText(quare + "吨");
        }else if (volume > 0) {
            weight.setText(volume + "方");
        }else {
            weight.setText("-");
        }
        price.setText(dataBean.getMoney()+"");
        carLong.setText(carLongData.get(dataBean.getCarLenghtV()));
        carType.setText(bodyTypeData.get(dataBean.getBodyTypeOptionV()));
        bodyType.setText("-");
        remark.setText(dataBean.getMemo());
        if (dataBean.getOssImage()!=null&&!dataBean.getOssImage().isEmpty()){
            Picasso.get().load(dataBean.getOssImage()).into(headImg);
        }else {
            Picasso.get().load(R.drawable.head_img).into(headImg);
        }
        name.setText(dataBean.getContactsNames());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=dataBean.getTels();
                if (str!=null&&!str.trim().isEmpty()) {
                    String[] split = str.split(",");
                    showBottomDialog(Arrays.asList(split));
                }
            }
        });
        ratingBar.setRating(dataBean.getCreditLevel());
        ratingBar.setNumStars(dataBean.getCreditLevel());
        ratingBar.setMax(dataBean.getCreditLevel());
    }

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
        dialogMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                PhoneCallUtils.call(menuList.get(position));
                if (bottomDialog!=null) bottomDialog.dismiss();

            }
        });
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


}
