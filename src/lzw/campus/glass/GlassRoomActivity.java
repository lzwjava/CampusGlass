package lzw.campus.glass;

import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;

public class GlassRoomActivity extends Activity {
	EditText searchWordEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glass_room_activity);
		setActionBarDrawable(R.drawable.bar);
		searchWordEdit=(EditText)findViewById(R.id.searchWord);
		
		AVAnalytics.trackAppOpened(getIntent());
		PushService.setDefaultPushCallback(this, GlassRoomActivity.class);
		AVInstallation.getCurrentInstallation().saveInBackground();
		
		checkIfOldUser(); 
		checkIfNeedToast();
	}

	void checkIfNeedToast() {
		Intent intent=getIntent();
		if(intent.getBooleanExtra(Constant.TOAST_AFTER_THROW_BACK, 
				false)==true){
			toast("已回复");
		}
	}
	
	void checkIfOldUser() {
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
			// 允许用户使用应用
		} else {
			// 缓存用户对象为空时， 可打开用户注册界面…
			Util.intentToClass(getApplicationContext(), LoginActivity.class);
			finish();
		}
	}
	
	void logOff(){
		AVUser.logOut();             //清除缓存用户对象
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		// TODO Auto-generated method stub
		switch(mi.getItemId()){
			case R.id.registerLogin:
				logOff();
				Util.intentToClass(getApplicationContext(), LoginActivity.class);
				finish();
				break;
		}
		super.onOptionsItemSelected(mi);
		return false;
	}

	void setActionBarDrawable(int id) {
		ActionBar actionBar=getActionBar();
		Drawable drawable=this.getResources().getDrawable(id);
	  actionBar.setBackgroundDrawable(drawable);
	}
	
	void toast(String txt){
		Toast.makeText(GlassRoomActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator=new MenuInflater(this);
		inflator.inflate(R.menu.glass_room_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void search(View v){
		String word=searchWordEdit.getText().toString();
		AVQuery<AVObject> query=new AVQuery<AVObject>("Glass");
		query.whereMatches("words",word);
		query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject>list, AVException e){
				if(e==null){
					int n=list.size();
					Random rand=new Random(new Date().getTime());
					int randId=rand.nextInt(n);
					AVObject obj=list.get(randId);
					String id=obj.getString("installationId");
					dialog(obj.getString("words"),id);
				}else{
					//e.printStackTrace();
					dialogHaveNone();
				}
			}
		});
		
	}
	
	void dialogHaveNone(){
		new AlertDialog.Builder(GlassRoomActivity.this).setTitle("报告")
				.setMessage("对不起，没打捞到~").setPositiveButton("算了", null)
				.create().show();
	}
	
	void dialog(final String words,final String id) {
		new AlertDialog.Builder(GlassRoomActivity.this).setTitle("Ta说")
				.setMessage(words).setPositiveButton("向对方回一个瓶子", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent=new Intent();
						intent.putExtra("words",words);
						intent.putExtra("installationId",id);
						intent.setClass(GlassRoomActivity.this, TalkActivity.class);
						startActivity(intent);
					}
				}).setNegativeButton("摔了", new OnClickListener(){
					public void onClick(DialogInterface dialog,int which){
						//TODO
					}
				}).create().show();
	}
	
	public boolean throwGlass(MenuItem v){
		Util.intentToClass(GlassRoomActivity.this, ThrowGlassActivity.class);
		return false;
	}
}
