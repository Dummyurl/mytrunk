package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.XmlHandlers.CitiesModel;
import com.car.portal.XmlHandlers.CityParserHandler;
import com.car.portal.XmlHandlers.DistrictModel;
import com.car.portal.XmlHandlers.DistrictParserHandler;
import com.car.portal.XmlHandlers.ProvinceModel;
import com.car.portal.adapter.ProvinceAdapter;
import com.car.portal.util.SharedPreferenceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.litepal.LitePal;
import org.xml.sax.SAXException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CitySelectActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, BaseQuickAdapter.OnItemChildClickListener {


    @ViewInject(R.id.action)
    TextView action;
    @ViewInject(R.id.close)
    ImageButton close;
    @ViewInject(R.id.radio_group)
    RadioGroup radioGroup;
    @ViewInject(R.id.radio_one)
    RadioButton radioOne;
    @ViewInject(R.id.radio_two)
    RadioButton radioTwo;
    @ViewInject(R.id.radio_three)
    RadioButton radioThree;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.parent)
    LinearLayout parent;
    @ViewInject(R.id.candidate0)
    TextView candidate0;
    @ViewInject(R.id.candidate1)
    TextView candidate1;
    @ViewInject(R.id.candidate2)
    TextView candidate2;
    private List<ProvinceModel> provinceModelList;
    private List<DistrictModel> districtsModelList;
    private List<CitiesModel> citiesModelList;
    private ProvinceAdapter provinceAdapter;
    private List<CitiesModel> names;
    private String saveAction;
    private List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city_select);

        names = new ArrayList<>();
        x.view().inject(this);
        close.setOnClickListener(this);
        parent.setOnClickListener(this);
        InitIntentData();
        InitProvinceData();
        InitTabViews();
        InitRecyclerView();
        InitCandidate();
    }

    private void InitIntentData() {
        Intent intent = getIntent();
        saveAction = intent.getStringExtra("action");
        action.setText(intent.getStringExtra("title"));
        if (saveAction==null||saveAction.trim().isEmpty()){
            throw new NullPointerException("save select data,please input the only key");
        }
    }

    private void InitRecyclerView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        provinceAdapter = new ProvinceAdapter(R.layout.item_city, names);
        provinceAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(provinceAdapter);
    }

    private void InitCandidate() {
        cities = SharedPreferenceUtil.ObtainCity(saveAction);
        candidate0.setOnClickListener(this);
        candidate1.setOnClickListener(this);
        candidate2.setOnClickListener(this);

        if (cities != null && cities.size() != 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (cities.size() >= 1 && cities.get(0) != null) {
                    candidate0.setVisibility(View.VISIBLE);
                    candidate0.setText(cities.get(0).split(" ")[2]);
                }
                if (cities.size() >= 2 && cities.get(1) != null) {
                    candidate1.setVisibility(View.VISIBLE);
                    candidate1.setText(cities.get(1).split(" ")[2]);
                }
                if (cities.size() == 3 && cities.get(2) != null) {
                    candidate2.setVisibility(View.VISIBLE);
                    candidate2.setText(cities.get(2).split(" ")[2]);
                }
            }
        }
    }

    private void InitTabViews() {
        radioGroup.setOnCheckedChangeListener(this);

    }

    private void InitProvinceData() {
        provinceModelList = LitePal.findAll(ProvinceModel.class);
        for (ProvinceModel provinceModel : provinceModelList) {
            CitiesModel model = new CitiesModel();
            model.setCode(provinceModel.getCode());
            model.setId(provinceModel.getId());
            model.setCid(provinceModel.getCid());
            model.setName(provinceModel.getName());
            model.setParentCode(provinceModel.getParentCode());
            names.add(model);
        }

    }


    private void InitCity() {
        try {
            InputStream inputStream = getAssets().open("Cities.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            CityParserHandler handler = new CityParserHandler();
            parser.parse(inputStream, handler);
            inputStream.close();
            citiesModelList = handler.getDataList();
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
            InputStream inputStream = getAssets().open("Districts.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            DistrictParserHandler handler = new DistrictParserHandler();
            parser.parse(inputStream, handler);
            inputStream.close();
            districtsModelList = handler.getDataList();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    private int code;
    private CitiesModel defaultCityInfo;

    private int addressPosition = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
            case R.id.parent:
                finish();
                break;
            case R.id.candidate0:
                addressPosition = 0;
                break;
            case R.id.candidate1:
                addressPosition = 1;
                break;
            case R.id.candidate2:
                addressPosition = 2;
                break;

        }
        if (addressPosition != -1) {
            String province = cities.get(addressPosition).split(" ")[0];
            String city = cities.get(addressPosition).split(" ")[1];
            String district = cities.get(addressPosition).split(" ")[2];
            String code = cities.get(addressPosition).split(" ")[3];
            ReBackData(province, city, district, code);
        }
    }

    private int currentPosition = 0;
    private int secondlyCode;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        names.clear();
        switch (i) {
            case R.id.radio_one:
                if (currentPosition == 2) {
                    radioTwo.setText(radioOne.getText().toString());
                }
                currentPosition = 0;
                InitProvinceData();

                break;
            case R.id.radio_two:
                if (radioOne.getText().equals("省") && radioTwo.getText().equals("市")) {
                    radioOne.performClick();
                    Toast.makeText(this, "请先选择省", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPosition = 1;
               /* for (CitiesModel citiesModel : citiesModelList) {
                    if (citiesModel.getParentCode() == secondlyCode) {

                        names.add(citiesModel);
                    }
                }*/
                List<CitiesModel> citiesModels = LitePal.where("parentCode =?", "" + code).find(CitiesModel.class);
                names.addAll(citiesModels);


                break;
            case R.id.radio_three:
                if (radioTwo.getText().equals("市") && radioThree.getText().equals("区")) {
                    radioTwo.performClick();
                    Toast.makeText(this, "请先选择市", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPosition = 2;

                List<DistrictModel> districtModels = LitePal.where("parentCode =?", "" + code).find(DistrictModel.class);
                for (DistrictModel districtModel : districtModels) {
                    CitiesModel model = new CitiesModel();
                    model.setCode(districtModel.getCode());
                    model.setId(districtModel.getId());
                    model.setCid(districtModel.getCid());
                    model.setName(districtModel.getName());
                    model.setParentCode(districtModel.getParentCode());
                    names.add(model);
                }

                if (defaultCityInfo != null)
                    names.add(0, defaultCityInfo);
                break;

        }
        provinceAdapter.notifyDataSetChanged();
        if (currentPosition == 1 && names.size() == 1) {

            provinceAdapter.getViewByPosition(recyclerView, 0, R.id.name).performClick();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


        switch (view.getId()) {
            case R.id.name:


                code = names.get(position).getCode();
                switch (currentPosition) {
                    case 0:

                        secondlyCode = code;
                        radioOne.setText(names.get(position).getName());
                        radioTwo.setChecked(true);
                        break;
                    case 1:

                        if (names.size() == 1) {
                            radioTwo.setText(names.get(0).getName());
                        } else {
                            radioTwo.setText(names.get(position).getName());

                        }
                        defaultCityInfo = new CitiesModel();
                        defaultCityInfo.setId(-1);
                        defaultCityInfo.setCode(names.get(position).getCode());
                        defaultCityInfo.setName("全" + names.get(position).getName());
                        radioThree.setChecked(true);

                        break;

                    case 2:
                        radioThree.setText(names.get(position).getName());
                        String province = radioOne.getText().toString();
                        String city = radioTwo.getText().toString();
                        String district = radioThree.getText().toString();
                        int code = names.get(position).getCode();
                        ReBackData(province, city, district, code + "");
                        break;
                }

                break;
        }

    }

    /**
     * @param province 省
     * @param city     市
     * @param district 区
     * @param code     邮编
     *                 <p>
     *                 其他需要返回到上级界面的数据在这里返回就行
     */
    private void ReBackData(String province, String city, String district, String code) {
        Intent intent = getIntent();
        intent.putExtra("province", province);
        intent.putExtra("city", city);
        intent.putExtra("district", district);
        intent.putExtra("code", code);
        intent.putExtra("action", saveAction);
        setResult(0, intent);
        finish();
    }


    public class cityInfo {
        private int id;
        private int cid;
        private String name;
        private int code;
        private int parentCode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getParentCode() {
            return parentCode;
        }

        public void setParentCode(int parentCode) {
            this.parentCode = parentCode;
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();

    }
}
