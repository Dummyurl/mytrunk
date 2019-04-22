package com.car.portal.util;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.car.portal.application.MyApplication;
import com.car.portal.service.UserService;

import java.io.File;

public class DownLoadUtil {
	
	private static final String TAG = "DownLoadUtil";
	
	private Context context;
	private UserService userService;
	private DownloadManager dm;
	
	private DownLoadUtil (Context context){
		this.context = context;
		userService = new UserService(context);
		dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	public static DownLoadUtil getIntance(Context context) {
		return new DownLoadUtil(context);
	}
	
	public void downLoadBySystem(String url) {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(trimUrl(url)));
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
		// 设置通知栏标题
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
		request.setVisibleInDownloadsUi(true);
		request.setTitle("货满车");
		request.setDescription("货满车 更新包，正在下载。");
		request.setAllowedOverRoaming(false);
		request.allowScanningByMediaScanner();
		// 设置文件存放目录
		request.setMimeType("application/vnd.android.package-archive");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CarCompany.apk");
		long enqueue = dm.enqueue(request);
		userService.saveValue("apk_loadId", String.valueOf(enqueue));
	}
	
	private String trimUrl(String url) {
		String s_url = userService.getHost() + "../" + url;
		s_url = s_url.replace("//", "/");
		s_url = s_url.replace("http:/", "http://");
		return s_url;
	}

	
	public void downLoadBySystem(Uri url, String saveKey) {
		DownloadManager.Request request = new DownloadManager.Request(url);
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
		// 设置通知栏标题
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
		request.setTitle("货满车");
		request.setDescription("正在下载更新包");
		request.setAllowedOverRoaming(false);
		request.allowScanningByMediaScanner();
		// 设置文件存放目录
		request.setMimeType("application/vnd.android.package-archive");
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CarCompany.apk");
		long enqueue = dm.enqueue(request);
		userService.saveValue(saveKey, String.valueOf(enqueue));
	}

	public void downLoad(final String url) {
		LogUtils.e(TAG, "local Version: " + MyApplication.localVersion + "\tserverVersion:" + MyApplication.serverVersion);
		if (MyApplication.localVersion < MyApplication.serverVersion) {
			if (FileUtil.SDCardisExist()) {
				downLoadBySystem(url);
			} else {
				try{
					String s_url = trimUrl(url);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(s_url));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} catch(Exception e) {
					BaseUtil.writeFile(TAG, e);
					e.printStackTrace();
				}
			}
		} else {
			File f = new File(FileUtil.getDownPath(), BaseUtil.APK_FILE_NAME);
			if (f.exists()) {
				 f.delete();
			}
		}
	}
	
	public static String queryDownTask(DownloadManager downManager, long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor= downManager.query(query);
        cursor.moveToFirst();
        String address = null;
        if(!cursor.isAfterLast()){
            address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        }
        cursor.close();
        return address;
    }

	@Override
	protected void finalize() throws Throwable {
		dm = null;
		userService = null;
		context = null;
		super.finalize();
	}
}
