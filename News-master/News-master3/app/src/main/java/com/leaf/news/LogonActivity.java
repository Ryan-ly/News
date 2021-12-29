package com.leaf.news;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.leaf.news.gson.UserInfo;
import com.leaf.news.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LogonActivity extends AppCompatActivity implements View.OnClickListener{
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        etUserName = findViewById(R.id.et_name);
        Button btLogin = findViewById(R.id.bt_login);
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPwdSwitch = !bPwdSwitch;
                if(bPwdSwitch){
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        注册
        UserInfo user = new UserInfo();
        user.setAccount(etAccount.getText().toString());
        user.setPassword(etPwd.getText().toString());
        user.setUsername(etUserName.getText().toString());
        Gson gson = new Gson();
        String json=gson.toJson(user);
        HttpUtil.sendOkHttpRequest1("http://106.52.184.133:7001/logon", new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                LogonActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String responseText = response.body().string();
                            System.out.println(responseText);
                            if(responseText.equals("1")){
                                Toast.makeText(LogonActivity.this,"注册成功,去登录", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LogonActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else if(responseText.equals("-1")){
                                Toast.makeText(LogonActivity.this,"账号重复", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(LogonActivity.this,"注册失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        },json);

    }

}