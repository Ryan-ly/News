package com.leaf.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
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
        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String accountKey = getResources().getString(R.string.login_account_name);
        String passwordKey = getResources().getString(R.string.login_password);
        String rememberPasswordKey = getResources().getString(R.string.login_remember_password);
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        String account = spFile.getString(accountKey,null);
        String password = spFile.getString(passwordKey,null);
        Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey,false);
        if(account != null && !TextUtils.isEmpty(account)){
            etAccount.setText(account);
        }
        if(password != null && !TextUtils.isEmpty(password)){
            etPwd.setText(password);
        }
        cbRememberPwd.setChecked(rememberPassword);
    }

    @Override
    public void onClick(View view) {

        //判断登录成功与否
        UserInfo user = new UserInfo();
        user.setAccount(etAccount.getText().toString());
        user.setPassword(etPwd.getText().toString());
        Gson gson = new Gson();
        String json=gson.toJson(user);
        HttpUtil.sendOkHttpRequest1("http://10.34.58.115:7001/login", new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                try {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String responseText = response.body().string();
                                Gson gson = new Gson();
                                UserInfo data = gson.fromJson(responseText, UserInfo.class);
                                System.out.println(data.getId());
                                if(data.getId().equals("-1")){
                                    Toast.makeText(LoginActivity.this,"登录失败，账号或密码错误",Toast.LENGTH_LONG).show();
                                }else{
                                    String spFileName = getResources().getString(R.string.shared_preferences_file_name);
                                    SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = spFile.edit();
                                    String loginStatus = getResources().getString(R.string.login_status);
                                    String loginName = getResources().getString(R.string.login_name);
                                    editor.putString(loginStatus,data.getId());
                                    editor.putString(loginName,data.getUsername());
                                    editor.apply();
                                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        },json);
        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String accountKey = getResources().getString(R.string.login_account_name);
        String passwordKey = getResources().getString(R.string.login_password);
        String rememberPasswordKey = getResources().getString(R.string.login_remember_password);
        String loginStatus = getResources().getString(R.string.login_status);
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spFile.edit();
        if(cbRememberPwd.isChecked()){
            String password = etPwd.getText().toString();
            String account = etAccount.getText().toString();
            editor.putString(accountKey,account);
            editor.putString(passwordKey,password);
            editor.putBoolean(rememberPasswordKey,true);
            editor.apply();
        }else{
            editor.remove(accountKey);
            editor.remove(passwordKey);
            editor.remove(rememberPasswordKey);
            editor.apply();
        }
    }

}