package lzw.campus.glass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lzw.util.GlassUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

public class ChatActivity extends Activity {
	ListView mListView;
	EditText etSend;
	ChatMsgViewAdapter mAdapter;
	Button btnSend,btnBack;
	List<ChatMsgEntity> mDataArrays=new 
	  ArrayList<ChatMsgEntity>();
	TextView title;
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
		title=(TextView)findViewById(R.id.title);
		
		ClickListner listner=new ClickListner();
		btnSend.setOnClickListener(listner);
		btnBack.setOnClickListener(listner);
	}
	
	void initData(){
	  CampusGlassApp app=(CampusGlassApp)getApplication();
	  AVObject curGlass=app.curGetGlass; 
	  
	  final String me=AVUser.getCurrentUser().getUsername();
	  String he=curGlass.getString("username");
	  title.setText(he);
	  
    AVQuery<AVObject> query = glassQuery(me, he);
		query.orderByAscending("createdAt");
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> glasses, AVException e) {
				if (e == null) {
					for(AVObject glass : glasses){
						ChatMsgEntity entity=new ChatMsgEntity();
						entity.text=glass.getString("words");
						entity.name=glass.getString("username");
						entity.date=glass.getCreatedAt().toString();
						if(entity.name.equals(me)){
							entity.isComMsg=false;
						}else entity.isComMsg=true; 
						mDataArrays.add(entity);
					}
					mAdapter = new ChatMsgViewAdapter(ChatActivity.this, mDataArrays);
					mListView.setAdapter(mAdapter);
					mListView.setSelection(mListView.getCount()-1);
				}else{
					e.printStackTrace();
				}
	  	}
	  });
	}

	AVQuery<AVObject> glassQuery(String username, String toUser) {
		AVQuery<AVObject> query1=getEqualQuery("username", username,"toUser",toUser);
	  AVQuery<AVObject> query2=getEqualQuery("username",toUser,"username",toUser);
	  List<AVQuery<AVObject>> queries=new ArrayList<AVQuery<AVObject>>();
	  queries.add(query1);
	  queries.add(query2);
	  AVQuery<AVObject> mainQuery=AVQuery.or(queries);
	  return mainQuery;
	}

	AVQuery<AVObject> getEqualQuery(String... pairs) {
		AVQuery<AVObject> query=AVQuery.getQuery("Glass");
		for(int i=0;i<pairs.length/2;i++){
			query.whereEqualTo(pairs[2*i],pairs[2*i+1]);
		}
	  return query;
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
		String me=AVUser.getCurrentUser().getUsername();
		ChatMsgEntity entity=new ChatMsgEntity(me,
      date(),text,false);
		mDataArrays.add(entity);
		String toUser=getFromCurGlass(Constant.TO_USER);
		GlassUtil.throwGlass(text, toUser);
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
	
	String getFromCurGlass(String key){
		CampusGlassApp app=(CampusGlassApp)getApplication();
		AVObject glass=app.curGetGlass;
		return glass.getString(key);
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
	
}
