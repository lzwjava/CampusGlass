package lzw.campus.glass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;

public class TalkActivity extends Activity {
	EditText replyEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talk_activity);
		setActionBarDrawable(R.drawable.bar); 
		
		AVAnalytics.trackAppOpened(getIntent());
		
		setTextView();
		replyEdit=(EditText)findViewById(R.id.reply);
	}
	
	void setTextView(){
		TextView tv=(TextView)findViewById(R.id.words); 
		String words=intentGet("words");
		tv.setText(words);
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

	void setActionBarDrawable(int id) {
		ActionBar actionBar=getActionBar();
		Drawable drawable=this.getResources().getDrawable(id);
	  actionBar.setBackgroundDrawable(drawable);
	}
	
	void toast(String txt){
		Toast.makeText(TalkActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator=new MenuInflater(this);
		inflator.inflate(R.menu.main,menu);
		return super.onCreateOptionsMenu(menu);
	}

	boolean isEmpty(String str,String prompt){
		if(str.isEmpty()){
			toast(prompt);
			return true;
		}
		return false;
	}
	
	public void throwIt(View v){
		String reply=replyEdit.getText().toString();
		if(isEmpty(reply,"回一句吧")){
			return;
		}
		
		String id=intentGet("installationId");
		
		AVQuery pushQuery = AVInstallation.getQuery();
		pushQuery.whereEqualTo("installationId", id);
		AVPush.sendMessageInBackground(reply, pushQuery,
				new SendCallback() {
					@Override
					public void done(AVException e) {
						toast("已回复");
						finish();
					}
				});
	}
}
