package com.car.portal.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.car.portal.application.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressLint("NewApi")
public class FileUtil {
	private static final Context context = MyApplication.getContext();
	private static final String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();// sd卡路径
	private static final String SDState = Environment.getExternalStorageState();// SD卡状态

	/**
	 * 文件存储到/data/data/<packagename>/files/默认目录下
	 * @param fileName
	 * @param bytes
	 * @return
	 */
	public static boolean write2CacheFile(String fileName, byte[] bytes) {
		FileOutputStream out = null;
		BufferedOutputStream bos = null;
		try {
			out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bos = new BufferedOutputStream(out);
			bos.write(bytes);
			bos.flush();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			BaseUtil.writeFile("FileUtil", e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					BaseUtil.writeFile("FileUtil", e);
				}
			}
		}
		return false;
	}

	/**
	 * 向SD卡里写字节
	 * @param path 文件夹目录
	 * @param file 文件名
	 * @param data 写入的字节数组
	 * @return
	 */
	public static boolean writeBytes(String path, String file, byte[] data) {
		FileOutputStream fos = null;
		try {
			// 拥有足够的容量
			if (data.length < getSDFreeSize()) {
				createDirectoryIfNotExist(path);
				createFileIfNotExist(path + file);
				fos = new FileOutputStream(path + File.separator + file);
				fos.write(data);
				fos.flush();
				return true;
			}
		} catch (Exception e) {
			BaseUtil.writeFile("FileUtil", e);
			LogUtils.e("writeBytes", e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					BaseUtil.writeFile("FileUtil", e);
				}
			}
		}
		return false;
	}

	/**
	 * 从SD卡里读取字节数组
	 * @param path 目录
	 * @param fileName 文件名
	 * @return 返回字节数组，文件不存在返回null
	 */
	public static byte[] readBytes(String path, String fileName) {
		File file = new File(path + File.separator + fileName);
		if (!file.exists()) {
			return null;
		}
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			return data;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			BaseUtil.writeFile("FileUtil", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				BaseUtil.writeFile("FileUtil", e);
			}
		}
		return null;
	}

	/**
	 * 将一个字节流写入到SD卡文件
	 * @param path 目录路径
	 * @param fileName 文件名
	 * @param input 字节流
	 * @return
	 */
	public static Boolean write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			int size = input.available();
			// 拥有足够的容量
			if (size < getSDFreeSize()) {
				createDirectoryIfNotExist(path);
				createFileIfNotExist(path + File.separator + fileName);
				file = new File(path + File.separator + fileName);
				output = new BufferedOutputStream(new FileOutputStream(file));
				byte buffer[] = new byte[1024];
				int temp;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
				}
				output.flush();
				return true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			BaseUtil.writeFile("FileUtil", e1);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				BaseUtil.writeFile("FileUtil", e);
			}
		}
		return false;
	}

	/**
	 * 判断SD卡是否存在
	 * @ return
	 */
	public static boolean SDCardisExist() {
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			if(MyApplication.osVersion >= 23) {
				int res = MyApplication.getContext().checkSelfPermission(
						Manifest.permission.WRITE_EXTERNAL_STORAGE);
				return res == PackageManager.PERMISSION_GRANTED;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean loadBySelf() {
		if(MyApplication.osVersion >= 23) {
			int res = MyApplication.getContext().checkSelfPermission(
					Manifest.permission.WRITE_EXTERNAL_STORAGE);
			return res == PackageManager.PERMISSION_GRANTED;
		} else {
			return true;
		}
	}

	/**
	 * 获取SD卡剩余容量大小（单位Byte）
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSizeLong();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocksLong();
		// 返回SD卡空闲大小
		return freeBlocks * blockSize; // 单位Byte
	}

	/**
	 * 获取SD卡总容量大小（单位Byte）
	 * @return
	 */
	public static long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSizeLong();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCountLong();
		// 返回SD卡大小
		return allBlocks * blockSize; // 单位Byte
	}

	/**
	 * 如果目录不存在，就创建目录
	 * @param path 目录
	 * @return 文件夹存在返回true
	 */
	public static boolean createDirectoryIfNotExist(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			return file.mkdirs();
		} else {
			LogUtils.e("目录", "目录存在！");
			return true;
		}
	}

	/**
	 * 如果文件不存在，就创建文件
	 * @param path 文件路径
	 * @return 文件夹存在返回true
	 */
	public static boolean createFileIfNotExist(String path) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				return file.createNewFile();
			} else {
				return true;
			}
		} catch (Exception e) {
			LogUtils.e("error", e.getMessage());
			return false;
		}
	}
	
	public static String getFileBasePath() {
		boolean res = SDCardisExist();
		if(res) {
			return SDCardPath;
		} else {
			return context.getFilesDir().getAbsolutePath();
		}
	}
	
	public static String getDownPath() {
		if (SDCardisExist()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
		} else {
			return context.getCacheDir().getAbsolutePath();
		}
	}
	
	public static void copyFile(File src, File decs) throws IOException {
		if(src != null && decs != null) {
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(decs);
			byte[] bts = new byte[1024];
			int len = 0;
			while ((len = in.read(bts)) >= 0) {
				out.write(bts, 0, len);
			}
			in.close();
			out.close();
		}
	}
	
}
