package com.car.portal.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.car.portal.activity.MainActivity;

import java.io.File;

@SuppressLint("NewApi")
public class DownloadCompleteReceiver extends BroadcastReceiver {
    private DownloadManager manager ;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            //通过downloadId去查询下载的文件名
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor myDownload = manager.query(query);
            if (myDownload.moveToFirst()) {
                int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String fileUri = myDownload.getString(fileUriIdx);
                String fileName = null;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (fileUri != null) {
                        fileName = Uri.parse(fileUri).getPath();
                    }
                } else {
                    //Android 7.0以上的方式：请求获取写入权限，这一步报错
                    //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
                    int filenameIdx =myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    fileName = myDownload.getString(filenameIdx);
                }
                installAPK(fileName,context);
            }
        }
    }

    //安装APK
    private void installAPK(String  filePath,Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(filePath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.car.portal.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}

