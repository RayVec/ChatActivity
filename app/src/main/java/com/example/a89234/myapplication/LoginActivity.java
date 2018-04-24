package com.example.a89234.myapplication;

import common.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public static String userInfo;
	EditText accountEt,receiverEt;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	    accountEt=(EditText) findViewById(R.id.et_account);
	    receiverEt=(EditText)findViewById(R.id.receiver_account);
	    Button btnLogin=(Button) findViewById(R.id.btn_login);
	    btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if(accountEt.getText().equals("") ){
					Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					login(Integer.parseInt(accountEt.getText().toString()));
				}
			}
	    });
	    ManageActivity.addActiviy("LoginActivity", this);
	}
	
	void login(int a){
		User user=new User();
		user.setAccount(a);
		user.setOperation("login");
		//在后台注册此线程
		boolean b=new ChatClient(this).sendLoginInfo(user);
		//登陆成功
		if(b){
			//转到主界面
			Intent intent=new Intent(LoginActivity.this,ChatActivity.class);
			intent.putExtra("account", Integer.parseInt(accountEt.getText().toString()));
			intent.putExtra("receiver",Integer.parseInt(receiverEt.getText().toString()));
			startActivity(intent);
		}else {
			Toast.makeText(this, "登陆失败！", Toast.LENGTH_SHORT).show();
		}
	}
}
