package lzw.campus.glass;

import lzw.util.GlassUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;

public class ThrowGlassActivity extends Activity {
	EditText wordsEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throw_glass_activity);
		setActionBarDrawable(R.drawable.bar);
		
		AVAnalytics.trackAppOpened(getIntent());
		
		wordsEdit=(EditText)findViewById(R.id.word);
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
		Toast.makeText(ThrowGlassActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator=new MenuInflater(this);
		inflator.inflate(R.menu.main,menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void throwIt(View v){
		String words=wordsEdit.getText().toString();
		GlassUtil.throwGlass(words,null);
		dialog();
	}
	
	void dialog() {
		new AlertDialog.Builder(ThrowGlassActivity.this).setTitle("亲爱的")
				.setMessage("瓶子已经扔向大海").setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//pushAll();
						//Util.intentToClass(getApplicationContext(), GlassRoomActivity.class);
						finish();
					}
				}).create().show();
	}
}
