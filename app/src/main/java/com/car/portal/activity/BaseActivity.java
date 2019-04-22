package com.car.portal.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.car.portal.application.MyApplication;
import com.car.portal.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

@TargetApi(23)
public class BaseActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (MyApplication.osVersion >= 23) {
			insertDummyContactWrapper();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
		case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
			LogUtils.e("BaseActivity", permissions.toString() + grantResults.toString());
			if(permissions != null) {
				int index = 0;
				for (String string : permissions) {
					if(index < grantResults.length) {
						LogUtils.e("BaseActivity", string);
					}
					index ++;
				}
			}
			break;
		default:
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

	private void insertDummyContactWrapper() {
		final List<String> permissionsList = new ArrayList<String>();
		boolean res = addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE);
		res = addPermission(permissionsList, Manifest.permission.WRITE_SETTINGS);
		res = addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		res = addPermission(permissionsList, Manifest.permission.CALL_PHONE);
		res = addPermission(permissionsList, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
		res = addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION);
		res = addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION);
		if(permissionsList!=null && permissionsList.size()>0)
		requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			if (!shouldShowRequestPermissionRationale(permission)) {
				return false;
			}
		}
		return true;
	}
}
