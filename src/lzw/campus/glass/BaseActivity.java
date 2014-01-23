package lzw.campus.glass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity); 
	}
	
	public void back(View v){
		back();
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
		Toast.makeText(BaseActivity.this,txt,
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
