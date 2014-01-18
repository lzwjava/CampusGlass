package lzw.campus.glass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;

public class ChatActivity extends Activity {
	ListView mListView;
	EditText etSend;
	ChatMsgViewAdapter mAdapter;
	Button btnSend,btnBack;
	List<ChatMsgEntity> mDataArrays=new 
	  ArrayList<ChatMsgEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		
		//getWindow().setSoftInputMode(WindowManager.
			//LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initData();
	}
	
	void initView(){
		mListView=(ListView)findViewById(R.id.listView);
		etSend=(EditText)findViewById(R.id.et_sendMessage);
		btnSend=(Button)findViewById(R.id.btn_send);
		btnBack=(Button)findViewById(R.id.btn_back);
		
		ClickListner listner=new ClickListner();
		btnSend.setOnClickListener(listner);
		btnBack.setOnClickListener(listner);
	}
	
	void initData(){
	  ChatMsgEntity entity=new ChatMsgEntity();
	  entity.text=intentGet("words");
	  entity.name="Ta";
	  entity.date="2014-1-1";
	  entity.isComMsg=true;
	  mDataArrays.add(entity);
	  
		mAdapter=new ChatMsgViewAdapter
		  (this,mDataArrays);
		mListView.setAdapter(mAdapter);
	}
	
	class ClickListner implements 
	 android.view.View.OnClickListener{
		public void onClick(View v){
			switch(v.getId()){
				case R.id.btn_send:
					send();
				  break;
				case R.id.btn_back:
					back();
					break;
			}
		}
	}
	
	void send(){
		String text=etSend.getText().toString();
		if(text.isEmpty()) return;
		ChatMsgEntity entity=new ChatMsgEntity("人马",
    date(),text,false);
		mDataArrays.add(entity);
		throwIt(text);
		etSend.setText(""); 
		mListView.setSelection(mListView.getCount()-1);
	}
	
	String date(){
		Date date=new Date();
		return date.toString();
	}
	
	void back(){
		finish();
	}
	
	String intentGet(String key){
		Intent intent=getIntent();
		return intent.getStringExtra(key);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		super.onOptionsItemSelected(mi);
		return false;
	}
	
	void toast(String txt){
		Toast.makeText(ChatActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	boolean isEmpty(String str,String prompt){
		if(str.isEmpty()){
			toast(prompt);
			return true;
		}
		return false;
	}
	
	public void throwIt(String reply){
		String id=intentGet("installationId");
		AVQuery pushQuery = AVInstallation.getQuery();
		pushQuery.whereEqualTo("installationId", id);
		AVPush.sendMessageInBackground(reply, pushQuery,
				new SendCallback() {
					@Override
					public void done(AVException e) {
						//toast("已回复");
						//finish();
					}
				});
	}
	
}
