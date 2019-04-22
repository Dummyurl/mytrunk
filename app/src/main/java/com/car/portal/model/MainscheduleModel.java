package com.car.portal.model;

import android.content.Context;
import com.car.portal.entity.Company;
import com.car.portal.entity.MainscheduleEntity;
import com.car.portal.entity.PortalDriver;
import com.car.portal.entity.UserDetail;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.view.ICallback;
public class MainscheduleModel {
    /**
     *
     *@作者 Administrator
     *@时间 2019/3/19 0019 上午 11:57
     *  判断用户的资料是否完成
     */
    public void ischeckuser(Context context, final PortalDriver driver, final UserDetail userDetail, final ICallback<MainscheduleEntity> callback) {
            UserService userService = new UserService(context);
            userService.getMyCompany(new HttpCallBack(context) {
                @Override
                public void onSuccess(Object... objects) {
                    if (objects != null && objects.length > 0) {
                        Company companyInfo = (Company) objects[0];
                        MainscheduleEntity mainscheduleEntity = new MainscheduleEntity();
                        //设置初始值
                        mainscheduleEntity.setHeadImg(true);
                        mainscheduleEntity.setDriver_check(true);
                        mainscheduleEntity.setCompany_info(true);
                        mainscheduleEntity.setDriver_info(true);
                        try {
                            if(driver.getDriverLicense().equals("")
                                    || driver.getDrivingImage().equals("")
                                    ||driver.getIdentImage().equals("")
                                    ||driver.getPersonalImage().equals("")){
                                mainscheduleEntity.setDriver_check(false);
                            }
                        }catch (Exception e){
                            mainscheduleEntity.setDriver_check(false);
                        }

                        try{
                            if(userDetail.getPersonImage().equals("")){
                                mainscheduleEntity.setHeadImg(false);
                            }
                        }catch (Exception e){
                            mainscheduleEntity.setHeadImg(false);
                        }

                        try{
                            if(userDetail.getTel().equals("")
                                    ||userDetail.getUsername().equals("")
                                    ||userDetail.getIdcard().equals("")){
                                mainscheduleEntity.setDriver_info(false);
                            }
                        }catch (Exception e){
                            mainscheduleEntity.setDriver_info(false);
                        }


                        try{
                            if(companyInfo.getName().equals("")||companyInfo.getAlias().equals("")||companyInfo.getAddress().equals("")||companyInfo.getCity().equals("")||companyInfo.getOffice_tel().equals("")
                                    ||companyInfo.getFax().equals("")||companyInfo.getLegaler().equals("")||companyInfo.getTel().equals("")||companyInfo.getDecs().equals("")||companyInfo.getLogo().equals("")){
                                mainscheduleEntity.setCompany_info(false);
                            }
                        }catch (Exception e){
                            mainscheduleEntity.setCompany_info(false);
                        }
                        callback.onSucceed(mainscheduleEntity);
                    }
                }
            });

    }


}
