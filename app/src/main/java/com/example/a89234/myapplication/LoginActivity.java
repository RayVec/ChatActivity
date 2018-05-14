package com.example.a89234.myapplication;

import common.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public static String userInfo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	    Button btnLogin=(Button) findViewById(R.id.btn_login);
	    btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
					login("f1776e5c7273e5b63120003c200ea6d");
			}
	    });

	    ManageActivity.addActiviy("LoginActivity", this);
	}
	
	void login(String a){
		User user=new User();
		user.setAccount(a);
		user.setOperation("login");
		//在后台注册此线程
		boolean b=new ChatClient(this).sendLoginInfo(user);
		//登陆成功
		if(b){
			//转到主界面
			HTTPUtils.cookie=HTTPUtils.setCookie();
			Log.d("cookie",HTTPUtils.cookie);
			Intent intent=new Intent(LoginActivity.this,RecentActivity.class);
			intent.putExtra("myaccount", a);
			intent.putExtra("mynick","ray");
			intent.putExtra("myavatar","1");
			startActivity(intent);
		}else {
			Toast.makeText(this, "登陆失败！", Toast.LENGTH_SHORT).show();
		}
	}
}
