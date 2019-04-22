package com.car.portal.util;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.car.portal.entity.CallLogBean;
import com.car.portal.entity.User;

@SuppressLint("SdCardPath")
public class MyDBUtil {

    private static final String DBName = "portalWeb.db";
    private static final int DBversion = 2;
    public static boolean needUpdate = false;

    private MyDBUtil() {
    }

    @SuppressLint("NewApi")
    public static DaoConfig getConfig() {
        return new DbManager.DaoConfig().setDbName(DBName)
                .setDbVersion(DBversion)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager manager) {
                        String isExistBean = "SELECT count(*) FROM sqlite_master WHERE " +
                                "type='table' AND name='callbean'";
                        String isExistCity = "SELECT count(*) FROM sqlite_master WHERE " +
                                "type='table' AND name='city'";
                        String isExitUser = "SELECT count(*) FROM sqlite_master WHERE " +
                                "type='table' AND name='user'";
                        try {
                            Cursor cursor = manager.execQuery(isExistCity);
                            cursor.moveToFirst();
                            if (cursor.getInt(0) == 0) {
                                String create = "CREATE TABLE `city` (" +
                                        "`id` int," +
                                        "`cid` int,`city` varchar(50)," +
                                        "`ower` int," +
                                        "`provice` varchar(12) DEFAULT '0'," +
                                        "`provice_id` int DEFAULT '0'," +
                                        "`level` int(1) DEFAULT '0'," +
                                        "`isSelect` int(1) DEFAULT '1'," +
                                        "`code` int(11) DEFAULT '0'," +
                                        "`areaName` varchar(45)," +
                                        "`parentId` int DEFAULT '0'," +
                                        "`parentCode` int(11) DEFAULT '0'," +
                                        "`shortName` varchar(45)," +
                                        "`newLevel` tinyint(1) DEFAULT '0'," +
                                        "`sort` tinyint(1) DEFAULT '0',PRIMARY KEY (`id`)" +
                                        ")";
                                manager.execNonQuery(create);

                                cursor = manager.execQuery(isExistBean);
                                cursor.moveToFirst();
                                if (cursor.getInt(0) == 0) {
                                    String create_bean = "CREATE TABLE `callbean` (" +
                                            "`id` int," +
                                            "`name` varchar(100)," +
                                            "`phone` varchar(50)," +
                                            "`date` varchar(100)," +
                                            "`time` varchar(100)" +
                                            ")";
                                    manager.execNonQuery(create_bean);
                                }

                                cursor = manager.execQuery(isExitUser);
                                cursor.moveToFirst();
                                if (cursor.getInt(0) == 0) {
                                    String create_user = "CREATE TEBLE `user` (" +
                                    		"`uid` int," +
                                    		"`cname` varchar(50)," +
                                    		"`alias` varchar(10)," +
                                    		"`companyId` int," +
                                    		"`userType` int" +
                                    	")";
                                    manager.execNonQuery(create_user);
                                }
                            }
                            cursor.close();
                        } catch (DbException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager manager, int arg1, int arg2) {
                        if (arg1 != arg2) {
                            needUpdate = true;
                            try {
                                manager.dropTable(User.class);
                            } catch (DbException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    public static DaoConfig getCallBeanDB() {
        return new DbManager.DaoConfig().setDbName(DBName)
                .setDbVersion(DBversion)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager manager) {
                        String isExistCity = "SELECT count(*) FROM sqlite_master WHERE " +
                                "type='table' AND name='callbean'";
                        try {
                            Cursor cursor = manager.execQuery(isExistCity);
                            cursor.moveToFirst();
                            if (cursor.getInt(0) == 0) {
                                String create = "CREATE TABLE `callbean` (" +
                                        "`id` int," +
                                        "`name` varchar(100)," +
                                        "`phone` varchar(50)," +
                                        "`date` varchar(100)," +
                                        "`time` varchar(100)" +
                                        ")";
                                manager.execNonQuery(create);
                            }
                            cursor.close();
                        } catch (DbException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager manager, int arg1, int arg2) {
                        //TODO changeDatabase
                        if (arg1 != arg2) {
                            needUpdate = true;
                            try {
                                manager.dropTable(CallLogBean.class);
                            } catch (DbException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                });
    }

    public static DbManager getManager() {
        return x.getDb(getConfig());
    }
}
