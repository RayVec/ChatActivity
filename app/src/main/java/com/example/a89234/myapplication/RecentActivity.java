package com.example.a89234.myapplication;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RecentActivity extends Activity{
	ListView listView;
	List<RecentEntity> recentEntityList=new ArrayList<RecentEntity>();
	String[] mes;
	int myAccount;
	MyBroadcastReceiver br;
	IntentFilter myIntentFilter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recent);
		myAccount=getIntent().getIntExtra("account",0);
		//注册广播
		myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();
		recentEntityList.add(new RecentEntity(1,1234,"公共测试圆桌"," "," ",0));
		listView = (ListView) findViewById(R.id.lv_recent);
		listView.setAdapter(new RecentAdapter(RecentActivity.this,recentEntityList));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				//打开聊天页面
				//广播内容依次为发送者账号，发送者昵称，发送者头像，内容，时间
				Intent intent = new Intent(RecentActivity.this, ChatActivity.class);
				intent.putExtra("receiver", recentEntityList.get(position).getAccount());
				intent.putExtra("nick", recentEntityList.get(position).getNick());
				intent.putExtra("account", myAccount);
				startActivity(intent);
			}
		});
        }

	@Override
	public void onStop(){
		super.onStop();
		unregisterReceiver(br);
	}
	@Override
	public void onResume(){
		super.onResume();
		registerReceiver(br,myIntentFilter);
	}

	//广播接收器
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//广播内容依次为发送者账号，发送者昵称，发送者头像，内容，时间
			mes = intent.getStringArrayExtra("message");
		    Toast.makeText(context,mes[1]+" : "+mes[3], Toast.LENGTH_SHORT).show();
			//更新最近会话列表， 检测chatEntityList，防止同一个好友的消息出现多个会话实体
		    Iterator it=recentEntityList.iterator();
		    int unRead=0;
		    if(recentEntityList!=null && recentEntityList.size()!=0){
		    	while(it.hasNext()){
		    		RecentEntity re=(RecentEntity) it.next();
		    		if(re.getAccount()==1234) {
		    			unRead=re.getUnRead()+1;
						recentEntityList.remove(re);
					}
		    	}
		    }
		    recentEntityList.add(new RecentEntity(
		    		Integer.parseInt(mes[2]), 
		    		1234,
		    		"公共测试圆桌",
		    		mes[1]+" : "+mes[3],
		    		mes[4],
					unRead));
		    listView.setAdapter(new RecentAdapter(RecentActivity.this, recentEntityList));
		}
	}
}
