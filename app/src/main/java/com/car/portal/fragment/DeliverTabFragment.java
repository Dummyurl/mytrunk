package com.car.portal.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.car.portal.R;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.view.ChildViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

public class DeliverTabFragment extends Fragment {

    TabLayout tabDeliver;
    ChildViewPager tabViewpage;
    @BindView(R.id.img_left_jt)
    GifImageView imgLeftJt;
    @BindView(R.id.img_right_jt)
    GifImageView imgRightJt;


    private View view;
    FragmentPagerAdapter adapter;
    private List<String> tab_list = new ArrayList<>();
    private List<Fragment> fragments_list = new ArrayList<>();
    public GotoFragment gotoFragment;
    public commonlyusedbean commons;

    @OnClick({R.id.img_left_jt, R.id.img_right_jt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_jt:
                tabViewpage.setCurrentItem(0);
                break;
            case R.id.img_right_jt:
                tabViewpage.setCurrentItem(1);
                break;
        }
    }


    public interface GotoFragment {
        void switchFragment(ChildViewPager viewPager);
    }

    public void setFragment(GotoFragment gotoFragment) {
        this.gotoFragment = gotoFragment;
    }

    public void forSkip() {
        if (gotoFragment != null) {
            gotoFragment.switchFragment(tabViewpage);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.from(getActivity()).inflate(
                    R.layout.activity_delivertab, null);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        ButterKnife.bind(this, view);
        tabDeliver = view.findViewById(R.id.tab_deliver);
        tabViewpage = view.findViewById(R.id.tab_viewpage);
        tab_list.add("货源发布");
        tab_list.add("常发货源");
        fragments_list.add(new DeliverFragment());
        fragments_list.add(new CommonlyusedFragment());
        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments_list.get(i);
            }

            @Override
            public int getCount() {
                return fragments_list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tab_list.get(position);
            }
        };
        tabViewpage.setOffscreenPageLimit(1);
        tabViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1) {
                    imgLeftJt.setVisibility(View.VISIBLE);
                    imgRightJt.setVisibility(View.GONE);
                }else {
                    imgLeftJt.setVisibility(View.GONE);
                    imgRightJt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabViewpage.setAdapter(adapter);
        tabDeliver.setupWithViewPager(tabViewpage);
    }

    public void setDataex(commonlyusedbean name) {
        this.commons = name;
    }

    public commonlyusedbean getDataex() {
        return commons;
    }

}
