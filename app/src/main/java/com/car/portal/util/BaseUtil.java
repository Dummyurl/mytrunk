package com.car.portal.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.car.portal.R;
import com.car.portal.activity.MainActivity;
import com.car.portal.application.MyApplication;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class BaseUtil {
	
	public static final int MAX_SIZE = 1024 * 1024;
	public static final int NOTIFICAT_ID = 500;
	public static final String DOWNLOAD_LOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
	public static final String APK_FILE_NAME = "/CarCompany.apk";




    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
	public static boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static File compressImage(String filePath, String fileName) throws IOException {
		Bitmap bm = getSmallBitmap(filePath);
		File imageDir = new File(filePath);
		File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/uploadCache/");
		if(!outDir.exists()) {
			outDir.mkdirs();
		}
		File outputFile = new File(outDir, fileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		int q = (int) ((100 * MAX_SIZE) / (imageDir.length() + MAX_SIZE - 1));
		FileOutputStream out = new FileOutputStream(outputFile);
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);
		bm = null;
		out.close();
		imageDir = null;
		outDir = null;
		return outputFile;
	}


	public static void Downloadapk(Context context,String url){
        DownloadManager manager =(DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        MyHttpUtil util = new MyHttpUtil(context);
        String downurl = util.getHost() + url.substring(1) + "?t=" + System.currentTimeMillis();
        DownloadManager.Request down=new DownloadManager.Request(Uri.parse(downurl));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        down.setVisibleInDownloadsUi(true);
        down.setTitle("货满车货主");
        down.setDescription("请稍后，正在下载中");
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        down.setDestinationInExternalFilesDir(context, null,"huomanche.apk");
        Long downloadId = manager.enqueue(down);
        DownloadCompleteReceiver downloadCompleteReceiver = new DownloadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadCompleteReceiver,intentFilter);
    }

	public static boolean isMobileNO(String mobiles) {
		String telRegex = "^((1[3,5,7,8][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}


    /**
     *
     *@作者 shuye
     *@时间 2019/3/28 0028 下午 3:16
     *  判断通知是否打开
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return isEnableV26(context);
        } else {
            return isEnableV19(context);
        }
    }


    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
     * 19及以上
     *
     * @param context
     * @return
     */
    public static boolean isEnableV19(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {

        } catch (NoSuchFieldException e) {

        } catch (InvocationTargetException e) {

        } catch (IllegalAccessException e) {

        }
        return false;
    }


    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     * 针对8.0及以上设备
     *
     * @param context
     * @return
     */
    public static boolean isEnableV26(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {

        }
        return false;
    }

    /**
     *
     *@作者 shuye
     *@时间 2019/3/28 0028 下午 3:15
     * 跳转到系统打开通知的页面
     */
    public static void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, 1);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                PackageManager packageManager =  activity.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
                intent.setData(Uri.parse("package:" + packageInfo.packageName));
                activity.startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, 1);
        }
    }



	
	public static void installApk(Context context, File cacheFile) throws IOException {
		File newFile = new File(cacheFile.getParentFile(), BaseUtil.APK_FILE_NAME);
		newFile.deleteOnExit();
		FileUtil.copyFile(cacheFile, newFile);
		BaseUtil.writeFile("LoginActivity", "" + cacheFile.length() + "    :   " + newFile.length());
		if(newFile.length() == cacheFile.length()) {
			cacheFile.deleteOnExit();
		} else {
			newFile.deleteOnExit();
			newFile = cacheFile;
		}
		if(newFile.length() == 0) {
			ToastUtil.show("安装包下载失败，请手动下载更新", context);
			return;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
			Uri apkUri = FileProvider.getUriForFile(context, "com.winterrunner.vandroid70_install_bug.fileprovider", newFile);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// 由于没有在Activity环境下启动Activity,设置下面的标签
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
			context.startActivity(intent);
		}else {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(newFile), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static void msgNotigy(Context context, int currentId, int msgSize) {
		UserService service = new UserService(context);
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		int timeType = Integer.valueOf(service.getNomalValue("timeTip", "1"));
		String startTime = service.getNomalValue("startTime", "08:00");
		String endTime = service.getNomalValue("endTime","22:00");
		boolean isVictor = StringUtil.getBooleanValue(service.getNomalValue("isVictor", "true"));
		boolean isVoice = StringUtil.getBooleanValue(service.getNomalValue("isVoice", "true"));
		if(currentId > 0) {
			service.saveValue("messageId", currentId + "");
		}
		if(timeType < 0) {
			return;
		} else if(timeType == 0) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String nowTime = format.format(new Date());
			StringBuffer bf = new StringBuffer(startTime);
			bf.deleteCharAt(2);
			int start = Integer.valueOf(bf.toString());
			bf = new StringBuffer(endTime);
			bf.deleteCharAt(2);
			int end = Integer.valueOf(bf.toString());
			bf = new StringBuffer(nowTime);
			bf.deleteCharAt(2);
			int now = Integer.valueOf(bf.toString());
			//开始时间小于结束时间时  小于开始时间或大于结束时间返回
			if(start <= end && (end < now || start > now)) {
				return;
			//开始时间大于结束时间时 大于结束时间且小于开始时间返回
			} else if(start > end && start > now && end < now) {
				return;
			}
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.message)
				.setContentTitle(context.getResources().getString(R.string.apk_name))
				.setContentText("您有新消息");
		if(isVoice)
			builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		if(isVictor)
			builder.setVibrate(new long[] { 200, 200, 200, 200 });
		builder.setLights(Color.RED, 1000, 1000);
		builder.setTicker("新消息");// 第一次提示消息的时候显示在通知栏上
		builder.setNumber(msgSize);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.message));
		builder.setAutoCancel(true);
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(pIntent);
		manager.notify(NOTIFICAT_ID, builder.build());
		manager = null;
		builder = null;
	}

	public static void writeFile(String tag, Object obj) {
		//已经由Bugly代替，并且更加好用。
		/*if(obj == null) {
			return;
		}else {
			String now = getNowTime();
			File outDir = new File(Environment.getExternalStoragePublicDirectory(".").getPath() + "/logs/");
			try {
				if (!outDir.exists()) {
					outDir.mkdirs();
				}
				File f = new File(outDir.getAbsoluteFile() + "/portal_" + now.substring(0, 10) + ".txt");
				f.setWritable(true, false);
				FileOutputStream out = new FileOutputStream(f, true);
				OutputStreamWriter writer = new OutputStreamWriter(out);
				writer.write(now);
				writer.write("\t");
				writer.write(tag);
				writer.write("\t");
				if (obj instanceof String) {
					writer.write(obj.toString());
				} else if (obj instanceof RuntimeException) {
					RuntimeException e = (RuntimeException) obj;
					writer.write(e.getMessage());
					writer.write("\n");
					StackTraceElement[] es = e.getStackTrace();
					if (es != null) {
						for (StackTraceElement ele : es) {
							writer.write("\t\t\t");
							writer.write("at   ");
							writer.write(ele.getClassName());
							writer.write("   ");
							writer.write(ele.getMethodName());
							writer.write("(line  ");
							writer.write(String.valueOf(ele.getLineNumber()));
							writer.write(")");
							writer.write("\n");
							writer.flush();
						}
					}
				} else {
					writer.write(obj.toString());
					writer.flush();
				}
				writer.write("\n");
				writer.close();
				out.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}*/
	}
	
	public static void clearLogs() {
		Long l = System.currentTimeMillis();
		long dep = 3600 * 100 * 24;
		File outDir = new File(Environment.getExternalStoragePublicDirectory(".").getPath() + "/logs/");
		if(outDir.exists()) {
			File[] f = outDir.listFiles();
			for (File file : f) {
				if(l - file.lastModified() > dep) {
					file.delete();
				}
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getNowTime() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sp.format(new Date());
	}

	public static boolean isTopActivy(String serviceName) {
		MyApplication.getContext();
		ActivityManager amanager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningServiceInfo> services = amanager.getRunningServices(Integer.MAX_VALUE);
		for (RunningServiceInfo runningServiceInfo : services) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	public static void installApk(Context context, Uri installUri) {
		try{
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);  
			intent.setDataAndType(installUri, "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch(Exception e) {
			e.printStackTrace();
			BaseUtil.writeFile("更新APP", e);
		}
	}


	/**
	 * 拨打电话（跳转到拨号界面，用户手动点击拨打）
	 *
	 * @param phoneNum 电话号码
	 */ public static void diallPhone(Context context,String phoneNum) {
	 	Intent intent = new Intent(Intent.ACTION_DIAL);
	 	Uri data = Uri.parse("tel:" + phoneNum);
	 	intent.setData(data);
	 	context.startActivity(intent);
	 }

}
