package com.leaf.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //    用于tab栏切换页面
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //    储存新闻类型
    private ArrayList<String> tab_title_list = new ArrayList<>();
    //    储存新闻块页面
    private ArrayList<Fragment> fragment_list = new ArrayList<>();
    //    定义新闻块
    private Fragment fragment1, fragment2, fragment3, fragment4, fragment5, fragment6, fragment7;
    //    定义新闻适配器
    private MyFragmentPagerAdapter adapter;
    //    侧滑交互
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private EditText search_et;
    private ImageView search_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 左上角图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            //actionBar.setTitle("MyNews");
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        String loginStatus = getResources().getString(R.string.login_status);
        String loginName = getResources().getString(R.string.login_name);
        final View textEntryView = navigationView.getHeaderView(0);
        final TextView textView = (TextView) textEntryView.findViewById(R.id.username);
        if( spFile.getString(loginStatus,null)==null|| !spFile.getString(loginStatus,null).equals("-1")){
            textView.setText(spFile.getString(loginName,null));
        }
//        导航栏初始化
        navigationView.setCheckedItem(R.id.nav_main);   //当前选择到的item
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_main:
                        break;
                    case R.id.nav_about:
                        intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_login:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logon:
                        intent = new Intent(MainActivity.this, LogonActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_history:
                        intent = new Intent(MainActivity.this, RecodeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_collect:
                        break;
                    case R.id.nav_exit:
                        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
                        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
                        String loginStatus = getResources().getString(R.string.login_status);
                        String loginName = getResources().getString(R.string.login_name);
                        SharedPreferences.Editor editor = spFile.edit();
                        editor.putString(loginStatus,"-1");
                        editor.putString(loginName,"尚未登陆");
                        editor.apply();
                        final View textEntryView = navigationView.getHeaderView(0);
                        final TextView textView = (TextView) textEntryView.findViewById(R.id.username);
                        textView.setText(spFile.getString(loginName,null));
                        Toast.makeText(MainActivity.this,"当前账号已退出", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
//                点击其他地方关闭侧滑导航
                navigationView.setCheckedItem(R.id.nav_main);
                drawerLayout.closeDrawers();
                return true;
            }
        });
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
        fragment1 = new Fragment_top();
        fragment2 = new Fragment_social();
        fragment3 = new Fragment_county();
        fragment4 = new Fragment_world();
        fragment5 = new Fragment_sport();
        fragment6 = new Fragment_tech();
        fragment7 = new Fragment_it();
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

        search_et = findViewById(R.id.search_et);
        search_iv = findViewById(R.id.search_iv);

        search_iv.setOnClickListener(this);
    }
//    用户选择事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //点击左上角home
                drawerLayout.openDrawer(GravityCompat.START); //显示侧滑菜单
                break;
            default:
        }
        return true;
    }

//    用户点击返回键事件

    @Override
    public void onBackPressed() {
        //        如果侧滑栏在则关闭侧滑栏,否则退出

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_iv){
            String contends =search_et.getText().toString();
            if(contends.length()==0){
                Toast.makeText(MainActivity.this,"请输入要搜索的关键词",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                intent.putExtra("search_contends",contends);
                intent.setClass(MainActivity.this,SearchActivity.class);
                startActivity(intent);

            }

        }
    }
}