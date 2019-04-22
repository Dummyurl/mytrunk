package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.ContactDTO;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;
import com.car.portal.service.GoodsService;
import com.car.portal.util.LogUtils;
import com.car.portal.view.BaseTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class AgreementItemActivity extends Activity {
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.txt_customer_name)
    private TextView txt_customer_name;
    @ViewInject(R.id.txt_client)
    private TextView txt_client;
    @ViewInject(R.id.txt_manufactor_contact)
    private TextView txt_manufactor_contact;
    @ViewInject(R.id.txt_driver_name)
    private TextView txt_driver_name;
    @ViewInject(R.id.txt_carrier)
    private TextView txt_carrier;
    @ViewInject(R.id.txt_license_num)
    private TextView txt_license_num;
    @ViewInject(R.id.txt_driver_tel)
    private TextView txt_driver_tel;
    @ViewInject(R.id.txt_driver_moblie)
    private TextView txt_driver_moblie;
    @ViewInject(R.id.txt_goods_name)
    private TextView txt_goods_name;
    @ViewInject(R.id.txt_all_count)
    private TextView txt_all_count;
    @ViewInject(R.id.txt_cross_pay)
    private TextView txt_cross_pay;
    @ViewInject(R.id.txt_all_freight)
    private TextView txt_all_freight;
    @ViewInject(R.id.txt_advance_pay)
    private TextView txt_advance_pay;
    @ViewInject(R.id.txt_default_pay)
    private TextView txt_default_pay;
    @ViewInject(R.id.txt_cost)
    private TextView txt_cost;
    @ViewInject(R.id.txt_starting_point)
    private TextView txt_starting_point;
    @ViewInject(R.id.txt_target_location)
    private TextView txt_target_location;
    @ViewInject(R.id.txt_start_date)
    private TextView txt_start_date;
    @ViewInject(R.id.txt_arrive_date)
    private TextView txt_arrive_date;
    @ViewInject(R.id.txt_handler_person)
    private TextView txt_handler_person;
    @ViewInject(R.id.txt_agreement_data)
    private TextView txt_agreement_data;
    @ViewInject(R.id.txt_message_pay)
    private TextView txt_message_pay;
    @ViewInject(R.id.txt_Accommodation)
    private TextView txt_Accommodation;
    @ViewInject(R.id.txt_court)
    private TextView txt_court;
    @ViewInject(R.id.txt_contracting_place)
    private TextView txt_contracting_place;
    private Intent intent;
    private String id = "0";
    private ContractService contractService;
    private String[] str_business;
    private boolean hasGetInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_item);
        x.view().inject(AgreementItemActivity.this);
        baseTitleView.setTitle(getResources().getString(R.string.agreement_info));
        contractService=new ContractService(this);
        if (intent==null)
            intent=getIntent();
        id = intent.getStringExtra("id");
        getData();
    }

    /**
     * 获取合同信息数据
     */
    public void getData(){
        contractService.getOneAgreementInfo(id, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects[0] != null && objects.length > 0) {
                	hasGetInfo = true;
                    ContactDTO contactDTO = (ContactDTO) objects[0];
                    txt_customer_name.setText("客户姓名：" + contactDTO.getBossName());
                    txt_client.setText("委托方：" + contactDTO.getCompany());
                    txt_manufactor_contact.setText("厂家联系：" + contactDTO.getBossMobile());
                    txt_driver_name.setText("司机姓名：" + contactDTO.getDriverName());
                    txt_carrier.setText("承运人：" + contactDTO.getBforShortName());
                    txt_license_num.setText("驾驶证号：" + contactDTO.getDriverLicense());
                    txt_driver_tel.setText("司机电话：" + contactDTO.getDriverFamilyTel());
                    txt_driver_moblie.setText("司机手机：" + contactDTO.getDriverMobile());
                    txt_goods_name.setText("货物名称：" + contactDTO.getGoodName());
                    txt_all_count.setText("总件数：" + contactDTO.getAmount());
                    txt_cross_pay.setText("过路支付：" + contactDTO.getOverLoadExes());
                    txt_all_freight.setText("全程运费：" + contactDTO.getTotalExes() + "(" +
                            contactDTO.getTotalExes() + ")");
                    txt_advance_pay.setText("预付运费：" + contactDTO.getPrepaidExes2() + "(" +
                            contactDTO.getPrepaidExes() + ")");
                    txt_default_pay.setText("尚欠运费：" + contactDTO.getOddExes2() + "(" + contactDTO
                            .getOddExes() + ")");
                    txt_cost.setText("总价值：" + contactDTO.getTotalValue2() + "(" + contactDTO
                            .getTotalValue() + ")");
                    txt_starting_point.setText("起运地点：" + contactDTO.getStartAddress());
                    txt_target_location.setText("目的地：" + contactDTO.getTargetAddress());
                    txt_start_date.setText("起运日期：" + contactDTO.getStartDate());
                    txt_arrive_date.setText("到达日期：" + contactDTO.getExpiryDatetime());
                    str_business = contactDTO.getBusiness();
                    txt_agreement_data.setText("合同日期：" + contactDTO.getContractDate());
                    txt_message_pay.setText("信息费：" + contactDTO.getInforAmount());
                    txt_Accommodation.setText("住宿费：" + contactDTO.getHotelChange());
                    txt_court.setText("合同法院：" + contactDTO.getCourt());
                    txt_contracting_place.setText("签约地点：" + contactDTO.getCourtCity());
                    getAllContacts();
                }
            }
        });
    }

    /**
     *  获取合同处理人
     */
    public void getAllContacts(){
        GoodsService goodsService=new GoodsService(this);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("处理人员：");
        for (int i=0;i<str_business.length;i++){
            int id=Integer.parseInt(str_business[i]);
            LogUtils.e("AgreementItem","   "+id);
            if (goodsService.selectUserById(id)!=null&&(goodsService.selectUserById(id)).getCname
                    ()!=null) {
                stringBuffer.append((goodsService.selectUserById(id)).getCname() + " ");
            }
            LogUtils.e("AgreementItem","   "+stringBuffer.toString());
        }
        txt_handler_person.setText(stringBuffer.toString());
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasGetInfo) {
//    		getData();
    	}
    }
}
