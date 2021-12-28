package com.leaf.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<String> tab_title_list = new ArrayList<>();
    private ArrayList<Fragment> fragment_list = new ArrayList<>();
    private Fragment fragment1, fragment2, fragment3, fragment4, fragment5, fragment6, fragment7;
    private MyFragmentPagerAdapter adapter;
    private String contends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contends = getIntent().getStringExtra("search_contends");
        Bundle bundle = new Bundle();
        bundle.putString("search_contends",contends);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        tab_title_list.add("头条");
        tab_title_list.add("社会");
        tab_title_list.add("国内");
        tab_title_list.add("国际");
        tab_title_list.add("体育");
        tab_title_list.add("科技");
        tab_title_list.add("IT");
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(4)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(5)));
        tabLayout.addTab(tabLayout.newTab().setText(tab_title_list.get(6)));
        fragment1 = new Fragment_top(); fragment1.setArguments(bundle);
        fragment2 = new Fragment_social();fragment2.setArguments(bundle);
        fragment3 = new Fragment_county();fragment3.setArguments(bundle);
        fragment4 = new Fragment_world();fragment4.setArguments(bundle);
        fragment5 = new Fragment_sport();fragment5.setArguments(bundle);
        fragment6 = new Fragment_tech();fragment6.setArguments(bundle);
        fragment7 = new Fragment_it();fragment7.setArguments(bundle);
        fragment_list.add(fragment1);
        fragment_list.add(fragment2);
        fragment_list.add(fragment3);
        fragment_list.add(fragment4);
        fragment_list.add(fragment5);
        fragment_list.add(fragment6);
        fragment_list.add(fragment7);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tab_title_list, fragment_list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);//tablayout与viewpager建立关联
        tabLayout.setTabsFromPagerAdapter(adapter); // 设置适配器,好像不需要



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }


}
