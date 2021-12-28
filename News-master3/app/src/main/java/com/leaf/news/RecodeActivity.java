package com.leaf.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.leaf.news.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecodeActivity extends AppCompatActivity {
    private List<Record> recordList = new ArrayList<Record>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode);

        listView = findViewById(R.id.list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("历史记录");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String loginId = getResources().getString(R.string.login_status);
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        final Record record = new Record();
        record.setId(spFile.getString(loginId,null));
        Gson gson = new Gson();
        String json=gson.toJson(record);
        if(spFile.getString(loginId,null)==null || !spFile.getString(loginId,null).equals("-1")){
            HttpUtil.sendOkHttpRequest1("http://10.34.58.115:7001/searchrecord", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    Gson gson = new Gson();
                    final RecodeList list = gson.fromJson(responseText, RecodeList.class);
                    final int code = list.code;
                    if (code == 200) {
                        recordList.clear();
                        String[] recodes=new String[list.data.size()];
                        int i = 0;
                        for (Record record : list.data) {
                            Record record1 = new Record(record.getTitle(), record.getUrl());
                            recordList.add(record1);
                            recodes[i++] = record.getTitle();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecodeActivity.this,
                                android.R.layout.simple_list_item_1,recodes);
                        listView.setAdapter(adapter);
                    }else{
                        System.out.println("错误");
                    }
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(RecodeActivity.this, "历史记录查询错误，请尝试登录", Toast.LENGTH_SHORT).show();
                }
            },json);
        }else{
            Toast.makeText(RecodeActivity.this, "历史记录查询错误，请尝试登录", Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(RecodeActivity.this, ContentsActivity.class);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String spFileName = getResources().getString(R.string.shared_preferences_file_name);
                String loginId = getResources().getString(R.string.login_status);
                SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
                Record record1 = recordList.get(position);
                intent.putExtra("title", record1.getTitle());
                intent.putExtra("uri", record1.getUrl());
                startActivity(intent);
            }
        });
    }
}