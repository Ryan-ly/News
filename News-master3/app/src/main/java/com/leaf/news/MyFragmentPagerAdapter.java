package com.leaf.news;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tab_title_list;
    private ArrayList<Fragment> fragment_list;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<String> tab_title_list, ArrayList<Fragment> fragment_list) {
        super(fm);
        this.tab_title_list = tab_title_list;
        this.fragment_list = fragment_list;
    }
    //    获取某个新闻块
    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }
    //    新闻类型数量
    @Override
    public int getCount() {
        return fragment_list.size();
    }
    //    获取某个新闻类型
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title_list.get(position);
    }
}