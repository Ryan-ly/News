package com.leaf.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.leaf.news.gson.News;
import com.leaf.news.util.HttpUtil;
import com.leaf.news.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment_tech extends Fragment {

    private List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private String contends;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_tech, container, false);

        bundle = getArguments();

        refreshLayout = (SwipeRefreshLayout) view1.findViewById(R.id.swipe_layout6);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView) view1.findViewById(R.id.list_view);
        adapter = new TitleAdapter(getContext(), R.layout.listview_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(getActivity(), ContentsActivity.class);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String spFileName = getResources().getString(R.string.shared_preferences_file_name);
                String loginId = getResources().getString(R.string.login_status);
                SharedPreferences spFile = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);
                Title title = titleList.get(position);
                intent.putExtra("title", title.getTitle());
                intent.putExtra("uri", title.getUri());
                if(spFile.getString(loginId,null)==null ||!spFile.getString(loginId,null).equals("-1")){
                    Record record = new Record();
                    record.setTitle(title.getTitle());
                    record.setUrl(title.getUri());
                    record.setId(spFile.getString(loginId,null));
                    Gson gson = new Gson();
                    String json=gson.toJson(record);
                    HttpUtil.sendOkHttpRequest1("http://106.52.184.133:7001/record", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        }

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }
                    },json);
                }
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestNew();
            }
        });
        requestNew();

        return view1;
    }

    private void requestNew() {
        // 根据返回的 URL 链接进行申请和返回数据
        //String address = "https://api.tianapi.com/keji/?key=525e52abf67d5340c48e9de0eaa395ec&num=50&rand=1";
        String address = "http://api.tianapi.com/sicprobe/index?key=3379a0aa4890b846cbfe177a17a61946&num=50";
        String current_act = getActivity().getClass().getSimpleName();
        if(current_act.equals( "SearchActivity")){
            contends = bundle.getString("search_contends");
            address = "http://api.tianapi.com/sicprobe/index?key=3379a0aa4890b846cbfe177a17a61946&num=50&word="+contends;
        }

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "新闻加载失败", Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                System.out.println(responseText);
                final NewsList newlist = Utility.parseJsonWithGson(responseText);
                final int code = newlist.code;
                final String msg = newlist.msg;
                if (code == 200) {
                    titleList.clear();
                    for (News news : newlist.newsList) {
                        Title title = new Title(news.title, news.source,news.description, news.picUrl, news.url, news.time);
                        titleList.add(title);
                    }

                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                listView.setSelection(0);
                                refreshLayout.setRefreshing(false);
                            }

                            ;
                        });
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "数据错误返回", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });


    }
}
