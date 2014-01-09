package lzw.model;

import lzw.fast.login.R;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GlassRoomActivity extends Activity {
	EditText searchWordEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glass_room_activity);
		setActionBarDrawable(R.drawable.bar);
		searchWordEdit=(EditText)findViewById(R.id.searchWord);
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
	
	public void search(View v){
		
	}
}
