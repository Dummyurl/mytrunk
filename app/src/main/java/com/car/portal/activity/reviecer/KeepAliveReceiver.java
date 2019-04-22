package com.car.portal.activity.reviecer;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.car.portal.activity.service.MainService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.DownLoadUtil;

public class KeepAliveReceiver extends BroadcastReceiver {
	
	private static final String BASE_ACTION = "com.car.portal.receiver.KeepAlive";
	
	public KeepAliveReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(BASE_ACTION)) {
			if(BaseUtil.isTopActivy(MainService.class.getName())) {
				Intent mIntent = new Intent(context, MainService.class);
				context.startService(mIntent);
			}
		} else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			if(BaseUtil.isTopActivy(MainService.class.getName())) {
				Intent mIntent = new Intent(context, MainService.class);
				context.startService(mIntent);
			}
		} else if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			long refernece = Long.valueOf(new UserService(context).getNomalValue("apk_loadId", "0"));
			if(refernece == myDwonloadID) {
				new UserService(context).saveValue("apk_loadId", "0");
				String serviceString = Context.DOWNLOAD_SERVICE;
				DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
				String url = DownLoadUtil.queryDownTask(dManager, myDwonloadID);
				if(url != null) {
					Uri downloadFileUri = Uri.parse(url);
					BaseUtil.installApk(context, downloadFileUri);
				}
			}
		}
	}
}
