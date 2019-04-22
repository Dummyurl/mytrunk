package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.XmlHandlers.CitiesModel;
import com.car.portal.XmlHandlers.DistrictModel;
import com.car.portal.XmlHandlers.ProvinceModel;
import com.car.portal.adapter.SelectCityAdapter;
import com.car.portal.entity.City;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.CitysService;
import com.car.portal.util.AutoChangeLine;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.CitySelectedItemView;
import com.car.portal.view.QueryTitleView;

import org.litepal.LitePal;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 多选没有测
 * @author sofa
 */
@SuppressLint("UseSparseArrays")
public class SelectCityActivity extends Activity implements AdapterView.OnItemClickListener{

	public static final int RESPONSE_SINGLE_CODE = 0x1001;
	public static final int RESPONSE_MUTIPLE_CODE = 0x1002;

	@ViewInject(R.id.grid_city)
	private GridView gridView;
	@ViewInject(R.id.selectCity_hasSelect)
	private AutoChangeLine hasSltView;
	@ViewInject(R.id.selectCity_confirm)
	private Button miltButton;
	@ViewInject(R.id.slt_rtn_img)
	private ImageView rtn;
	@ViewInject(R.id.img_found_return)
	private ImageView img_found_return;
	@ViewInject(R.id.img_found_city)
	private ImageView img_found_city;
	@ViewInject(R.id.city_name)
	private EditText city_name;
	private CitysService citysService;
	private SelectCityAdapter selectCityAdapter;
	private static final int MAX_SELECTED = 5;
	private City city;
	private int min_level = 0;
	private boolean isMulty;
	private int max_level = 4;
	private int level;

	private City firstCity;
	private City select_province;
	private City select_city;
	private City select_area;
	private List<City> selects = new ArrayList<City>(MAX_SELECTED);
	private boolean isfound =false;
	private InputMethodManager imm;


    /**
     * 传入参数 minLevel 最小层数，省份为1，城市为2
     * 参数maxLevel 最多层数，最大值4
     * 参数 isMutiple 是不是多选
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
		x.view().inject(SelectCityActivity.this);
		Intent intent = getIntent();
		min_level = intent.getIntExtra("minLevel", 0);
		max_level = intent.getIntExtra("maxLevel", 4);
		isMulty = intent.getBooleanExtra("isMutiple", false);
		List<City> citys = intent.getParcelableArrayListExtra("selectedCities");
		setSelectedCity(citys);
		if(min_level < 0) {
			min_level = 0;
		} else if(min_level >= 4) {
			min_level = 3;
		}
		if(max_level > 4) {
			max_level = 4;
		} else if(max_level < min_level) {
			max_level = 4;
		}
		initTitle();
		citysService = new CitysService(this);
		gridView.setOnItemClickListener(this);
		selectCityAdapter = new SelectCityAdapter(this);
		gridView.setAdapter(selectCityAdapter);
		firstCity = new City();
		firstCity.setCid(0);
		firstCity.setCity("不限");
		firstCity.setId(0);
		firstCity.setNewLevel(0);
		firstCity.setProvice("不限");
		firstCity.setProvice_id(0);
		getProvice();
    }

    /**
     * 初始化标题
     */
    public void initTitle(){
		imm = (InputMethodManager) getSystemService(SelectCityActivity.INPUT_METHOD_SERVICE);
		setCurrentLevel(0);
		img_found_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		img_found_city.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isfound = true;
				imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
				List<CitiesModel> city ;
				List<ProvinceModel> pro;
				List<DistrictModel> dist;
				List<City> cities = new ArrayList<>();
				String cityname = city_name.getText().toString();
				if(cityname.equals("")){
					return;
				}
				pro = LitePal.where("name like ?","%"+cityname+"%").find(ProvinceModel.class);
				city = LitePal.where("name like ?","%"+cityname+"%").find(CitiesModel.class);
				if(pro.size()==0&&city.size()==0){
					Toast.makeText(SelectCityActivity.this, "没有找到"+cityname, Toast.LENGTH_SHORT).show();
				}else {
					for (int i=0;i<pro.size();i++){
						City  city1 = new City();
						city1.setAreaName(pro.get(i).getName());
						city1.setShortName(pro.get(i).getName());
						city1.setCid(pro.get(i).getCid());
						city1.setCity(pro.get(i).getName());
						city1.setCode(pro.get(i).getCode());
						city1.setId(0);
						city1.setLevel(0);
						city1.setNewLevel(1);
						city1.setParentId(0);

						cities.add(city1);
					}

					for (int i=0;i<city.size();i++){
						City  city1 = new City();
						city1.setAreaName(city.get(i).getName());
						city1.setShortName(city.get(i).getName());
						city1.setCid(city.get(i).getCid());
						city1.setCity(city.get(i).getName());
						city1.setCode(city.get(i).getCode());
						city1.setId(0);
						List<ProvinceModel> peovin = LitePal.where("code = ?",city.get(i).getParentCode()+"").find(ProvinceModel.class);
						city1.setLevel(peovin.get(0).getCid());
						city1.setNewLevel(2);
						city1.setParentId(0);
						cities.add(city1);
					}

					setCurrentLevel(0);
					selectCityAdapter.setList(cities);
					selectCityAdapter.notifyDataSetChanged();
				}

			}
		});


		city_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
					img_found_city.performClick();
					return true;
				}
				return false;
			}
		});

		miltButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selects.contains(firstCity)) {
					selects.remove(firstCity);
				}
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra("cities", (ArrayList<City>) selects);
				setResult(RESPONSE_MUTIPLE_CODE,intent);
				finish();
			}
		});
		rtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				level --;
				switch (level) {
					case 1:
						city = select_province;
						getChildByCity();
						break;
					case 2:
						city = select_city;
						getChildByCity();
						break;
					case 3:
						city = select_area;
						getChildByCity();
						break;
					default:
						getProvice();
				}
			}
		});
    }

    /**
     * 获取省份信息
     */
    public void getProvice(){
		city = null;
		citysService.getProvince(new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				List<City> cities = (List<City>) objects[0];
				if(min_level <= 0) {
					if(cities != null) {
						cities.add(0, firstCity);
					}
				}
				setCurrentLevel(0);
				selectCityAdapter.setList(cities);
				selectCityAdapter.notifyDataSetChanged();
			}
		});
    }
    

    /**
     * 获取下级城市
     */
    public void getChildByCity(){
		initSelectCity();
		citysService.getChildByCity(city, new HttpCallBack(this) {
			@Override
			public void onSuccess(Object... objects) {
				@SuppressWarnings("unchecked")
				List<City> cities = (List<City>) objects[0];
				if(min_level <= city.getNewLevel()) {
					cities.add(0, city);
				}
				setCurrentLevel(city.getNewLevel());
				selectCityAdapter.setList(cities);
				selectCityAdapter.notifyDataSetChanged();
			}
		});
    }

	private void setCurrentLevel(int level) {
		this.level = level;
		if(level >= 1) {
			rtn.setVisibility(View.VISIBLE);
		} else {
			rtn.setVisibility(View.GONE);
		}
	}

	private void initSelectCity() {
		if(city==null){
			getProvice();
			return;
		}
		switch (city.getNewLevel()) {
			case 1:
				select_province = city;
				break;
			case 2:
				select_city = city;
				break;
			case 3:
				select_area = city;
				break;
		}
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		city = (City) selectCityAdapter.getItem(position);
		if(isfound == true){
			getChildByCity();
			isfound = false;
		}else if((min_level <= city.getNewLevel() && position == 0 && min_level != 1)
				|| city.getNewLevel() == max_level) {
			selectFinish(position);
		}else if(min_level == 1 &&  city.getNewLevel() == 1 && position == 0 && level == 1) {
			selectFinish(position);
		}
		else {
			getChildByCity();
		}
    }
    
    private void selectFinish(int position) {
    	if(!isMulty) {
			selectCityAdapter.selectNum(position);
			selectCityAdapter.notifyDataSetChanged();
    		Intent intent=new Intent();
    		intent.putExtra("city", city);
    		setResult(RESPONSE_SINGLE_CODE, intent);
            finish();
    	} else {
    		//TODO 多选城市，添加城市到hasSltView
    		if(city.getCid() == 0 && city.getCity().equals("不限")) {
    			city = firstCity;
    			selects.removeAll(selects);
    			hasSltView.removeAllViews();
    			selects.add(city);
    			addSelectedCityView(city);
    		} else if(city.getCid() > 0) {
    			if(selects.contains(firstCity)) {
    				selects.removeAll(selects);
        			hasSltView.removeAllViews();
    			}
	    		if(!selects.contains(city)) {
		    		if(selects.size() == MAX_SELECTED) {
		    			ToastUtil.show("最多只能添加" + MAX_SELECTED + "个", this);
		    			return;
		    		} else {
		    			selects.add(city);
		    			addSelectedCityView(city);
		    			getProvice();
		    			setCurrentLevel(0);
		    		}
	    		} else {
	    			selects.remove(city);
	    			hasSltView.deleteByCid(city);
	    			getProvice();
	    			setCurrentLevel(0);
	    		}
	    	}

    		if(selects.size() > 0) {
    			miltButton.setVisibility(View.VISIBLE);
    		} else {
    			miltButton.setVisibility(View.GONE);
    		}
    	}
    }

	private void addSelectedCityView(City c) {
		LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		CitySelectedItemView view = new CitySelectedItemView(this);
		view.setLayoutParams(p);
		view.setContent(c.getCity());
		view.setDeleteListener(new CitySelectedItemView.DeleteListener() {
			@Override
			public void onClick(View v, City c) {
				hasSltView.deleteByCid(c);
				selects.remove(c);
				if(selects.size() > 0) {
					miltButton.setVisibility(View.VISIBLE);
				} else {
					miltButton.setVisibility(View.GONE);
				}
			}
		});
		view.setCity(c);
		hasSltView.addView(view);
	}

	public void setSelectedCity(List<City> citys) {
		if(isMulty) {
			if(citys != null) {
				for (City city : citys) {
					if(!selects.contains(city)) {
						selects.add(city);
						addSelectedCityView(city);
					}
				}
				if(selects.size() > 0) {
					miltButton.setVisibility(View.VISIBLE);
				} else {
					miltButton.setVisibility(View.GONE);
				}
			}
		}
	}
}
