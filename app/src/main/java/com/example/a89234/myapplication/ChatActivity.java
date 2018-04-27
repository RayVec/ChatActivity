package com.example.a89234.myapplication;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import common.ChatMessage;
import common.ChatMessageType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity {
    public static String myInfo;
    EditText et_input;
    private String chatContent;//消息内容
    ListView chatListView;
    private int myAccount;
    private int deskAccount;
    private String nick;
    IntentFilter myIntentFilter;
    public List<ChatEntity> chatEntityList=new ArrayList<ChatEntity>();//所有聊天内容
    public static int[] avatar=new int[]{R.drawable.avatar_default,R.drawable.h001,R.drawable.h002,R.drawable.h003,
            R.drawable.h004,R.drawable.h005,R.drawable.h006};
    MyBroadcastReceiver br;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置不需要标题
        setContentView(R.layout.activity_main);   //设置主界面
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //这里获取当前聊天者的账号，目前可以使用的有1，2，3，4，5五个数字
        myAccount=getIntent().getIntExtra("account", 0);
        //这里指定接收圆桌的圆桌号，目前只开通1234
        nick=getIntent().getStringExtra("nick");
        deskAccount=getIntent().getIntExtra("receiver",0);
        ImageView avatar_iv=(ImageView) findViewById(R.id.chat_top_avatar);
        avatar_iv.setImageResource(avatar[1]);   //设置头像
        TextView nick_tv=(TextView) findViewById(R.id.chat_top_nick);
        nick_tv.setText(nick);
        et_input=(EditText) findViewById(R.id.et_input);
        /**
         * 获取当前圆桌所有的消息
         *
         */
        findViewById(R.id.ib_send).setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                ObjectOutputStream oos;
                try {
                            //通过account找到该线程，从而得到OutputStream
                    //得到输入的数据，并清空EditText
                    chatContent=et_input.getText().toString();
                    et_input.setText("");
                    /**
                     *此处应加判断用户是否在线
                     *
                     *
                     */
                    //发送消息
                    ChatMessage m=new ChatMessage();
                    m.setType(ChatMessageType.COM_MES);//普通信息包
                    m.setSender(myAccount);          //设置账号
                    m.setSenderNick("ray");  //设置昵称
                    m.setSenderAvatar(1);   //设置头像
                    m.setReceiver(deskAccount);          //设置接收者的账号
                    m.setContent(chatContent);           //设置消息内容
                    m.setSendTime(MyTime.geTimeNoS());    //设置发送的时间
                    /**
                     * 若离线，则将信息m发送到此圆桌的离线消息内
                     *
                     *
                     */
                    oos = new ObjectOutputStream(ManageClientConServer.getClientConServerThread(myAccount).getS().getOutputStream());
                    oos.writeObject(m);
                    //更新聊天内容
                    updateChatView(new ChatEntity(
                            chatContent,
                            MyTime.geTime(),
                            false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //注册广播
         myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();    //创建一个广播接收器
        registerReceiver(br, myIntentFilter);
        ManageActivity.addActiviy("ChatActivity", this);
    }
    @Override
    public void finish() {
        unregisterReceiver(br);
        super.finish();
    }
    @Override
    public void onStop(){
        unregisterReceiver(br);
        super.onStop();
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
            String[] mes = intent.getStringArrayExtra("message");
            //更新聊天内容
            updateChatView(new ChatEntity(
                    mes[3],
                    mes[4],
                    true));
        }
    }
    public void updateChatView(ChatEntity chatEntity){
        chatEntityList.add(chatEntity);
        chatListView=(ListView) findViewById(R.id.lv_chat);
        chatListView.setAdapter(new MsgAdapter(this,chatEntityList));
    }

}
