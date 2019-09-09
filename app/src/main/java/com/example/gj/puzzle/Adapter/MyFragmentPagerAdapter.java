package com.example.gj.puzzle.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gj.puzzle.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gj
 * Created on 19-9-9
 * Description
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> tabFragments=new ArrayList<>();
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 1; i < 6; i++) {
            mTitles.add("难度 " + i);
            tabFragments.add(MyFragment.newInstance(i+""));
        }

    }

    @Override
    public Fragment getItem(int position) {
        return  tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
