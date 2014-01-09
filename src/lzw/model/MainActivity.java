package lzw.model;

import lzw.fast.login.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText userName,password;
	boolean isDebug=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		userName=(EditText)findViewById(R.id.userName);
		password=(EditText)findViewById(R.id.password);
		setActionBarDrawable(R.drawable.bar);
		
	}
	
	void setActionBarDrawable(int id) {
		ActionBar actionBar=getActionBar();
		Drawable drawable=this.getResources().getDrawable(id);
	  actionBar.setBackgroundDrawable(drawable);
	}
	
	void toast(String txt){
		Toast.makeText(MainActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	void intentToClass(Context ctx, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(ctx, cls);
		startActivity(intent);
	}
	
	public void login(View v){
		String userNameStr=userName.getText().toString();
		if(userNameStr.isEmpty()){
			toast("«Î ‰»Î’À∫≈");
			return;
		}
		
		String passwordStr=password.getText().toString();
		if(passwordStr.isEmpty()){
			toast("«Î ‰»Î√‹¬Î");
			return;
		}
		
		if(NetworkUtil.isConnect(MainActivity.this)){
			boolean isLogin=false;
			isLogin=Login.checkLogin(userNameStr, passwordStr);
			if(isLogin || isDebug)
				intentToClass(getApplicationContext(), GlassRoomActivity.class);
		}else{
			toast("µ«¬º ß∞‹");
		}
	}
}
