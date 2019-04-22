package com.car.portal.fragment;

import android.support.v4.app.Fragment;

import com.jph.takephoto.app.TakePhotoFragment;

public abstract class MainBaseFragment extends TakePhotoFragment {
	
	public abstract void search(String condition);
	
	public abstract void onWindowFoucusChanged(boolean hasFocus);

}
