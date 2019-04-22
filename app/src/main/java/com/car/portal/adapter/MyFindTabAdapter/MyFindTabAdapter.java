package com.car.portal.adapter.MyFindTabAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**ViewPager的Adapter
 Created by Qinghong on 2019/1/19.
 */
public class MyFindTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;                         //fragment列表
    private List<String> mTitles;                              //tab名的列表


    public MyFindTabAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.mFragmentList=fragmentList;
        this.mTitles=titles;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }


}
