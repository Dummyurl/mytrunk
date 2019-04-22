package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.car.portal.entity.City;

import android.view.View;
import android.widget.BaseAdapter;

public abstract class CityBaseAdapter extends BaseAdapter {
	protected List<City> values;
	protected View.OnClickListener listener;
	protected FocusChangeListener focusChangeListener;
	public abstract String getSelectCityNames();
	public abstract String getSelectCityIds();
	public abstract City getSelectCity();
	public abstract List<City> getSelectCitys();
	public abstract void refreshChecked();
	
	public void setValues(List<City> values) {
		this.values = values;
	}

	public void addValue(City city) {
		if(this.values == null) {
			values = new ArrayList<City>();
		}
		if(!values.contains(city)) {
			values.add(city);
		}
	}

	public void removeValue(City city) {
		if (values != null) {
			values.remove(city);
		}
	}
	
	public void setFocusChangeListener(FocusChangeListener listener) {
		focusChangeListener = listener;
	}
	
	public void setItemClickListenter(View.OnClickListener listener) {
		this.listener = listener;
	}
	
	public static interface FocusChangeListener {
		void changed();
	}

	public abstract boolean isMultiple();

	public abstract  void setFocusCity(List<City> city);
}
